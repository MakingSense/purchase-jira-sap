package com.makingsense.sap.purchase.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the information of a purchased stored in Jira as a ticket.
 */
public class JiraPurchaseTicket {

    @NotBlank(message = "The company is mandatory.")
    private String company;

    @Pattern(regexp = ".+[ ]*/.+", message = "Business unit attribute has invalid format.")
    @NotBlank(message = "The business unit is mandatory.")
    private String businessUnit;

    @Pattern(regexp = "[a-zA-Z0-9]+[ ]*/.+", message = "Department attribute has invalid format.")
    @NotBlank(message = "The department is mandatory.")
    private String department;

    @Pattern(regexp = "[a-zA-Z0-9]+[ ]*/.+", message = "Project attribute has invalid format.")
    @NotBlank(message = "The ticket project is mandatory.")
    private String project;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "The date of payment is mandatory.")
    private LocalDate dateOfPayment;

    @NotBlank(message = "The Jira ticket id is mandatory.")
    private String ticketId;

    @NotBlank(message = "The ticket creator is mandatory.")
    private String creator;

    @NotBlank(message = "The creator email is mandatory")
    @Email(message = "The creator email is invalid")
    private String email;

    @NotBlank(message = "The ticket creator display name is mandatory.")
    private String creatorDisplayName;

    @NotBlank(message = "The ticket description is mandatory.")
    private String description;

    @NotBlank(message = "The currency is mandatory.")
    private String currency;

    @NotEmpty
    List<JiraPurchaseItem> items;

    /**
     * Default constructor
     */
    public JiraPurchaseTicket() {
        items = new ArrayList<>();
    }

    public String getCompany() {
        return company;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public String getDepartment() {
        return department;
    }

    public String getProject() {
        return project;
    }

    public LocalDate getDateOfPayment() {
        return dateOfPayment;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getCreator() {
        return creator;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatorDisplayName() {
        return creatorDisplayName;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrency() {
        return currency;
    }

    public List<JiraPurchaseItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "JiraPurchaseTicket {"
                + ", company='" + company + '\''
                + ", businessUnit='" + businessUnit + '\''
                + ", department='" + department + '\''
                + ", project='" + project + '\''
                + ", dateOfPayment=" + dateOfPayment
                + ", ticketId='" + ticketId + '\''
                + ", creator='" + creator + '\''
                + ", email='" + email + '\''
                + ", creatorDisplayName='" + creatorDisplayName + '\''
                + ", description='" + description + '\''
                + ", currency='" + currency + '\''
                + '}';
    }
}
