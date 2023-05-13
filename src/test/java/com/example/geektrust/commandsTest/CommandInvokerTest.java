package com.example.geektrust.commandsTest;

import static org.mockito.ArgumentMatchers.anyList;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.geektrust.commands.AddSubscriptionCommand;
import com.example.geektrust.commands.AddTopUpCommand;
import com.example.geektrust.commands.CommandInvoker;
import com.example.geektrust.commands.PrintRenewalDetailsCommand;
import com.example.geektrust.commands.StartSubscriptionCommand;
import com.example.geektrust.exceptions.NoSuchCommandException;

@DisplayName("CommandInvokerTest")
@ExtendWith(MockitoExtension.class)
public class CommandInvokerTest {
    private CommandInvoker commandInvoker;

    @Mock
    AddSubscriptionCommand addSubscriptionCommand;

    @Mock
    AddTopUpCommand addTopUpCommand;

    @Mock
    PrintRenewalDetailsCommand printRenewalDetailsCommand;

    @Mock
    StartSubscriptionCommand startSubscriptionCommand;

    @BeforeEach
    void setup(){
        commandInvoker = new CommandInvoker();

        commandInvoker.register("START_SUBSCRIPTION", startSubscriptionCommand);
        commandInvoker.register("ADD_SUBSCRIPTION", addSubscriptionCommand);
        commandInvoker.register("ADD_TOPUP", addTopUpCommand);
        commandInvoker.register("PRINT_RENEWAL_DETAILS", printRenewalDetailsCommand);
    }

    @Test
    @DisplayName("executeCommand method Should Execute Command Given CommandName and List of tokens")
    public void executeCommand_GivenNameAndTokens_ShouldExecuteCommand(){
        commandInvoker.execute("START_SUBSCRIPTION", anyList());
        commandInvoker.execute("ADD_SUBSCRIPTION", anyList());
        commandInvoker.execute("ADD_TOPUP", anyList());
        commandInvoker.execute("PRINT_RENEWAL_DETAILS", anyList());
    }


    @Test
    @DisplayName("executeCommand method should throw exception given commandName and List of tokens")
    public void executeCommand_GivenNameAndTokens_ShouldThrowException(){
        Assertions.assertThrows(NoSuchCommandException.class, () -> {
            commandInvoker.execute("NEW_COMMAND", new ArrayList<String>());
        });
        
    }

}
