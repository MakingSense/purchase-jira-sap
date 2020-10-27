package com.makingsense.sap.purchase.errors;

/**
 * Exception that represents that an internal server error occured while communicating with SAP services.
 */
public class SAPInternalServerException extends RuntimeException {

    public SAPInternalServerException(final String message) {
        super(message);
    }
}
