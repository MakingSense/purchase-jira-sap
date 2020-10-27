package com.makingsense.sap.purchase.errors;

/**
 * Exception that represents that there are invalid credentials set in the application.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(final String message) {
        super(message);
    }
}
