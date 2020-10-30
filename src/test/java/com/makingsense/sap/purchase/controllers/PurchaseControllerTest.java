package com.makingsense.sap.purchase.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makingsense.sap.purchase.models.JiraPurchaseTicket;
import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.services.MigrateToSAP;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link PurchaseController}.
 */
public class PurchaseControllerTest {

    private PurchaseController target;

    private MigrateToSAP<Purchase, JiraPurchaseTicket> service;

    private ObjectMapper mapper;


    @BeforeEach
    public void setUp() {
        service = mock(MigrateToSAP.class);
        mapper = mock(ObjectMapper.class);
        when(mapper.copy()).thenReturn(mapper);

        target = new PurchaseController(service, mapper);
    }

    @Test
    public void shouldMigrateTicket() throws Exception {
        // Arrange
        final JiraPurchaseTicket ticket = mock(JiraPurchaseTicket.class);
        when(mapper.writeValueAsString(any())).thenReturn("{\"validJson\": true }");

        // Act
        final ResponseEntity responseEntity = target.migrateJiraTicket(ticket);

        // Assert
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));
        verify(mapper).writeValueAsString(any());
    }

    @Test
    public void shouldThrowExceptionIfObjectIsNotSerialized() throws Exception {
        // Arrange
        final JiraPurchaseTicket ticket = mock(JiraPurchaseTicket.class);
        final JsonProcessingException ex = mock(JsonProcessingException.class);
        when(mapper.writeValueAsString(any())).thenThrow(ex);

        // Act
        assertThrows(JsonProcessingException.class,
                () -> target.migrateJiraTicket(ticket),
                "The test should have thrown an exception.");

        // Assert
        verify(mapper).writeValueAsString(any());
    }
}
