package com.makingsense.sap.purchase.services.impl;

import com.makingsense.sap.purchase.models.DocumentLines;
import com.makingsense.sap.purchase.models.JiraPurchaseTicket;
import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.repositories.SAPRepository;
import com.makingsense.sap.purchase.services.MigrateToSAP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * The main responsibility is to communicate with SAP service to create a new purchase
 * based on a {@link JiraPurchaseTicket}.
 */
@Component
public class JiraToSAPPurchase implements MigrateToSAP<Purchase, JiraPurchaseTicket> {

    private final static Logger LOGGER = LoggerFactory.getLogger(JiraPurchaseTicket.class);

    private final SAPRepository sapRepository;

    @Value("#{${sap.purchase.mapping.business.unit}}")
    private Map<String, String> businessUnitMap;

    @Value("#{${sap.purchase.mapping.deparment}}")
    private Map<String, String> departmentMap;

    @Value("#{${sap.purchase.mapping.location}}")
    private Map<String, String> locationMap;

    @Value("#{${sap.purchase.mapping.project}}")
    private Map<String, String> projectMap;

    @Value("${sap.purchase.description.max.length}")
    private int maxDescriptionLength;

    @Value("${sap.purchase.item.code:99999}")
    private String itemCode;

    @Autowired
    public JiraToSAPPurchase(final SAPRepository sapRepository) {
        this.sapRepository = sapRepository;
    }

    /**
     * Create a new {@link Purchase} on SAP based on a {@link JiraPurchaseTicket}.
     *
     * @param ticket    the {@link JiraPurchaseTicket} information.
     * @return          the {@link Purchase} information created in SAP.
     */
    @Override
    public Purchase migrate(final JiraPurchaseTicket ticket) {
        final Purchase purchase = createPurchase(ticket);

        return sapRepository.createPurchase(purchase);
    }

    /**
     * Creates a new {@link Purchase} based on a {@link JiraPurchaseTicket}.
     *
     * @param ticket    the {@link JiraPurchaseTicket} information
     * @return          a {@link Purchase}
     */
    private Purchase createPurchase(final JiraPurchaseTicket ticket) {
        LOGGER.debug("Migrating Jira information to SAP: {}.", ticket);

        final String costingCode = businessUnitMap.get(ticket.getBusinessUnit());
        final String costingCode2 = departmentMap.get(ticket.getDepartment());
        // TODO: Get the value from Jira, not in placed yet.
        final String costingCode3 = "Arg";
        final String costingCode4 = projectMap.get(ticket.getProject());

        final String description = ticket.getDescription()
                .substring(0, Math.min(ticket.getDescription().length(), maxDescriptionLength));

        final DocumentLines document = new DocumentLines.DocumentLinesBuilder()
                .setLineNum(0)
                .setItemCode(itemCode)
                .setItemDescription(description)
                .setQuantity(ticket.getQuantity())
                .setBusinessUnit(costingCode)
                .setDeparment(costingCode2)
                .setLocation(costingCode3)
                .setProject(costingCode4)
                .build();

        final Purchase purchase = new Purchase.PurchaseBuilder()
                .withDocument(Collections.singletonList(document))
                .setRequriedDate(ticket.getDateOfPayment())
                .setDocDate(new Date())
                .setJiraTicketId(ticket.getTicketId())
                .setCreatorEmail(ticket.getCreator())
                .setCreatorName(ticket.getCreatorDisplayName())
                .build();

        LOGGER.debug("The purchase to save is: [{}].", purchase);

        return purchase;
    }
}