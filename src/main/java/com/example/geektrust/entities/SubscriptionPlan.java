package com.example.geektrust.entities;

import java.time.LocalDate;

public class SubscriptionPlan {
    private String id;
    private LocalDate startDate;
    private LocalDate renewalDate;
    private SubscriptionType type; 
    private int months;
    private int price;
    private CategoryType category;
   

    public SubscriptionPlan(CategoryType category, SubscriptionType type){
        this.category = category;
        this.type = type;
    
    }

    public SubscriptionPlan(String id, CategoryType category, SubscriptionType type){
        this(category, type);
        this.id = id;
    }

    public SubscriptionPlan(CategoryType category, SubscriptionType type, 
    LocalDate startDate, LocalDate renewalDate, int month, int price){
       this(category, type);
       this.startDate = startDate;
       this.renewalDate = renewalDate;
       this.months = month;
       this.price = price;
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public SubscriptionType getType() {
        return type;
    }

    public int getMonths() {
        return months;
    }

    public int getPrice() {
        return price;
    }

    public CategoryType getCategory() {
        return category;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubscriptionPlan other = (SubscriptionPlan) obj;
        if (category != other.category)
            return false;
        if (months != other.months)
            return false;
        if (price != other.price)
            return false;
        if (renewalDate == null) {
            if (other.renewalDate != null)
                return false;
        } else if (!renewalDate.equals(other.renewalDate))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (type != other.type)
            return false;
        return true;
    }


    public String getId() {
        return id;
    }

}
