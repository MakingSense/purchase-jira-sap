package com.makingsense.sap.purchase.services.impl;

import com.makingsense.sap.purchase.models.JiraPurchaseItem;
import com.makingsense.sap.purchase.models.JiraPurchaseTicket;
import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.services.CurrencyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link MapIssueToSAPImpl}.
 */
public class MapIssueToSAPImplTest {

    private MapIssueToSAPImpl target;

    private CurrencyFactory currencyFactory;

    @BeforeEach
    public void setUp() {
        currencyFactory = mock(CurrencyFactory.class);

        target = new MapIssueToSAPImpl(currencyFactory);
        ReflectionTestUtils.setField(target, "maxDescriptionLength", 100);
    }

    @Test
    public void shouldMigrateTicket() {
        // Arrange
        final JiraPurchaseTicket ticket = createValidTicket();
        final String currency = "USD";

        when(currencyFactory.getCurrency(any(), anyString(), anyBoolean())).thenReturn(currency);

        // Act
        final Purchase actual = target.mapToPurchase(ticket, "msargTest");

        // Assert
        assertThat(actual.getDocumentLines().get(0).getItemCode(), is("A0001"));
        assertThat(actual.getDocumentLines().get(0).getCostingCode(), is("100"));
        assertThat(actual.getDocumentLines().get(0).getCostingCode2(), is("200"));
        assertThat(actual.getDocumentLines().get(0).getCostingCode3(), is("300"));
        assertThat(actual.getDocumentLines().get(0).getCostingCode4(), is("400"));
        assertThat(actual.getDocumentLines().get(0).getCurrency(), is(currency));
    }

    @Test
    public void shouldThrowExceptionWhenTicketHasNotMandatoryFields() {
        // Arrange
        final JiraPurchaseTicket ticket = new JiraPurchaseTicket();

        // Act, Assert
        assertThrows(IllegalArgumentException.class,
                () -> target.mapToPurchase(ticket, "msargTest"),
                "When ticket does not contain mandatory fields an exception should be thrown.");
    }

    private JiraPurchaseTicket createValidTicket() {
        final String accountId = UUID.randomUUID().toString();

        final JiraPurchaseItem item = mock(JiraPurchaseItem.class);
        when(item.getQuantity()).thenReturn(23f);
        when(item.getUnitPrice()).thenReturn(0.1f);
        when(item.getLocation()).thenReturn("300 / Test Location");
        when(item.getItemCode()).thenReturn("US/A0001 / Test Item code");

        final JiraPurchaseTicket ticket = mock(JiraPurchaseTicket.class);

        when(ticket.getDescription()).thenReturn("ItemDescription");
        when(ticket.getBusinessUnit()).thenReturn("100 / Test Business Unit");
        when(ticket.getCurrency()).thenReturn("USD");
        when(ticket.getCompany()).thenReturn("Company");
        when(ticket.getCreator()).thenReturn(accountId);
        when(ticket.getCreatorDisplayName()).thenReturn("John Doe");
        when(ticket.getDateOfPayment()).thenReturn(LocalDate.now());
        when(ticket.getDepartment()).thenReturn("200 / Test Department");
        when(ticket.getProject()).thenReturn("400 / Test Project");
        when(ticket.getTicketId()).thenReturn("TicketId");
        when(ticket.getEmail()).thenReturn("johndoe@test.com");
        when(ticket.getItems()).thenReturn(Collections.singletonList(item));

        return ticket;
    }
}
