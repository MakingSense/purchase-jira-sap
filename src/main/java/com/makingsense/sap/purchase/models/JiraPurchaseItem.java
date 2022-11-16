package com.makingsense.sap.purchase.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.util.StringUtils;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Represents the different Items that a purchase can have.
 */
public class JiraPurchaseItem {

    @DecimalMin(value = "0.01", message = "The unit price attribute is mandatory and must be greater than 0.01")
    private float unitPrice;

    @Min(value = 1, message = "The quantity attribute is mandatory and must be at least 1.")
    private float quantity;

    @Pattern(regexp = "[a-zA-Z0-9]+[ ]*/.+", message = "Location attribute has invalid format.")
    @NotBlank(message = "The ticket location is mandatory.")
    private String location;

    @Pattern(regexp = "[a-zA-Z]+[ ]*/[ ]*[a-zA-Z0-9]+[ ]*/.+", message = "Item code has invalid format.")
    @NotBlank(message = "The item code is mandatory.")
    private String itemCode;

    /**
     * Default constructor.
     */
    public JiraPurchaseItem() {
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getLocation() {
        return location;
    }

    public String getItemCode() {
        return itemCode;
    }

    public boolean isValid() {
        return unitPrice != 0.0 && quantity != 0.0 && StringUtils.isEmpty(location) && StringUtils.isEmpty(itemCode);
    }

    @Override
    public String toString() {
        return "JiraPurchaseTicket {"
                + ", itemCode='" + itemCode + '\''
                + ", location='" + location + '\''
                + ", unitPrice='" + unitPrice + '\''
                + ", quantity='" + quantity + '\''
                + '}';
    }
}
