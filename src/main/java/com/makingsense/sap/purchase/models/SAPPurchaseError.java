package com.makingsense.sap.purchase.models;

/**
 * Model that represents the general error schema of the APIs.
 */
public class SAPPurchaseError {

    private String errorMessage;

    private int errorCode;

    public SAPPurchaseError(final ErrorCodes error) {
        this.errorMessage = error.getMessage();
        this.errorCode = error.getCode();
    }

    public SAPPurchaseError(final ErrorCodes error, final String message) {
        this.errorCode = error.getCode();
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
