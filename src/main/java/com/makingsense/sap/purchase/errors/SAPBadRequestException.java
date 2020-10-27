package com.makingsense.sap.purchase.errors;

/**
 * Exception that represents a Bad Request when trying to access SAP services.
 */
public class SAPBadRequestException extends RuntimeException {

    public SAPBadRequestException(final String message) {
        super(message);
    }
}
