package com.makingsense.sap.purchase.services;

import com.makingsense.sap.purchase.models.JiraPurchaseTicket;
import com.makingsense.sap.purchase.models.Purchase;

public interface MapIssueToSAP {

    Purchase mapToPurchase(final JiraPurchaseTicket ticket);
}
