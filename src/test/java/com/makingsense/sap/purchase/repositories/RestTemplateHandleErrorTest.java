package com.makingsense.sap.purchase.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makingsense.sap.purchase.errors.InvalidCredentialsException;
import com.makingsense.sap.purchase.errors.SAPBadRequestException;
import com.makingsense.sap.purchase.errors.SAPInternalServerException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link RestTemplateHandlerError}.
 */
public class RestTemplateHandleErrorTest {

    private RestTemplateHandlerError target;

    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = mock(ObjectMapper.class);

        target = new RestTemplateHandlerError(mapper);
    }

    @Test
    public void shouldThrowInternalErrorWhenRestTemplateAnswersInternalServerError() throws IOException {
        // Arrange
        final ClientHttpResponse error = mock(ClientHttpResponse.class);
        when(error.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        final InputStream body = mock(InputStream.class);
        when(error.getBody()).thenReturn(body);

        when(mapper.readValue(any(InputStream.class), ArgumentMatchers.<Class<Map>>any()))
                .thenReturn(Collections.EMPTY_MAP);

        // Act - Assert
        assertThrows(SAPInternalServerException.class,
                () -> target.handleError(error),
                "When an internal server error is returned a SAPInternalServerException should be " +
                        "thrown by the application.");
    }

    @Test
    public void shouldThrowInvalidCredentialsErrorWhenRestTemplateAnswersUnauthorized() throws IOException {
        // Arrange
        final ClientHttpResponse error = mock(ClientHttpResponse.class);
        when(error.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED);
        final InputStream body = mock(InputStream.class);
        when(error.getBody()).thenReturn(body);

        when(mapper.readValue(any(InputStream.class), ArgumentMatchers.<Class<Map>>any()))
                .thenReturn(Collections.EMPTY_MAP);

        // Act - Assert
        assertThrows(InvalidCredentialsException.class,
                () -> target.handleError(error),
                "When an unauthorized request is returned an InvalidCredentialsException should be " +
                        "thrown by the application.");
    }

    @Test
    public void shouldThrowBadRequestErrorWhenRestTemplateAnswersBadRequest() throws IOException {
        // Arrange
        final ClientHttpResponse error = mock(ClientHttpResponse.class);
        when(error.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        final InputStream body = mock(InputStream.class);
        when(error.getBody()).thenReturn(body);

        when(mapper.readValue(any(InputStream.class), ArgumentMatchers.<Class<Map>>any()))
                .thenReturn(Collections.EMPTY_MAP);

        // Act - Assert
        assertThrows(SAPBadRequestException.class,
                () -> target.handleError(error),
                "When a Bad request is returned a SAPBadRequestException should be " +
                        "thrown by the application.");
    }

}
