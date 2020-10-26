package com.makingsense.sap.purchase.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * Entity that represents a {@link Purchase} on SAP.
 */
public class Purchase {

    @JsonProperty("DocEntry")
    private int docEntry;

    @JsonProperty("DocumentLines")
    private List<DocumentLines> documentLines;

    @JsonProperty("RequriedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date requriedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("DocDate")
    private Date docDate;

    @JsonProperty("U_ID_JIRA")
    private String jiraId;

    @JsonProperty("U_Creator")
    private String creatorEmail;

    /**
     * Default constructor used by jackson.
     *
     * @param documentLines     the {@link DocumentLines} of the purchase
     * @param requiredDate      date which the purchase took place
     * @param docDate           date of migration
     * @param jiraId            the id of the Jira ticket
     * @param creatorEmail      the email that created the Jira ticket
     */
    public Purchase(final List<DocumentLines> documentLines,
                    final Date requiredDate,
                    final Date docDate,
                    final String jiraId,
                    final String creatorEmail,
                    final int docEntry) {
        this.documentLines = documentLines;
        this.requriedDate = requiredDate;
        this.docDate = docDate;
        this.jiraId = jiraId;
        this.creatorEmail = creatorEmail;
        this.docEntry = docEntry;
    }

    /**
     * Creates a new instance of {@link Purchase}.
     *
     * @param builder   the {@link PurchaseBuilder}.
     */
    private Purchase(final PurchaseBuilder builder) {
        this.documentLines = builder.documentLines;
        this.requriedDate = builder.requriedDate;
        this.docDate = builder.docDate;
        this.jiraId = builder.jiraId;
        this.creatorEmail = builder.creatorEmail;
    }

    public int getDocEntry() {
        return docEntry;
    }

    @Override
    public String toString() {
        return "Purchase {" +
                "docEntry=" + docEntry +
                ", documentLines=" + documentLines +
                ", requriedDate=" + requriedDate +
                ", docDate=" + docDate +
                ", jiraId='" + jiraId + '\'' +
                ", creatorEmail='" + creatorEmail + '\'' +
                '}';
    }

    /**
     * Builder class to create new {@link Purchase} objects in a fluent manner.
     */
    public static class PurchaseBuilder {

        private List<DocumentLines> documentLines;

        private Date requriedDate;

        private Date docDate;

        private String jiraId;

        private String creatorEmail;

        public PurchaseBuilder withDocument(final List<DocumentLines> documentLines) {
            this.documentLines = documentLines;
            return this;
        }

        public PurchaseBuilder setRequriedDate(final Date requriedDate) {
            this.requriedDate = requriedDate;
            return this;
        }

        public PurchaseBuilder setDocDate(final Date docDate) {
            this.docDate = docDate;
            return this;
        }

        public PurchaseBuilder setJiraTicketId(final String jiraId) {
            this.jiraId = jiraId;
            return this;
        }

        public PurchaseBuilder setCreatorEmail(final String email) {
            this.creatorEmail = email;
            return this;
        }

        public Purchase build() {
            return new Purchase(this);
        }
    }
}
