package com.makingsense.sap.purchase.repositories;

import com.makingsense.sap.purchase.data.source.SAPSource;
import com.makingsense.sap.purchase.models.Purchase;

/**
 * Repository that defines the interfaces to communicate with SAP.
 */
public interface SAPRepository {

    /**
     * Creates a new purchase in SAP.
     *
     * @param ticket    the ticket information to be created.
     * @return  the created {@link Purchase}.
     */
    Purchase createPurchase(final Purchase ticket, final SAPSource sapSource);
}
