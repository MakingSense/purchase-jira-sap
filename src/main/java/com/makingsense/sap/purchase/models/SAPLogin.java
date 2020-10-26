package com.makingsense.sap.purchase.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity to represent the information connection to SAP.
 */
public class SAPLogin {

    @JsonProperty("CompanyDB")
    private String companyDB;

    @JsonProperty("UserName")
    private String userName;

    @JsonProperty("Password")
    private String password;

    public SAPLogin(final String companyDB,
                    final String userName,
                    final String password) {
        this.companyDB = companyDB;
        this.userName = userName;
        this.password = password;
    }

}
