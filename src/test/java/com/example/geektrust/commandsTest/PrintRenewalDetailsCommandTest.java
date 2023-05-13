package com.example.geektrust.commandsTest;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.geektrust.commands.PrintRenewalDetailsCommand;
import com.example.geektrust.dtos.PrintRenewalDetails;
import com.example.geektrust.dtos.RenewalReminder;
import com.example.geektrust.entities.CategoryType;
import com.example.geektrust.services.SubscriptionService;

@DisplayName("PrintRenewalDetailCommandTest")
@ExtendWith(MockitoExtension.class)
public class PrintRenewalDetailsCommandTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private PrintRenewalDetailsCommand printRenewalDetailsCommand;
    int one =1;

    @Mock
    SubscriptionService subscriptionServiceMock;

    @BeforeEach
    void setup(){
        System.setOut(new PrintStream(outputStreamCaptor));
        printRenewalDetailsCommand = new PrintRenewalDetailsCommand(subscriptionServiceMock);
    }

    @DisplayName("execute method should print renewal details when given command and tokens")
    @Test
    public void execute_GivenPrintRenewalDetailCommandAndTokens_ShouldPrintRenewalDetails(){
        RenewalReminder rm1 = new RenewalReminder(CategoryType.MUSIC, "10-03-2022");
        RenewalReminder rm2 = new RenewalReminder(CategoryType.VIDEO, "10-05-2022");
        RenewalReminder rm3 = new RenewalReminder(CategoryType.PODCAST, "10-03-2022");

        List<RenewalReminder> renewalList = Arrays.asList(rm1, rm2, rm3);

        int amount = 750;
        PrintRenewalDetails printRenewalDetails = new PrintRenewalDetails(renewalList, amount);

        when(subscriptionServiceMock.print()).thenReturn(printRenewalDetails);

        String expected1 = "RENEWAL_REMINDER MUSIC 10-03-2022";
        String expected2 = "RENEWAL_REMINDER VIDEO 10-05-2022";
        String expected3 = "RENEWAL_REMINDER PODCAST 10-03-2022";
        String expected4 = "RENEWAL_AMOUNT 750";

        
        List<String> tokens = Arrays.asList("PRINT_RENEWAL_DETAILS");
        printRenewalDetailsCommand.execute(tokens);

        String actual[] = outputStreamCaptor.toString().trim().split(" ");
        String actual1 = actual[0] + " " + actual[1] +" " + actual[2].substring(0, 10);
        String actual2 = actual[0] + " " + actual[3] + " " + actual[4].substring(0, 10);
        String actual3 = actual[0] + " " + actual[5] + " " + actual[6].substring(0, 10);
        String actual4 = "RENEWAL_AMOUNT" + " " + actual[7];

        Assertions.assertEquals(actual1, expected1);
        Assertions.assertEquals(actual2, expected2);
        Assertions.assertEquals(actual3, expected3);
        Assertions.assertEquals(actual4, expected4);
        
        verify(subscriptionServiceMock, times(one)).print();

    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
