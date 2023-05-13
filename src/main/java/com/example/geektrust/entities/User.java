package com.example.geektrust.entities;

import java.util.ArrayList;
import java.util.List;

public class User{
    private List<SubscriptionPlan> subscriptionPlan;
    private TopUp topUp;
    private String id;

    public User(){
        subscriptionPlan = new ArrayList<>();
        this.topUp = null;
    }

    public User(String id) {
        this.id = id;
        subscriptionPlan = new ArrayList<>();
    }

    public User(List<SubscriptionPlan> subscriptionPlan, TopUp topUp){
        this.subscriptionPlan = subscriptionPlan;
        this.topUp = topUp;
    }

    
    public List<SubscriptionPlan> getSubscriptionPlan() {
        return subscriptionPlan;
    }
    public void setSubscriptionPlan(List<SubscriptionPlan> subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }
    public TopUp getTopUp() {
        return topUp;
    }
    // public void setTopUp(TopUp topUp) {
    //     this.topUp = topUp;
    // }    
    

    public void addSubscriptionPlan(SubscriptionPlan subscriptionPlan){
        this.subscriptionPlan.add(subscriptionPlan);
    }

    public void removeSubscriptionPlan(SubscriptionPlan subscriptionPlan){
        this.subscriptionPlan.remove(subscriptionPlan);
    }

    public String getId() {
        return id;
    }

}
