package com.makingsense.sap.purchase.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Entity that represents a {@link Purchase} on SAP.
 */
public class Purchase {

    @JsonProperty("DocEntry")
    private int docEntry;

    @JsonProperty("DocumentLines")
    private List<DocumentLines> documentLines;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("RequriedDate")
    private LocalDate requriedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("DocDate")
    private LocalDate docDate;

    @JsonProperty("U_ID_JIRA")
    private String jiraId;

    @JsonProperty("U_Creator")
    private String creatorEmail;

    @JsonProperty("U_CreatorName")
    private String creatorDisplayName;

    @JsonIgnore
    private String company;

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
                    final LocalDate requiredDate,
                    final LocalDate docDate,
                    final String jiraId,
                    final String creatorEmail,
                    final String creatorDisplayName,
                    final int docEntry) {
        this.documentLines = documentLines;
        this.requriedDate = requiredDate;
        this.docDate = docDate;
        this.jiraId = jiraId;
        this.creatorEmail = creatorEmail;
        this.creatorDisplayName = creatorDisplayName;
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
        this.creatorDisplayName = builder.creatorDisplayName;
        this.company = builder.company;
    }

    public int getDocEntry() {
        return docEntry;
    }

    public String getJiraId() {
        return jiraId;
    }

    public String getCompany() {
        return company;
    }

    public List<DocumentLines> getDocumentLines() {
        return documentLines;
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
                ", creatorName='" + creatorDisplayName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other != null && this.getClass() == other.getClass()) {
            final Purchase purchase = (Purchase) other;
            return docEntry == purchase.docEntry &&
                    Objects.equals(documentLines, purchase.documentLines) &&
                    Objects.equals(requriedDate, purchase.requriedDate) &&
                    Objects.equals(docDate, purchase.docDate) &&
                    Objects.equals(jiraId, purchase.jiraId) &&
                    Objects.equals(creatorEmail, purchase.creatorEmail) &&
                    Objects.equals(company, purchase.company);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(docEntry,
                documentLines,
                requriedDate,
                docDate,
                jiraId,
                creatorEmail,
                company);
    }

    /**
     * Builder class to create new {@link Purchase} objects in a fluent manner.
     */
    public static class PurchaseBuilder {

        private List<DocumentLines> documentLines;

        private LocalDate requriedDate;

        private LocalDate docDate;

        private String jiraId;

        private String creatorEmail;

        private String creatorDisplayName;

        private String company;

        public PurchaseBuilder withDocument(final List<DocumentLines> documentLines) {
            this.documentLines = documentLines;
            return this;
        }

        public PurchaseBuilder setRequriedDate(final LocalDate requriedDate) {
            this.requriedDate = requriedDate;
            return this;
        }

        public PurchaseBuilder setDocDate(final LocalDate docDate) {
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

        public PurchaseBuilder setCreatorName(final String creatorDisplayName) {
            this.creatorDisplayName = creatorDisplayName;
            return this;
        }

        public PurchaseBuilder setCompany(final String company) {
            this.company = company;
            return this;
        }

        public Purchase build() {
            if (documentLines == null
                    || requriedDate == null
                    || docDate == null
                    || Strings.isNullOrEmpty(jiraId)
                    || Strings.isNullOrEmpty(creatorEmail)
                    || Strings.isNullOrEmpty(company)) {
                throw new IllegalArgumentException("Purchase mandatory fields were not present.");
            }
            return new Purchase(this);
        }
    }
}
