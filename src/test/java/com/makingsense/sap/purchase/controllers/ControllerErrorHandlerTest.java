package com.makingsense.sap.purchase.controllers;

import com.makingsense.sap.purchase.errors.InvalidCredentialsException;
import com.makingsense.sap.purchase.errors.InvalidSAPDBException;
import com.makingsense.sap.purchase.errors.SAPBadRequestException;
import com.makingsense.sap.purchase.models.ErrorCodes;
import com.makingsense.sap.purchase.models.SAPPurchaseError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ControllerErrorHandler}.
 */
public class ControllerErrorHandlerTest {

    private ControllerErrorHandler target;

    @BeforeEach
    public void setUp() {
        target = new ControllerErrorHandler();
    }

    @Test
    public void shouldReturnSAPUnauthorizedCode() {
        // Arrange
        final InvalidCredentialsException ex = new InvalidCredentialsException("Invalid Credentials");
        final SAPPurchaseError expected = new SAPPurchaseError(ErrorCodes.SAP_UNAUTHORIZED_REQUEST);

        // Act
        final ResponseEntity actual = target.handleUnauthorizedException(ex);

        // Assert
        assertThat(actual.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(actual.getBody(), is(expected));
    }

    @Test
    public void shouldReturnSAPBadRequestCode() {
        // Arrange
        final SAPBadRequestException ex = new SAPBadRequestException("Bad Request");
        final SAPPurchaseError expected = new SAPPurchaseError(ErrorCodes.SAP_BAD_REQUEST_REQUEST);

        // Act
        final ResponseEntity actual = target.handleBadRequests(ex);

        // Assert
        assertThat(actual.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(actual.getBody(), is(expected));
    }

    @Test
    public void shouldReturnBadRequestCode() {
        // Arrange
        final MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        final String errorMessage = "Mandatory fields not present.";
        final BindingResult bindingResult = mock(BindingResult.class);
        final ObjectError error = new ObjectError("errorName", errorMessage);

        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(error));
        when(ex.getBindingResult()).thenReturn(bindingResult);

        final SAPPurchaseError expected = new SAPPurchaseError(ErrorCodes.ARGUMENT_VALIDATION_BAD_REQUEST,
                errorMessage);

        // Act
        final ResponseEntity actual = target.handleBadRequestsException(ex);

        // Assert
        assertThat(actual.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(actual.getBody(), is(expected));
    }

    @Test
    public void shouldReturnFirstMessageBadRequestCode() {
        // Arrange
        final MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        final String errorMessage = "Mandatory fields not present.";
        final BindingResult bindingResult = mock(BindingResult.class);
        final ObjectError error = new ObjectError("errorName", errorMessage);
        final ObjectError secondError = new ObjectError("secondErrorName", "Second error message");

        final List<ObjectError> errors = new ArrayList() {{
           add(error);
           add(secondError);
        }};

        when(bindingResult.getAllErrors()).thenReturn(errors);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        final SAPPurchaseError expected = new SAPPurchaseError(ErrorCodes.ARGUMENT_VALIDATION_BAD_REQUEST,
                errorMessage);

        // Act
        final ResponseEntity actual = target.handleBadRequestsException(ex);

        // Assert
        assertThat(actual.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(actual.getBody(), is(expected));
    }

    @Test
    public void shouldReturnInvalidSAPDBCode() {
        // Arrange
        final InvalidSAPDBException ex = new InvalidSAPDBException("InvalidDB");
        final SAPPurchaseError expected = new SAPPurchaseError(ErrorCodes.SAP_INVALID_DB);

        // Act
        final ResponseEntity actual = target.handleInvalidDBException(ex);

        // Assert
        assertThat(actual.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(actual.getBody(), is(expected));
    }

    @Test
    public void shouldReturnGeneralBadRequest() {
        // Arrange
        final HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        final SAPPurchaseError expected = new SAPPurchaseError(ErrorCodes.GENERAL_BAD_REQUEST);

        // Act
        final ResponseEntity actual = target.handleNotReadableException(ex);

        // Assert
        assertThat(actual.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(actual.getBody(), is(expected));
    }

    @Test
    public void shouldReturnGenericInternalErrorCode() {
        // Arrange
        final IOException ex = new IOException("IO Exception");
        final SAPPurchaseError expected = new SAPPurchaseError(ErrorCodes.INTERNAL_SERVER_ERROR);

        // Act
        final ResponseEntity actual = target.handleGeneralException(ex);

        // Assert
        assertThat(actual.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(actual.getBody(), is(expected));
    }
}
