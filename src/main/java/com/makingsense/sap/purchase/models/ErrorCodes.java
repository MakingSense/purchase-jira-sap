package com.makingsense.sap.purchase.models;

/**
 *
 */
public enum ErrorCodes {

    // 4xx Family error codes
    BAD_REQUEST(40001, "The request is malformed."),


    // 5xx Family error codes
    INTERNAL_SERVER_ERROR(50001, "The application run into un unknown problem. Please contact support.");

    private final int code;

    private final String message;

    private ErrorCodes(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
