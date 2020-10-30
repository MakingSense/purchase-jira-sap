package com.makingsense.sap.purchase.data.source;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Entity that represents the information needed to communicate with SAP, such as the DB to be used,
 * the user and the password.
 */
public class SAPSource {

    @JsonProperty("CompanyDB")
    private String db;

    @JsonProperty("UserName")
    private String user;

    @JsonProperty("Password")
    private String password;

    public String getDb() {
        return db;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setDb(final String db) {
        this.db = db;
    }

    public void setUser(final String userName) {
        this.user = userName;
    }

    public void setPassword(final String password) {
        this.password = password;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other != null && other.getClass() == this.getClass()) {
            final SAPSource sapSource = (SAPSource) other;
            return Objects.equals(db, sapSource.db) &&
                    Objects.equals(user, sapSource.user) &&
                    Objects.equals(password, sapSource.password);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(db, user, password);
    }
}
