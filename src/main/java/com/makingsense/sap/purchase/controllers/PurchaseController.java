package com.makingsense.sap.purchase.controllers;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.makingsense.sap.purchase.models.JiraPurchaseTicket;
import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.serializers.PurchaseSerializer;
import com.makingsense.sap.purchase.services.MigrateToSAP;
import com.makingsense.sap.purchase.services.impl.JiraToSAPPurchase;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API that expose a resource to handle a JIRA ticket.
 */
@RestController()
@RequestMapping(path = "/purchase")
public class PurchaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseController.class);

    private final MigrateToSAP<Purchase, JiraPurchaseTicket> jiraToSAPPurchase;

    private final ObjectMapper mapper;

    @Autowired
    public PurchaseController(final MigrateToSAP jiraToSAPPurchase, final ObjectMapper mapper) {
        this.jiraToSAPPurchase = jiraToSAPPurchase;

        this.mapper = mapper.copy();
        final SimpleModule module = new SimpleModule();
        module.addSerializer(Purchase.class, new PurchaseSerializer());
        mapper.configure(
                JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(),
                true
        );

        this.mapper.registerModule(module);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity migrateJiraTicket(@Valid @RequestBody final JiraPurchaseTicket ticket) throws Exception {
        LOGGER.info("A new Jira ticket needs to be migrated: {}.", ticket);

        final Purchase createdPurchase = jiraToSAPPurchase.migrate(ticket);

        final String response = mapper.writeValueAsString(createdPurchase);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

}
