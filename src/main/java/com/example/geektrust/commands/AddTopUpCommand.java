package com.example.geektrust.commands;

import java.util.List;

import com.example.geektrust.exceptions.AddTopUpFailedException;
import com.example.geektrust.services.UserService;

public class AddTopUpCommand implements ICommand{
    private UserService userService;

    public AddTopUpCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(List<String> tokens) {
        try{
            int i=1, j=2;
            userService.addTopUp(tokens.get(i), Integer.parseInt(tokens.get(j)));
        }
        catch(AddTopUpFailedException e){
            System.out.println(e.getMessage());
        }
        
    }
    
}
