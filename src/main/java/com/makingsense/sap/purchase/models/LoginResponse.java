package com.makingsense.sap.purchase.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

    @JsonProperty(value = "odata.metadata")
    private final String oData;

    @JsonProperty(value = "SessionId")
    private final String sessionId;

    @JsonProperty(value = "Version")
    private final String version;

    @JsonProperty(value = "SessionTimeout")
    private final int sessionTimeout;

    /**
     * Creates a new {@link LoginResponse}
     *
     */
    public LoginResponse(final String oData, final String sessionId, final String version, final int sessionTimeout) {
        this.oData = oData;
        this.sessionId = sessionId;
        this.version = version;
        this.sessionTimeout = sessionTimeout;
    }

}
