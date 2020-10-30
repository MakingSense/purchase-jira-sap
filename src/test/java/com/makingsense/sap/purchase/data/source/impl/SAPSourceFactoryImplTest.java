package com.makingsense.sap.purchase.data.source.impl;

import com.makingsense.sap.purchase.configuration.SAPConfigurationProperties;
import com.makingsense.sap.purchase.data.source.SAPSource;
import com.makingsense.sap.purchase.errors.InvalidSAPDBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link SAPSourceFactoryImpl}.
 */
public class SAPSourceFactoryImplTest {

    private SAPSourceFactoryImpl target;

    private SAPConfigurationProperties properties;

    private SAPSource source;

    @BeforeEach
    public void setUp() {
        properties = mock(SAPConfigurationProperties.class);

        source = mock(SAPSource.class);
        final Map<String, SAPSource> sources = new HashMap(){{
            put("making", source);
        }};

        when(properties.getConfigurations()).thenReturn(sources);

        target = new SAPSourceFactoryImpl(properties);
    }

    @Test
    public void shouldReturnCorrectSAPSource() {
        // Act
        final SAPSource making = target.getSource("making");

        // Assert
        assertThat(making, is(source));
    }

    @Test
    public void shouldReturnCorrectSAPSourceWhenCompanyContainsUpperCase() {
        // Act
        final SAPSource making = target.getSource("Making Sense LLC");

        // Assert
        assertThat(making, is(source));
    }

    @Test
    public void shouldThrowExceptionWhenUsingInvalidCompany() {
        assertThrows(InvalidSAPDBException.class,
                () -> target.getSource("notValid"),
                "When an invalid company is provided, the factory should throw an exception.");
    }
}
