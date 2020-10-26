package com.makingsense.sap.purchase.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Represents the information of a purchased stored in Jira as a ticket.
 */
public class JiraPurchaseTicket {

    @Min(value = 1, message = "The quantity of items must be at least 1.")
    private int quantity;

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
    private Date creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "The date of payment is mandatory.")
    private Date dateOfPayment;

    @NotBlank(message = "The Jira ticket id is mandatory.")
    private String ticketId;

    @NotBlank(message = "The ticket creator is mandatory.")
    private String creator;

    @NotBlank(message = "The ticket description is mandatory.")
    private String description;

    /**
     * Default constructor
     */
    public JiraPurchaseTicket() {
    }

    public int getQuantity() {
        return quantity;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getDateOfPayment() {
        return dateOfPayment;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getCreator() {
        return creator;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "JiraPurchaseTicket {" +
                "quantity=" + quantity +
                ", businessUnit='" + businessUnit + '\'' +
                ", department='" + department + '\'' +
                ", location='" + location + '\'' +
                ", project='" + project + '\'' +
                ", creationDate=" + creationDate +
                ", dateOfPayment=" + dateOfPayment +
                ", ticketId='" + ticketId + '\'' +
                ", creator='" + creator + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
