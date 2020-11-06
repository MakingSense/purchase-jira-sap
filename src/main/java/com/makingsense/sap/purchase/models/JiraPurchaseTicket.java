package com.makingsense.sap.purchase.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Represents the information of a purchased stored in Jira as a ticket.
 */
public class JiraPurchaseTicket {

    @Min(value = 1, message = "The quantity attribute is mandatory and must be at least 1.")
    private float quantity;

    @DecimalMin(value = "0.01", message = "The total attribute is mandatory and must be greater than 0.01")
    private float total;

    @NotBlank(message = "The company is mandatory.")
    private String company;

    @NotBlank(message = "The business unit is mandatory.")
    private String businessUnit;

    @NotBlank(message = "The ticket business unit is mandatory.")
    private String department;

    @NotBlank(message = "The ticket location is mandatory.")
    private String location;

    @NotBlank(message = "The ticket project is mandatory.")
    private String project;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "The ticket creation date is mandatory.")
    private LocalDate creationDate;

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

    /**
     * Default constructor
     */
    public JiraPurchaseTicket() {
    }

    public float getQuantity() {
        return quantity;
    }

    public float getTotal() {
        return total;
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

    public String getLocation() {
        return location;
    }

    public String getProject() {
        return project;
    }

    public LocalDate getCreationDate() {
        return creationDate;
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

    @Override
    public String toString() {
        return "JiraPurchaseTicket {" +
                "quantity=" + quantity +
                ", total=" + total +
                ", company='" + company + '\'' +
                ", businessUnit='" + businessUnit + '\'' +
                ", department='" + department + '\'' +
                ", location='" + location + '\'' +
                ", project='" + project + '\'' +
                ", creationDate=" + creationDate +
                ", dateOfPayment=" + dateOfPayment +
                ", ticketId='" + ticketId + '\'' +
                ", creator='" + creator + '\'' +
                ", email='" + email + '\'' +
                ", creatorDisplayName='" + creatorDisplayName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
