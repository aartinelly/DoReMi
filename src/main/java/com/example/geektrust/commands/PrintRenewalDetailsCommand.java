package com.example.geektrust.commands;

import java.util.List;

import com.example.geektrust.dtos.PrintRenewalDetails;
import com.example.geektrust.dtos.RenewalReminder;
import com.example.geektrust.exceptions.NoSubscriptionFoundException;
import com.example.geektrust.services.ISubscriptionService;

public class PrintRenewalDetailsCommand implements ICommand{
    private ISubscriptionService subscriptionService;

    public PrintRenewalDetailsCommand(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(List<String> tokens) {
        try{
           PrintRenewalDetails details = subscriptionService.print();
           for (RenewalReminder print : details.getRenewalReminders()) {
                System.out.println("RENEWAL_REMINDER " +print.getCategoryType()+" "+print.getRenewalDate());
           }

           System.out.println("RENEWAL_AMOUNT "+details.getAmount());
        }
        catch(NoSubscriptionFoundException e){
            System.out.println(e.getMessage());
        }
        
    }

    
}
