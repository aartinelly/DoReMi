package com.example.geektrust.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.geektrust.exceptions.NoSuchCommandException;

public class CommandInvoker {
    private static final Map<String, ICommand> commandMap = new HashMap<>();

    //Register all the commands
    public void register(String commandName, ICommand command){
        commandMap.put(commandName, command);
    }

    //get registered command 
    public ICommand getCommand(String commandName){
        return commandMap.get(commandName);
    }

    //execute registered commands
    public void execute(String commandName, List<String> tokens) throws NoSuchCommandException{
        ICommand command = getCommand(commandName);
        if(command == null){
            throw new NoSuchCommandException();
        }

        command.execute(tokens);
    }
}
