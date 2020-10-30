package com.makingsense.sap.purchase.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

import java.util.Objects;
import java.util.function.Consumer;

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

    @JsonProperty("CostingCode")
    private String costingCode;

    @JsonProperty("CostingCode2")
    private String costingCode2;

    @JsonProperty("CostingCode3")
    private String costingCode3;

    @JsonProperty("CostingCode4")
    private String costingCode4;

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
        this.costingCode = builder.costingCode;
        this.costingCode2 = builder.costingCode2;
        this.costingCode3 = builder.costingCode3;
        this.costingCode4 = builder.costingCode4;
    }

    @Override
    public String toString() {
        return "DocumentLines {" +
                "lineNum=" + lineNum +
                ", itemCode='" + itemCode + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", quantity=" + quantity +
                ", costingCode='" + costingCode + '\'' +
                ", costingCode2='" + costingCode2 + '\'' +
                ", costingCode3='" + costingCode3 + '\'' +
                ", costingCode4='" + costingCode4 + '\'' +
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

    public static class DocumentLinesBuilder {

        private int lineNum;

        private String itemCode;

        private String itemDescription;

        private int quantity;

        private String costingCode;

        private String costingCode2;

        private String costingCode3;

        private String costingCode4;

        public DocumentLinesBuilder setLineNum(final int lineNum) {
            this.lineNum = lineNum;
            return this;
        }

        public DocumentLinesBuilder setItemCode(final String itemCode) {
            this.itemCode = itemCode;
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

        public DocumentLinesBuilder setBusinessUnit(final String businessUnit) {
            this.costingCode = businessUnit;
            return this;
        }

        public DocumentLinesBuilder setDepartment(final String department) {
            this.costingCode2 = department;
            return this;
        }

        public DocumentLinesBuilder setLocation(final String location) {
            this.costingCode3 = location;
            return this;
        }

        public DocumentLinesBuilder setProject(final String project) {
            this.costingCode4 = project;
            return this;
        }

        public DocumentLines build() {
            if (Strings.isNullOrEmpty(itemCode)
                    || Strings.isNullOrEmpty(itemDescription)
                    || quantity <= 0
                    || Strings.isNullOrEmpty(costingCode)
                    || Strings.isNullOrEmpty(costingCode2)
                    || Strings.isNullOrEmpty(costingCode3)
                    || Strings.isNullOrEmpty(costingCode4)) {
                throw new IllegalArgumentException("Not all mandatory fields were provided.");
            }
            return new DocumentLines(this);
        }
    }

}
