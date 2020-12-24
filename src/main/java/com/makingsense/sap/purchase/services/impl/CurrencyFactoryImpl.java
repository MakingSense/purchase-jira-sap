package com.makingsense.sap.purchase.services.impl;

import com.makingsense.sap.purchase.configuration.SapCurrencyConfiguration;
import com.makingsense.sap.purchase.errors.InvalidSAPDBException;
import com.makingsense.sap.purchase.services.CurrencyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class CurrencyFactoryImpl implements CurrencyFactory {

    private final Map<String, String> payloadCurrency;

    private final Map<String, String> inlineCurrency;

    /**
     * Creates a new instance {@link CurrencyFactoryImpl}.
     * @param configuration the {@link SapCurrencyConfiguration} configuration.
     */
    @Autowired
    public CurrencyFactoryImpl(final SapCurrencyConfiguration configuration) {
        this.payloadCurrency = configuration.getPayloadCurrency();
        this.inlineCurrency = configuration.getInlineCurrency();
    }

    @Override
    public String getCurrency(final String company, final String currency, final boolean isInLine) {
        if (isInLine) {
            return get(inlineCurrency, company, currency);
        } else {
            return get(payloadCurrency, company, currency);
        }
    }

    private String get(final Map<String, String> mapToLookFor,
                       final String company,
                       final String currency) {
        final Optional<String> source = mapToLookFor.keySet()
                .stream()
                .filter(key -> key.contains(company.toLowerCase())
                        && key.contains(currency.toLowerCase()))
                .findFirst();

        if (!source.isPresent()) {
            throw new InvalidSAPDBException("The company does not match with a SAP DB.");
        }

        return mapToLookFor.get(source.get());
    }
}
