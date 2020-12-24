package com.makingsense.sap.purchase.configuration;

import com.makingsense.sap.purchase.data.source.SAPSource;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuration class for the DB  to use when communicating with SAP.
 */
@ConfigurationProperties(prefix = "sap.purchase")
public class SAPConfigurationProperties {

    private Map<String, SAPSource> configurations = new HashMap<>();

    public Map<String, SAPSource> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(final Map<String, SAPSource> configurations) {
        this.configurations = configurations;
    }

}
