package com.makingsense.sap.purchase.services.impl;

import com.makingsense.sap.purchase.models.JiraPurchaseTicket;
import com.makingsense.sap.purchase.models.Purchase;
import com.makingsense.sap.purchase.repositories.SAPRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

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

    private static final String BUSINESS_UNIT_KEY = "Default Unit";

    private static final String DEPARTMENT_KEY = "Default department";

    private static final String LOCATION_KEY = "Default location";

    private static final String PROJECT_KEY = "Default project";

    private Map<String, String> businessUnitMap = Collections.singletonMap(BUSINESS_UNIT_KEY, "1000");

    private Map<String, String> departmentMap = Collections.singletonMap(DEPARTMENT_KEY, "1000");

    private Map<String, String> locationMap = Collections.singletonMap(LOCATION_KEY, "1000");

    private Map<String, String> projectMap = Collections.singletonMap(PROJECT_KEY, "1000");

    @BeforeEach
    public void setUp() {
        repository = mock(SAPRepository.class);

        target = new JiraToSAPPurchase(repository);

        ReflectionTestUtils.setField(target, "businessUnitMap", businessUnitMap);
        ReflectionTestUtils.setField(target, "departmentMap", departmentMap);
        ReflectionTestUtils.setField(target, "locationMap", locationMap);
        ReflectionTestUtils.setField(target, "projectMap", projectMap);
        ReflectionTestUtils.setField(target, "itemCode", "100");
        ReflectionTestUtils.setField(target, "maxDescriptionLength", 100);
    }

    @Test
    public void shouldMigrateTicket() {
        // Arrange
        final JiraPurchaseTicket ticket = createValidTicket();
        final Purchase expected = mock(Purchase.class);
        when(repository.createPurchase(any())).thenReturn(expected);

        // Act
        final Purchase actual = target.migrate(ticket);

        // Assert
        assertThat(actual, is(expected));
        verify(repository).createPurchase(any());
    }

    @Test
    public void shouldThrowExceptionWhenTicketHasNotMandatoryFields() {
        // Arrange
        final JiraPurchaseTicket ticket = new JiraPurchaseTicket();

        // Act
        assertThrows(IllegalArgumentException.class,
                () -> target.migrate(ticket),
                "When ticket does not contain mandatory fields an exception should be thrown.");

        // Assert
        verifyNoInteractions(repository);
    }

    private JiraPurchaseTicket createValidTicket() {
        final String accountId = UUID.randomUUID().toString();

        final JiraPurchaseTicket ticket = mock(JiraPurchaseTicket.class);

        when(ticket.getDescription()).thenReturn("ItemDescription");
        when(ticket.getBusinessUnit()).thenReturn(BUSINESS_UNIT_KEY);
        when(ticket.getCompany()).thenReturn("Company");
        when(ticket.getCreator()).thenReturn(accountId);
        when(ticket.getCreatorDisplayName()).thenReturn("John Doe");
        when(ticket.getDateOfPayment()).thenReturn(new Date());
        when(ticket.getDepartment()).thenReturn(DEPARTMENT_KEY);
        when(ticket.getProject()).thenReturn(PROJECT_KEY);
        when(ticket.getQuantity()).thenReturn(23f);
        when(ticket.getTicketId()).thenReturn("TicketId");
        when(ticket.getEmail()).thenReturn("johndoe@test.com");
        when(ticket.getLocation()).thenReturn(LOCATION_KEY);

        return ticket;
    }
}
