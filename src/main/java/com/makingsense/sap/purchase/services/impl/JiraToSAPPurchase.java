package com.makingsense.sap.purchase.services.impl;

import com.makingsense.sap.purchase.data.source.SAPSource;
import com.makingsense.sap.purchase.data.source.SAPSourceFactory;
import com.makingsense.sap.purchase.models.JiraPurchaseTicket;
import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.repositories.SAPRepository;
import com.makingsense.sap.purchase.services.MapIssueToSAP;
import com.makingsense.sap.purchase.services.MigrateToSAP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The main responsibility is to communicate with SAP service to create a new purchase
 * based on a {@link JiraPurchaseTicket}.
 */
@Component
public class JiraToSAPPurchase implements MigrateToSAP<Purchase, JiraPurchaseTicket> {

    private final SAPRepository sapRepositoryImpl;

    private final MapIssueToSAP mapIssueToSAP;

    private final SAPSourceFactory sapSourceFactory;

    @Autowired
    public JiraToSAPPurchase(final SAPRepository sapRepositoryImpl,
                             final MapIssueToSAP mapIssueToSAP,
                             final SAPSourceFactory sapSourceFactory) {
        this.sapRepositoryImpl = sapRepositoryImpl;
        this.mapIssueToSAP = mapIssueToSAP;
        this.sapSourceFactory = sapSourceFactory;
    }

    /**
     * Create a new {@link Purchase} on SAP based on a {@link JiraPurchaseTicket}.
     *
     * @param ticket    the {@link JiraPurchaseTicket} information.
     * @return          the {@link Purchase} information created in SAP.
     */
    @Override
    public Purchase migrate(final JiraPurchaseTicket ticket) {
        final SAPSource source = sapSourceFactory.getSource(ticket.getCompany());

        final Purchase purchase = mapIssueToSAP.mapToPurchase(ticket, source.getDb());

        return sapRepositoryImpl.createPurchase(purchase, source);
    }

}