package com.makingsense.sap.purchase.data.source.impl;

import com.makingsense.sap.purchase.configuration.SAPConfigurationProperties;
import com.makingsense.sap.purchase.data.source.SAPSourceFactory;
import com.makingsense.sap.purchase.errors.InvalidSAPDBException;
import com.makingsense.sap.purchase.data.source.SAPSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link SAPSourceFactory} to obtain the correct {@link SAPSource} to connect
 * to SAP.
 */
@Component
public class SAPSourceFactoryImpl implements SAPSourceFactory {

    private final Map<String, SAPSource> sapSources;

    @Autowired
    public SAPSourceFactoryImpl(final SAPConfigurationProperties properties) {
        sapSources = properties.getConfigurations();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SAPSource getSource(final String company) {
        final Optional<String> source = sapSources.keySet()
                .stream()
                .filter(key -> company.toLowerCase().contains(key))
                .findFirst();

        if (!source.isPresent()) {
            throw new InvalidSAPDBException("The company does not match with a SAP DB.");
        }

        return sapSources.get(source.get());
    }
}
