package com.makingsense.sap.purchase.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other != null && this.getClass() == other.getClass()) {
            final SAPPurchaseError that = (SAPPurchaseError) other;
            return errorCode == that.errorCode &&
                    Objects.equals(errorMessage, that.errorMessage);
        }

        return false;

    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, errorMessage);
    }
}
