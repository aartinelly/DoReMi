package com.example.geektrust.repositories;

import java.util.Optional;

import com.example.geektrust.entities.CategoryType;
import com.example.geektrust.entities.SubscriptionPlan;

public interface ISubscriptionRepository extends CRUDRepository<SubscriptionPlan, String>{
    public Optional<SubscriptionPlan> findByCategory(CategoryType category); 
}
