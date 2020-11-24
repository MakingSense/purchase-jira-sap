package com.makingsense.sap.purchase.services.impl;

import com.makingsense.sap.purchase.models.JiraPurchaseTicket;
import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.repositories.SAPRepository;
import com.makingsense.sap.purchase.services.MapIssueToSAP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link JiraToSAPPurchaseTest}.
 */
public class JiraToSAPPurchaseTest {

    private JiraToSAPPurchase target;

    private SAPRepository repository;

    private MapIssueToSAP mapIssueToSAP;

    @BeforeEach
    public void setUp() {
        repository = mock(SAPRepository.class);
        mapIssueToSAP = mock(MapIssueToSAP.class);

        target = new JiraToSAPPurchase(repository, mapIssueToSAP);
    }

    @Test
    public void shouldMigrateTicket() {
        // Arrange
        final JiraPurchaseTicket ticket = mock(JiraPurchaseTicket.class);
        final Purchase expected = mock(Purchase.class);

        when(mapIssueToSAP.mapToPurchase(any())).thenReturn(expected);
        when(repository.createPurchase(any())).thenReturn(expected);

        // Act
        final Purchase actual = target.migrate(ticket);

        // Assert
        assertThat(actual, is(expected));
        verify(repository).createPurchase(any());
        verify(mapIssueToSAP).mapToPurchase(any());
    }

}
