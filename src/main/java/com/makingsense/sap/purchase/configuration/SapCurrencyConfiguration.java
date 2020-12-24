package com.makingsense.sap.purchase.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "sap.purchase.currency")
public class SapCurrencyConfiguration {

    private Map<String, String> payloadCurrency = new HashMap<>();

    private Map<String, String> inlineCurrency = new HashMap<>();

    public Map<String, String> getPayloadCurrency() {
        return payloadCurrency;
    }

    public Map<String, String> getInlineCurrency() {
        return inlineCurrency;
    }

    public void setConfigurations(final Map<String, String> configurations) {
        configurations.forEach((key, value) -> {
            if (key.contains("inline")) {
                inlineCurrency.put(key, value);
            } else {
                payloadCurrency.put(key, value);
            }
        });
    }

}

