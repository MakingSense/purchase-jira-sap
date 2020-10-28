package com.makingsense.sap.purchase.data.source;

/**
 * Defines an interface to get the correct {@link SAPSource} object to connect to SAP based
 * on the company input.
 */
public interface SAPSourceFactory {

    /**
     * Obtains the {@link SAPSource} to communicate with SAP.
     *
     * @param company   the company name to connect to SAP.
     * @return  a new instance of {@link SAPSource}.
     */
    SAPSource getSource(final String company);
}
