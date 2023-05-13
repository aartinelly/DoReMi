package com.example.geektrust.commands;

import java.util.List;

import com.example.geektrust.exceptions.AddSubscriptionFailedException;
import com.example.geektrust.services.ISubscriptionService;

public class AddSubscriptionCommand implements ICommand{
    private ISubscriptionService subscriptionService;

    public AddSubscriptionCommand(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(List<String> tokens) {
        try{
            int i = 1, j = 2;
            subscriptionService.addSubscription(tokens.get(i), tokens.get(j));
        }
        catch(AddSubscriptionFailedException e){
            System.out.println(e.getMessage());
        }
    }
}
