package com.example.geektrust.commandsTest;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.example.geektrust.commands.StartSubscriptionCommand;
import com.example.geektrust.exceptions.InvalidDateException;
import com.example.geektrust.services.SubscriptionService;

@DisplayName("StartSubscriptionCommandTest")
@ExtendWith(MockitoExtension.class)
public class StartSubscriptionCommandTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private StartSubscriptionCommand startSubscriptionCommand;

    @Mock
    SubscriptionService subscriptionService;

    @BeforeEach
    void setup(){
        System.setOut(new PrintStream(outputStreamCaptor));
        startSubscriptionCommand = new StartSubscriptionCommand(subscriptionService);
    }

    @Test
    @DisplayName("execute method should execute the StartSubscriptionCommand when given start date")
    public void execute_GivenStartDate_ShouldExecuteCommand(){
       when(subscriptionService.startSubscription("20-02-2022")).thenReturn("20-02-2022");
        List<String> tokens = Arrays.asList("START_SUBSCRIPTION", "20-02-2022");
        startSubscriptionCommand.execute(tokens);
    }

    @Test
    @DisplayName("execute method should throw InvalidDateException when given invalid date")
    public void execute_GivenStartDate_ShouldThrowException(){
        List<String> tokens = Arrays.asList("START_SUBSCRIPTION", "7-19-2022");
        String expected = "INVALID_DATE";
        doThrow(new InvalidDateException(expected)).when(subscriptionService).startSubscription(anyString());
        startSubscriptionCommand.execute(tokens);

        Assertions.assertEquals(expected, outputStreamCaptor.toString().trim());
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
