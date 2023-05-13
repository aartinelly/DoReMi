package com.example.geektrust.commands;

import java.util.List;

import com.example.geektrust.exceptions.InvalidDateException;
import com.example.geektrust.services.ISubscriptionService;

public class StartSubscriptionCommand implements ICommand{
    private ISubscriptionService subscriptionService;

    public StartSubscriptionCommand(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }


    @Override
    public void execute(List<String> tokens) {
        try{
            int i=1;
           subscriptionService.startSubscription(tokens.get(i));
        }
        catch(InvalidDateException e){
            System.out.println(e.getMessage());
        }
    }
    
}
