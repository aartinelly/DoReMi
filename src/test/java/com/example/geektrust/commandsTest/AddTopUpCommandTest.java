package com.example.geektrust.commandsTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
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

import com.example.geektrust.commands.AddTopUpCommand;
import com.example.geektrust.entities.Constants;
import com.example.geektrust.entities.TopUp;
import com.example.geektrust.entities.TopUpType;
import com.example.geektrust.exceptions.AddTopUpFailedException;
import com.example.geektrust.services.UserService;

@DisplayName("AddTopUpCommandTest")
@ExtendWith(MockitoExtension.class)
public class AddTopUpCommandTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private AddTopUpCommand addTopUpCommand;
    int one =1;
     String id = "1";

    @Mock
    UserService userServiceMock;

    @BeforeEach
    void setup(){
        System.setOut(new PrintStream(outputStreamCaptor));
        addTopUpCommand = new AddTopUpCommand(userServiceMock);
    }

    @DisplayName("execute method should execute addTopUpCommand when given valid top up plan")
    @Test
    public void execute_GivenValidTopUpPlan_ShouldExecuteCommand(){
        int price = Constants.oneMonth * Constants.topUpFourDevice;
        TopUp topUp = new TopUp(TopUpType.FOUR_DEVICE, Constants.oneMonth, price);
        when(userServiceMock.addTopUp(anyString(), anyInt())).thenReturn(topUp);

        List<String> tokens = Arrays.asList("ADD_TOPUP","FOUR_DEVICE","1");
        addTopUpCommand.execute(tokens);

        verify(userServiceMock, times(one)).addTopUp(anyString(), anyInt());
    }

    @DisplayName("execute method should throw AddTopUpFailedException when subscription list is empty")
    @Test
    public void execute_GivenValidTopUpPlan_ShouldThrowAddTopUpFailedException(){
        
        String expected = "ADD_TOPUP_FAILED SUBSCRIPTIONS_NOT_FOUND";
        doThrow(new AddTopUpFailedException(expected)).when(userServiceMock).addTopUp(anyString(), anyInt());

        List<String> tokens = Arrays.asList("ADD_TOPUP","FOUR_DEVICE","1");
        addTopUpCommand.execute(tokens);

        Assertions.assertEquals(expected, outputStreamCaptor.toString().trim());

        verify(userServiceMock, times(one)).addTopUp(anyString(), anyInt());
    }

    @DisplayName("execute method should throw AddTopUpFailedException when subscription list is empty")
    @Test
    public void execute_GivenDuplicateTopUpPlan_ShouldThrowAddTopUpFailedException(){
        
        String expected = "ADD_TOPUP_FAILED DUPLICATE_TOPUP";
        doThrow(new AddTopUpFailedException(expected)).when(userServiceMock).addTopUp(anyString(), anyInt());

        List<String> tokens = Arrays.asList("ADD_TOPUP","FOUR_DEVICE","1");
        addTopUpCommand.execute(tokens);

        Assertions.assertEquals(expected, outputStreamCaptor.toString().trim());

        verify(userServiceMock, times(one)).addTopUp(anyString(), anyInt());
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
