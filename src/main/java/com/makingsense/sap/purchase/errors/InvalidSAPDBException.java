package com.makingsense.sap.purchase.errors;

/**
 * Exception that represents that the DB selected in SAP does not exists.
 */
public class InvalidSAPDBException extends RuntimeException {

    public InvalidSAPDBException(final String message) {
        super(message);
    }
}
