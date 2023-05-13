package com.example.geektrust.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.geektrust.entities.CategoryType;
import com.example.geektrust.entities.SubscriptionPlan;

public class SubscriptionRepository implements ISubscriptionRepository{
    private final Map<String, SubscriptionPlan> subscriptionMap;
    private Integer autoIncrement = 0;

    public SubscriptionRepository(){
        subscriptionMap = new HashMap<String, SubscriptionPlan>();
    }

    public SubscriptionRepository(Map<String, SubscriptionPlan> subscriptionMap){
        this.subscriptionMap = subscriptionMap;
        this.autoIncrement = subscriptionMap.size();
    }

    @Override
    public Optional<SubscriptionPlan> findByCategory(CategoryType category) {
    
        return subscriptionMap.entrySet().stream().filter(v -> v.getValue().getCategory().equals(category))
        .map(m -> m.getValue()).findFirst();

    }

    @Override
    public long count() {
        return subscriptionMap.size();
    }

    @Override
    public void delete(SubscriptionPlan entity) {
        subscriptionMap.remove(entity.getId(), entity);
    }

    @Override
    public void deleteById(String id) {
        subscriptionMap.remove(id, subscriptionMap.get(id));
    }

    @Override
    public boolean existsById(String id) {
        return subscriptionMap.containsKey(id);
    }

    @Override
    public List<SubscriptionPlan> findAll() {
        return subscriptionMap.entrySet().stream().map(v -> v.getValue()).collect(Collectors.toList());
    }

    @Override
    public Optional<SubscriptionPlan> findById(String id) {
        return subscriptionMap.entrySet().stream()
        .filter(k -> k.getKey().equals(id)).map(v -> v.getValue()).findAny();
    }

    @Override
    public SubscriptionPlan save(SubscriptionPlan subscriptionPlan) {

        if (subscriptionPlan.getId()==null) {
            autoIncrement++;
            subscriptionMap.put(Integer.toString(autoIncrement), subscriptionPlan);
            return subscriptionPlan;
        }
        subscriptionMap.put(subscriptionPlan.getId(), subscriptionPlan);
        return subscriptionPlan;
    }    
    
}
