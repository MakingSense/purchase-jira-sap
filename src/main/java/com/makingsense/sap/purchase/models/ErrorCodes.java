package com.makingsense.sap.purchase.models;

/**
 * Error codes defined, to better understanding what kind of error is happening if there is a failure
 * in the application.
 */
public enum ErrorCodes {

    // 4xx Family error codes
    GENERAL_BAD_REQUEST(40001, "The request is malformed. Please check fields values."),
    ARGUMENT_VALIDATION_BAD_REQUEST(40002, "The request is malformed. Please check fields values."),

    // 5xx Family error codes
    INTERNAL_SERVER_ERROR(50001, "The application run into un unknown problem. Please contact support."),
    SAP_UNAUTHORIZED_REQUEST(50002, "The application was not authorized to communicate with SAP."),
    SAP_BAD_REQUEST_REQUEST(50003, "The application request is malformed."),
    SAP_INVALID_DB(50004, "The company name is invalid.");

    private final int code;

    private final String message;

    ErrorCodes(final int code, final String message) {
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
