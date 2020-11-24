package com.makingsense.sap.purchase.services.impl;

import com.makingsense.sap.purchase.models.JiraPurchaseTicket;
import com.makingsense.sap.purchase.models.Purchase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link MapIssueToSAPImpl}.
 */
public class MapIssueToSAPImplTest {

    private MapIssueToSAPImpl target;

    @BeforeEach
    public void setUp() {
        target = new MapIssueToSAPImpl();
        ReflectionTestUtils.setField(target, "maxDescriptionLength", 100);
    }

    @Test
    public void shouldMigrateTicket() {
        // Arrange
        final JiraPurchaseTicket ticket = createValidTicket();

        // Act
        final Purchase actual = target.mapToPurchase(ticket);

        // Assert
        assertThat(actual.getDocumentLines().get(0).getItemCode(), is("A0001"));
        assertThat(actual.getDocumentLines().get(0).getCostingCode(), is("100"));
        assertThat(actual.getDocumentLines().get(0).getCostingCode2(), is("200"));
        assertThat(actual.getDocumentLines().get(0).getCostingCode3(), is("300"));
        assertThat(actual.getDocumentLines().get(0).getCostingCode4(), is("400"));
    }

    @Test
    public void shouldThrowExceptionWhenTicketHasNotMandatoryFields() {
        // Arrange
        final JiraPurchaseTicket ticket = new JiraPurchaseTicket();

        // Act, Assert
        assertThrows(IllegalArgumentException.class,
                () -> target.mapToPurchase(ticket),
                "When ticket does not contain mandatory fields an exception should be thrown.");
    }

    private JiraPurchaseTicket createValidTicket() {
        final String accountId = UUID.randomUUID().toString();

        final JiraPurchaseTicket ticket = mock(JiraPurchaseTicket.class);

        when(ticket.getDescription()).thenReturn("ItemDescription");
        when(ticket.getBusinessUnit()).thenReturn("100 - Test Business Unit");
        when(ticket.getCompany()).thenReturn("Company");
        when(ticket.getCreator()).thenReturn(accountId);
        when(ticket.getCreatorDisplayName()).thenReturn("John Doe");
        when(ticket.getDateOfPayment()).thenReturn(LocalDate.now());
        when(ticket.getDepartment()).thenReturn("200 - Test Department");
        when(ticket.getLocation()).thenReturn("300 - Test Location");
        when(ticket.getProject()).thenReturn("400 - Test Project");
        when(ticket.getQuantity()).thenReturn(23f);
        when(ticket.getTotal()).thenReturn(0.1f);
        when(ticket.getTicketId()).thenReturn("TicketId");
        when(ticket.getEmail()).thenReturn("johndoe@test.com");
        when(ticket.getItemCode()).thenReturn("US-A0001 - Test Item code");

        return ticket;
    }
}
