package com.example.geektrust.services;

import com.example.geektrust.dtos.PrintRenewalDetails;
import com.example.geektrust.entities.SubscriptionPlan;

public interface ISubscriptionService {
    String startSubscription(String startDate);
    SubscriptionPlan addSubscription(String category, String planName);
    PrintRenewalDetails print();
}
