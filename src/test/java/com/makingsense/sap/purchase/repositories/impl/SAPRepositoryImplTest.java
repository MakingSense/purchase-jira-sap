package com.makingsense.sap.purchase.repositories.impl;

import com.makingsense.sap.purchase.data.source.SAPSource;
import com.makingsense.sap.purchase.data.source.SAPSourceFactory;
import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.repositories.SAPRepositoryImpl;
import io.github.resilience4j.retry.Retry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link SAPRepositoryImpl}.
 */
public class SAPRepositoryImplTest {

    private SAPRepositoryImpl target;

    private RestTemplate restTemplate;

    private Retry retry;

    @BeforeEach
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        //retry = mock(Retry.class);
        retry = Retry.ofDefaults("id");

        target = new SAPRepositoryImpl(restTemplate, retry);

        ReflectionTestUtils.setField(target, "loginPath", "Login");
        ReflectionTestUtils.setField(target, "purchaseRequestPath", "PurchaseRequests");
    }

    @Test
    public void shouldCreateNewPurchaseInSAP() {
        // Arrange
        final Purchase purchase = mock(Purchase.class);
        final String company = "testCompany";
        when(purchase.getCompany()).thenReturn(company);

        final SAPSource sapSource = mock(SAPSource.class);
        final HttpEntity<SAPSource> entity = new HttpEntity<>(sapSource);

        final ResponseEntity<String> response = mock(ResponseEntity.class);
        when(response.getHeaders()).thenReturn(createSessionHeader());
        when(restTemplate.exchange(
                ArgumentMatchers.eq("Login"),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
                .thenReturn(response);

        final ResponseEntity purchaseResponse = mock(ResponseEntity.class);
        when(restTemplate.exchange(
                ArgumentMatchers.eq("PurchaseRequests"),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
                .thenReturn(purchaseResponse);

        final Purchase expected = mock(Purchase.class);
        when(purchaseResponse.getBody()).thenReturn(expected);

        // Act
        final Purchase actual = target.createPurchase(purchase, sapSource);

        // Assert
        assertThat(actual, is(expected));
    }

    private HttpHeaders createSessionHeader() {
        final List<String> headersValue = new ArrayList() {{
            add("B1SESSION=testsession;http");
            add("ROUTEID=Route.2;http");
        }};

        final HttpHeaders headers = new HttpHeaders();
        headers.addAll(HttpHeaders.SET_COOKIE, headersValue);

        return headers;
    }
}
