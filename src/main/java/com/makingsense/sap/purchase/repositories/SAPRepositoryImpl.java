package com.makingsense.sap.purchase.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makingsense.sap.purchase.data.source.SAPSourceFactory;
import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.data.source.SAPSource;

import io.github.resilience4j.retry.Retry;

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
 * Implementation of {@link SAPRepository} that interacts with SAP via REST APIs.
 */
@Component
public class SAPRepositoryImpl implements SAPRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SAPRepositoryImpl.class);

    private static final String SESSION_ATTRIBUTE = "B1SESSION";

    private static final String ROUTE_ATTRIBUTE = "ROUTEID";

    private static final String SESSION_DELIMITER = ";";

    private final RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Value("${sap.purchase.login.path:Login}")
    private String loginPath;

    @Value("${sap.purchase.request.path:PurchaseRequests}")
    private String purchaseRequestPath;

    static {
        disableSslVerification();
    }

    private Function<HttpEntity, ResponseEntity> decorated;

    @Autowired
    public SAPRepositoryImpl(final RestTemplate restTemplate,
                             final Retry sapRetry) {
        this.restTemplate = restTemplate;

        decorated = Retry.decorateFunction(sapRetry, (HttpEntity entity) -> this.executeWithRetry(entity));
    }

    /**
     * Creates a session with SAP server.
     *
     * @return  a {@link ResponseEntity} that contains the session information to be used later on.
     */

    private ResponseEntity<String> login(final SAPSource source) {
        LOGGER.info("SAPSource to be used: [{}].", source);
        final HttpEntity<SAPSource> entity = new HttpEntity<>(source);

        final ResponseEntity<String> response = restTemplate.exchange(loginPath,
                    HttpMethod.POST,
                    entity,
                    String.class);

        LOGGER.debug("Login was success. New session was generated.");

        return response;
    }

    public Purchase createPurchase(final Purchase ticket, final SAPSource source) {
        LOGGER.debug("A new purchase will be created in SAP: {}.", ticket);

        final HttpHeaders headers = login(source).getHeaders();

        final MultiValueMap<String, String> sessionHeader = createSessionHeader(headers);

        final HttpEntity entity = new HttpEntity(ticket, sessionHeader);

        try {
            mapper.writerWithDefaultPrettyPrinter();
            LOGGER.info("To send = {}", mapper.writeValueAsString(ticket));
        } catch (final Exception ex) {
            LOGGER.error("ERROR");
        }

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
