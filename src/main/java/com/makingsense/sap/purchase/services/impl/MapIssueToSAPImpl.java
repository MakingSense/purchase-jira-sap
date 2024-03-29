package com.makingsense.sap.purchase.services.impl;

import com.google.common.base.Strings;
import com.makingsense.sap.purchase.models.DocumentLines;
import com.makingsense.sap.purchase.models.JiraPurchaseItem;
import com.makingsense.sap.purchase.models.JiraPurchaseTicket;
import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.services.CurrencyFactory;
import com.makingsense.sap.purchase.services.MapIssueToSAP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MapIssueToSAPImpl implements MapIssueToSAP {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapIssueToSAPImpl.class);

    private final CurrencyFactory currencyFactory;

    @Value("${sap.purchase.description.max.length}")
    private int maxDescriptionLength;

    @Autowired
    public MapIssueToSAPImpl(final CurrencyFactory currencyFactory) {
        this.currencyFactory = currencyFactory;
    }

    /**
     * Creates a new {@link Purchase} based on a {@link JiraPurchaseTicket}.
     *
     * @param ticket    the {@link JiraPurchaseTicket} information
     * @return          a {@link Purchase}
     */
    public Purchase mapToPurchase(final JiraPurchaseTicket ticket, final String company) {
        LOGGER.debug("About to map ticket: [{}].", ticket);
        final String description = Strings.isNullOrEmpty(ticket.getDescription()) ? "" :
                ticket.getDescription().substring(0, Math.min(ticket.getDescription().length(), maxDescriptionLength));

        final List<DocumentLines> documents = new ArrayList<>();
        for (int i = 0; i < ticket.getItems().size(); i++) {

            final JiraPurchaseItem jiraPurchaseItem = ticket.getItems().get(i);
            LOGGER.debug("Item [{}] of the request: [{}].", i + 1, jiraPurchaseItem);
            final DocumentLines sapDocumentLine = new DocumentLines.DocumentLinesBuilder()
                    .setLineNum(i)
                    .setItemCode(getMappingValue(jiraPurchaseItem.getItemCode(), 1))
                    .setItemDescription(description)
                    .setQuantity(jiraPurchaseItem.getQuantity())
                    .setPrice(jiraPurchaseItem.getUnitPrice())
                    .setBusinessUnit(getMappingValue(ticket.getBusinessUnit(), 0))
                    .setDepartment(getMappingValue(ticket.getDepartment(), 0))
                    .setLocation(getMappingValue(jiraPurchaseItem.getLocation(), 0))
                    .setProject(getMappingValue(ticket.getProject(), 0))
                    .setCurrency(currencyFactory.getCurrency(company, ticket.getCurrency(), true))
                    .build();

            documents.add(sapDocumentLine);
        }

        final Purchase purchase = new Purchase.PurchaseBuilder()
                .withDocument(documents)
                .setRequriedDate(ticket.getDateOfPayment())
                .setDocDate(LocalDate.now())
                .setJiraTicketId(ticket.getTicketId())
                .setCreatorEmail(ticket.getEmail())
                .setCreatorName(ticket.getCreatorDisplayName())
                .setCompany(ticket.getCompany())
                .setCurrency(currencyFactory.getCurrency(company, ticket.getCurrency(), false))
                .build();

        LOGGER.debug("Jira ticket mapped to SAP Purchase: [{}].", purchase);

        return purchase;
    }

    private Optional<String> getMappingValue(final String input, final int index) {
        Optional response = Optional.empty();

        if (!Strings.isNullOrEmpty(input)) {
            response = Optional.of(input.split("/")[index].trim());
        }

        return response;
    }
}
