package com.makingsense.sap.purchase.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.makingsense.sap.purchase.models.Purchase;

import java.io.IOException;

/**
 * Serializer used in the purchase API to display minimal information of the new entry created
 * on SAP.
 */
public class PurchaseSerializer extends StdSerializer<Purchase> {

    public PurchaseSerializer() {
        this(null);
    }

    public PurchaseSerializer(final Class<Purchase> purchaseClass) {
        super(purchaseClass);
    }

    @Override
    public void serialize(final Purchase purchase,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("docEntry", purchase.getDocEntry());
        jsonGenerator.writeStringField("jiraId", purchase.getJiraId());
        jsonGenerator.writeEndObject();
    }
}
