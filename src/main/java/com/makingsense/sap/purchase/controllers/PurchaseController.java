package com.makingsense.sap.purchase.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makingsense.sap.purchase.models.JiraPurchaseTicket;
import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.services.MigrateToSAP;
import com.makingsense.sap.purchase.services.impl.JiraToSAPPurchase;
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

import javax.validation.Valid;

/**
 * REST API that expose a resource to handle a JIRA ticket.
 */
@RestController()
@RequestMapping(path = "/purchase")
public class PurchaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseController.class);

    private final MigrateToSAP<Purchase,JiraPurchaseTicket> migrationService;

    private final ObjectMapper mapper;

    @Autowired
    public PurchaseController(final JiraToSAPPurchase service, final ObjectMapper mapper) {
        this.migrationService = service;
        this.mapper = mapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity migrateJiraTicket(@Valid @RequestBody final JiraPurchaseTicket ticket) {
        LOGGER.info("A new Jira ticket needs to be migrated: {}.", ticket);
        final Purchase createdPurchase = migrationService.migrate(ticket);

        return new ResponseEntity(createdPurchase, HttpStatus.CREATED);
    }


}
