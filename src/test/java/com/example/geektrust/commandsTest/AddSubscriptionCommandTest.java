package com.example.geektrust.commandsTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
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

import com.example.geektrust.commands.AddSubscriptionCommand;
import com.example.geektrust.entities.CategoryType;
import com.example.geektrust.entities.SubscriptionPlan;
import com.example.geektrust.entities.SubscriptionType;
import com.example.geektrust.exceptions.AddSubscriptionFailedException;
import com.example.geektrust.services.SubscriptionService;

@DisplayName("AddSubscriptionCommandTest")
@ExtendWith(MockitoExtension.class)
public class AddSubscriptionCommandTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private AddSubscriptionCommand addSubscriptionCommand;

    int one =1;

    @Mock
    SubscriptionService subscriptionServiceMock;

    @BeforeEach
    void setup(){
        System.setOut(new PrintStream(outputStreamCaptor));
        addSubscriptionCommand = new AddSubscriptionCommand(subscriptionServiceMock);
    }

    @DisplayName("execute method should execute AddSubscription Command with the given tokens")
    @Test
    public void execute_GivenTokens_ShouldExecuteAddSubscriptionCommand(){
        SubscriptionPlan sp = new SubscriptionPlan(CategoryType.MUSIC, SubscriptionType.PERSONAL);
        when(subscriptionServiceMock.addSubscription(anyString(), anyString())).thenReturn(sp);
        
        List<String> tokens = Arrays.asList("ADD_SUBSCRIPTION", "MUSIC", "PERSONAL");
        addSubscriptionCommand.execute(tokens);

        verify(subscriptionServiceMock, times(one)).addSubscription(anyString(), anyString());

    }

    @DisplayName("execute method should throw exception when passed invalid date")
    @Test
    public void execute_GivenInvalidDate_ShouldThrowAddSubscriptionFailedException(){
        String expected = "ADD_SUBSCRIPTION_FAILED INVALID_DATE";
        doThrow(new AddSubscriptionFailedException(expected)).when(subscriptionServiceMock).addSubscription(anyString(), anyString());
        List<String> tokens = Arrays.asList("ADD_SUBSCRIPTION", "MUSIC", "PERSONAL");
        addSubscriptionCommand.execute(tokens);

        Assertions.assertEquals(expected, outputStreamCaptor.toString().trim());

        verify(subscriptionServiceMock, times(one)).addSubscription(anyString(), anyString());
    }

    @DisplayName("execute method should throw exception when passed duplicate category")
    @Test
    public void execute_GivenDuplicateCategory_ShouldThrowAddSubscriptionFailedException(){
        String expected = "ADD_SUBSCRIPTION_FAILED DUPLICATE_CATEGORY";
        doThrow(new AddSubscriptionFailedException(expected)).when(subscriptionServiceMock).addSubscription(anyString(), anyString());
        List<String> tokens = Arrays.asList("ADD_SUBSCRIPTION", "MUSIC", "PERSONAL");
        addSubscriptionCommand.execute(tokens);

        Assertions.assertEquals(expected, outputStreamCaptor.toString().trim());

        verify(subscriptionServiceMock, times(one)).addSubscription(anyString(), anyString());
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

}
