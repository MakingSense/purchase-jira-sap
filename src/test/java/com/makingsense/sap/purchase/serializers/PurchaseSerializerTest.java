package com.makingsense.sap.purchase.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.makingsense.sap.purchase.models.Purchase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link PurchaseSerializer}.
 */
public class PurchaseSerializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        final SimpleModule module = new SimpleModule();
        module.addSerializer(Purchase.class, new PurchaseSerializer());

        mapper = new ObjectMapper();
        mapper.registerModule(module);
    }

    @Test
    public void shouldMapCorrectly() throws Exception {
        // Arrange
        final Purchase purchase = mock(Purchase.class);
        when(purchase.getDocEntry()).thenReturn(1);
        when(purchase.getJiraId()).thenReturn("FI-11");

        // Act
        final String actual = mapper.writeValueAsString(purchase);

        // Assert
        assertThat(actual, is("{\"docEntry\":1,\"jiraId\":\"FI-11\"}"));
    }

}
