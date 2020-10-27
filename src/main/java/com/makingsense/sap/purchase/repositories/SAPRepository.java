package com.makingsense.sap.purchase.repositories;

import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.models.SAPLogin;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.resilience4j.retry.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Repository that communicates with SAP via REST APIs.
 */
@Component
public class SAPRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SAPRepository.class);

    private static final String SESSION_ATTRIBUTE = "B1SESSION";

    private static final String ROUTE_ATTRIBUTE = "ROUTEID";

    private static final String SESSION_DELIMITER = ";";

    private final RestTemplate restTemplate;

    @Value("${sap.purchase.db}")
    private String companyDb;

    @Value("${sap.purchase.user}")
    private String userName;

    @Value("${sap.purchase.pass}")
    private String password;

    @Value("${sap.purchase.login.path:Login}")
    private String loginPath;

    @Value("${sap.purchase.request.path:PurchaseRequestsAlgo}")
    private String purchaseRequestPath;

    static {
        disableSslVerification();
    }

    private Function<HttpEntity, ResponseEntity> decorated;

    @Autowired
    public SAPRepository(final RestTemplate restTemplate,
                         final Retry sapRetry) {
        this.restTemplate = restTemplate;

        decorated = Retry.decorateFunction(sapRetry, (HttpEntity entity) -> this.executeWithRetry(entity));
    }

    /**
     * Creates a session with SAP server.
     *
     * @return  a {@link ResponseEntity} that contains the session information to be used later on.
     */

    private ResponseEntity<String> login() {
        final SAPLogin login = new SAPLogin(companyDb, userName, password);

        final HttpEntity<SAPLogin> entity = new HttpEntity<>(login);

        final ResponseEntity<String> response = restTemplate.exchange(loginPath,
                    HttpMethod.POST,
                    entity,
                    String.class);

        LOGGER.debug("Login was success. New session was generated.");

        return response;
    }

    public Purchase createPurchase(final Purchase ticket) {
        LOGGER.debug("A new purchase will be created in SAP: {}.", ticket);

        final HttpHeaders headers = login().getHeaders();

        final MultiValueMap<String, String> sessionHeader = createSessionHeader(headers);

        final HttpEntity entity = new HttpEntity(ticket, sessionHeader);

        final ResponseEntity<Purchase> response = decorated.apply(entity);

        LOGGER.info("A new purchase was created in SAP. Purchase = [{}].", response);

        return response.getBody();
    }

    private ResponseEntity<Purchase> executeWithRetry(final HttpEntity entity) {
        final ResponseEntity<Purchase> response =
                restTemplate.exchange(purchaseRequestPath, HttpMethod.POST, entity, Purchase.class);

        return response;
    }

    /**
     * Create the Cookie header for next SAP requests.
     *
     * @param headers   the {@link HttpHeaders} response after a Login with SAP.
     * @return          a Cookie header.
     */
    private MultiValueMap<String, String> createSessionHeader(final HttpHeaders headers) {
        final List<String> sessionInfo = headers.get(HttpHeaders.SET_COOKIE);
        final String sessionId = sessionInfo.stream()
                .map(s -> s.split(SESSION_DELIMITER)[0])
                .filter(s -> s.contains(SESSION_ATTRIBUTE) || s.contains(ROUTE_ATTRIBUTE))
                .collect(Collectors.joining(SESSION_DELIMITER));

        final MultiValueMap<String, String> cookieHeader = new HttpHeaders() {{
            add(HttpHeaders.COOKIE, sessionId);
        }};

        LOGGER.debug("The Cookie header was created.");

        return cookieHeader;
    }

    /**
     * Workaround used to avoid certificates issues with SAP.
     */
    private static void disableSslVerification() {
        try{
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
