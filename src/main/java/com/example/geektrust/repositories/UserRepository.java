package com.example.geektrust.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.geektrust.entities.User;

public class UserRepository implements CRUDRepository<User, String>{
    private final Map<String,User> userMap;

    public UserRepository() {
        this.userMap = new HashMap<String, User>();
        String id = "1";
        this.userMap.put(id, new User());
    }

    @Override
    public long count() {
        return userMap.size();
    }

    @Override
    public void delete(User entity) {
        userMap.remove(entity.getId(), entity);
    }

    @Override
    public void deleteById(String id) {
        userMap.remove(id, userMap.get(id));
    }

    @Override
    public boolean existsById(String id) {
        return userMap.entrySet().stream().anyMatch(k -> k.getKey().equals(id));
    }

    @Override
    public List<User> findAll() {
       return userMap.entrySet().stream().map(v-> v.getValue())
       .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(String id) {
       return userMap.entrySet().stream().filter(k -> k.getKey().equals(id))
        .map(v -> v.getValue()).findAny();
    }

    @Override
    public User save(User user) {
        String id = "1";
        userMap.put(id, user);
        return userMap.get(id);
    }

}
