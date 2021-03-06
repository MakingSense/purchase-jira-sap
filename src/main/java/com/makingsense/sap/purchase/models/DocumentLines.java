package com.makingsense.sap.purchase.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents the information of a purchase in SAP.
 */
public class DocumentLines {

    @JsonProperty("LineNum")
    private int lineNum;

    @JsonProperty("ItemCode")
    private String itemCode;

    @JsonProperty("ItemDescription")
    private String itemDescription;

    @JsonProperty("Quantity")
    private int quantity;

    @JsonProperty("Price")
    private float price;

    @JsonProperty("CostingCode")
    private String costingCode;

    @JsonProperty("CostingCode2")
    private String costingCode2;

    @JsonProperty("CostingCode3")
    private String costingCode3;

    @JsonProperty("CostingCode4")
    private String costingCode4;

    @JsonProperty("Currency")
    private String currency;

    /**
     * Default constructor. Used by jackson.
     */
    public DocumentLines() {
    }

    private DocumentLines(final DocumentLinesBuilder builder) {
        this.lineNum = builder.lineNum;
        this.itemCode = builder.itemCode;
        this.itemDescription = builder.itemDescription;
        this.quantity = builder.quantity;
        this.price = builder.price;
        this.costingCode = builder.costingCode;
        this.costingCode2 = builder.costingCode2;
        this.costingCode3 = builder.costingCode3;
        this.costingCode4 = builder.costingCode4;
        this.currency = builder.currency;
    }

    @Override
    public String toString() {
        return "DocumentLines {" +
                "lineNum=" + lineNum +
                ", itemCode='" + itemCode + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", costingCode='" + costingCode + '\'' +
                ", costingCode2='" + costingCode2 + '\'' +
                ", costingCode3='" + costingCode3 + '\'' +
                ", costingCode4='" + costingCode4 + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other != null && this.getClass() == other.getClass()) {
            final DocumentLines that = (DocumentLines) other;
            return  quantity == that.quantity &&
                    Objects.equals(itemCode, that.itemCode) &&
                    Objects.equals(itemDescription, that.itemDescription) &&
                    Objects.equals(costingCode, that.costingCode) &&
                    Objects.equals(costingCode2, that.costingCode2) &&
                    Objects.equals(costingCode3, that.costingCode3) &&
                    Objects.equals(costingCode4, that.costingCode4);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity,
                itemCode,
                itemDescription,
                costingCode,
                costingCode2,
                costingCode3,
                costingCode4);
    }

    public int getLineNum() {
        return lineNum;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public String getCostingCode() {
        return costingCode;
    }

    public String getCostingCode2() {
        return costingCode2;
    }

    public String getCostingCode3() {
        return costingCode3;
    }

    public String getCostingCode4() {
        return costingCode4;
    }

    public String getCurrency() {
        return currency;
    }

    public static class DocumentLinesBuilder {

        private int lineNum;

        private String itemCode;

        private String itemDescription;

        private int quantity;

        private float price;

        private String costingCode;

        private String costingCode2;

        private String costingCode3;

        private String costingCode4;

        private String currency;

        public DocumentLinesBuilder setLineNum(final int lineNum) {
            this.lineNum = lineNum;
            return this;
        }

        public DocumentLinesBuilder setItemCode(final Optional<String> itemCode) {
            itemCode.ifPresent(item -> this.itemCode = item);
            return this;
        }

        public DocumentLinesBuilder setItemDescription(final String itemDescription) {
            this.itemDescription = itemDescription;

            return this;
        }

        public DocumentLinesBuilder setQuantity(final float quantity) {
            this.quantity = (int) quantity;
            return this;
        }

        public DocumentLinesBuilder setPrice(final float price) {
            this.price = price;
            return this;
        }

        public DocumentLinesBuilder setBusinessUnit(final Optional<String> businessUnit) {
            businessUnit.ifPresent(bu -> this.costingCode = bu);
            return this;
        }

        public DocumentLinesBuilder setDepartment(final Optional<String> department) {
            department.ifPresent(de -> this.costingCode2 = de);
            return this;
        }

        public DocumentLinesBuilder setLocation(final Optional<String> location) {
            location.ifPresent(lo -> this.costingCode3 = lo);
            return this;
        }

        public DocumentLinesBuilder setProject(final Optional<String> project) {
            project.ifPresent(pr -> this.costingCode4 = pr);
            return this;
        }

        public DocumentLinesBuilder setCurrency(final String currency) {
            this.currency = currency;
            return this;
        }

        public DocumentLines build() {
            if (Strings.isNullOrEmpty(itemCode)
                    || Strings.isNullOrEmpty(itemDescription)
                    || quantity <= 0
                    || price <= 0
                    || Strings.isNullOrEmpty(costingCode)
                    || Strings.isNullOrEmpty(costingCode2)
                    || Strings.isNullOrEmpty(costingCode3)
                    || Strings.isNullOrEmpty(costingCode4)
                    || Strings.isNullOrEmpty(currency)) {
                throw new IllegalArgumentException("Not all mandatory fields were provided.");
            }
            return new DocumentLines(this);
        }
    }

}
