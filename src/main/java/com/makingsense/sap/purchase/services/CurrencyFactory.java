package com.makingsense.sap.purchase.services;

import java.util.Optional;

/**
 * Currency service that determines the representation to map to SAP.
 */
public interface CurrencyFactory {

    /**
     * Gets the currency needed for the company selected.
     *
     * @param company   the company selected
     * @return  the company currency to use.
     */
    String getCurrency(String company, String currency, boolean isInLine);
}
