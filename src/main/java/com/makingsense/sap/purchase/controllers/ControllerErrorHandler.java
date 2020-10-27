package com.makingsense.sap.purchase.controllers;

import com.makingsense.sap.purchase.errors.InvalidCredentialsException;
import com.makingsense.sap.purchase.errors.SAPBadRequestException;
import com.makingsense.sap.purchase.models.ErrorCodes;
import com.makingsense.sap.purchase.models.SAPPurchaseError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * General exception handler of the application. The main goal apart from handling the
 * exception is to create a known model for errors, that will help to understand the
 * root cause of any specific error in the application.
 */
@ControllerAdvice
public class ControllerErrorHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ControllerErrorHandler.class);

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<SAPPurchaseError> handleUnauthorizedException(final InvalidCredentialsException ex) {
        LOGGER.error("There was a problem when generating the session with SAP. Exception = [{}].", ex);

        final SAPPurchaseError error = new SAPPurchaseError(ErrorCodes.SAP_UNAUTHORIZED_REQUEST);

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SAPBadRequestException.class)
    public ResponseEntity<SAPPurchaseError> handleBadRequests(final SAPBadRequestException ex) {
        LOGGER.error("There was a problem in the request made to SAP. Exception = [{}].", ex);

        final SAPPurchaseError error = new SAPPurchaseError(ErrorCodes.SAP_BAD_REQUEST_REQUEST);

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SAPPurchaseError> handleBadRequestsException(final MethodArgumentNotValidException ex) {

        final String firstErrorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .get()
                .getDefaultMessage();

        LOGGER.error("There is a problem in the request. Exception = [{}]", ex.getMessage());

        final SAPPurchaseError error = new SAPPurchaseError(ErrorCodes.BAD_REQUEST, firstErrorMessage);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<SAPPurchaseError> handleGeneralException(final Exception ex) {
        LOGGER.error("An error occurred while processing the request. Exception = [{}]", ex.getMessage());

        final SAPPurchaseError smError = new SAPPurchaseError(ErrorCodes.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(smError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
