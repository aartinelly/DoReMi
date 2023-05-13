package com.example.geektrust.appConfig;

import com.example.geektrust.commands.AddSubscriptionCommand;
import com.example.geektrust.commands.AddTopUpCommand;
import com.example.geektrust.commands.CommandInvoker;
import com.example.geektrust.commands.PrintRenewalDetailsCommand;
import com.example.geektrust.commands.StartSubscriptionCommand;
import com.example.geektrust.repositories.ISubscriptionRepository;
import com.example.geektrust.repositories.SubscriptionRepository;
import com.example.geektrust.repositories.UserRepository;
import com.example.geektrust.services.ISubscriptionService;
import com.example.geektrust.services.SubscriptionService;
import com.example.geektrust.services.UserService;

public class ApplicationConfig {
    //repository object
    private final ISubscriptionRepository subscriptionRepository = new SubscriptionRepository();
    private final UserRepository userRepository = new UserRepository();

    //Service object
    private final ISubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository, userRepository);
    private final UserService userService = new UserService(userRepository);

    //command object
    private final StartSubscriptionCommand startSubscriptionCommand = new StartSubscriptionCommand(subscriptionService);
    private final AddSubscriptionCommand addSubscriptionCommand = new AddSubscriptionCommand(subscriptionService);
    private final AddTopUpCommand addTopUpCommand = new AddTopUpCommand(userService);
    private final PrintRenewalDetailsCommand printRenewalDetailsCommand = new PrintRenewalDetailsCommand(subscriptionService);
    
    private final CommandInvoker commandInvoker = new CommandInvoker();

    public CommandInvoker getCommandInvoker(){
        commandInvoker.register("START_SUBSCRIPTION", startSubscriptionCommand);
        commandInvoker.register("ADD_SUBSCRIPTION", addSubscriptionCommand);
        commandInvoker.register("ADD_TOPUP", addTopUpCommand);
        commandInvoker.register("PRINT_RENEWAL_DETAILS", printRenewalDetailsCommand);

        return commandInvoker;
    }
}
