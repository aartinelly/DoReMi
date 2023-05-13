package com.example.geektrust.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.geektrust.dtos.PrintRenewalDetails;
import com.example.geektrust.dtos.RenewalReminder;
import com.example.geektrust.entities.CategoryType;
import com.example.geektrust.entities.Constants;
import com.example.geektrust.entities.ErrorMessages;
import com.example.geektrust.entities.SubscriptionPlan;
import com.example.geektrust.entities.SubscriptionType;
import com.example.geektrust.entities.User;
import com.example.geektrust.exceptions.AddSubscriptionFailedException;
import com.example.geektrust.exceptions.InvalidDateException;
import com.example.geektrust.exceptions.NoSubscriptionFoundException;
import com.example.geektrust.repositories.ISubscriptionRepository;
import com.example.geektrust.repositories.UserRepository;

public class SubscriptionService implements ISubscriptionService{
    private ISubscriptionRepository subscriptionRepository;
    private UserRepository userRepository;
    private LocalDate startDate;

    private static boolean isValidDate; 

    public SubscriptionService(ISubscriptionRepository subscriptionRepository,
            UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    public void setStartDate(LocalDate date){
        this.startDate = date;
    }

    public static boolean isValidDate() {
        return isValidDate;
    }

    public static void setValidDate(boolean isValidDate) {
        SubscriptionService.isValidDate = isValidDate;
    }

    @Override
    public String startSubscription(String date) throws InvalidDateException{
        String formatType = "dd-MM-yyyy";
       try{
        startDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(formatType));
        isValidDate = true;
       }
       catch(DateTimeParseException e){
            isValidDate = false;
            throw new InvalidDateException(ErrorMessages.INVALID_DATE.toString());
       }
      
        return DateTimeFormatter.ofPattern(formatType).format(startDate);
        
    }


    @Override
    public SubscriptionPlan addSubscription(String category, String planName) throws AddSubscriptionFailedException{
        if (!isValidDate) {
            throw new AddSubscriptionFailedException(ErrorMessages.ADD_SUBSCRIPTION_FAILED.toString()+" "+ErrorMessages.INVALID_DATE.toString());
        }
        CategoryType cType = null; 
        SubscriptionType sType = null;
        // set categorty as CategoryType
        
        switch(category){
            case "MUSIC": cType = CategoryType.MUSIC;
            break;
            case "VIDEO": cType = CategoryType.VIDEO;
            break;
            case "PODCAST": cType = CategoryType.PODCAST;
            break;
        }

        //set planName as SubscriptionType
        switch(planName){
            case "FREE": sType = SubscriptionType.FREE;
            break;
            case "PERSONAL": sType = SubscriptionType.PERSONAL;
            break;
            case "PREMIUM": sType = SubscriptionType.PREMIUM;
            break;
        }

        //check for duplicate subscription
        Optional<SubscriptionPlan> sp = subscriptionRepository.findByCategory(cType);
        if(sp.isPresent()) throw new AddSubscriptionFailedException(ErrorMessages.ADD_SUBSCRIPTION_FAILED.toString()+" "+ErrorMessages.DUPLICATE_CATEGORY.toString());
       
        //set renewal date based on category and plan
       SubscriptionPlan subscriptionPlan = setRenewalDate(cType, sType);
        
        subscriptionRepository.save(subscriptionPlan);
        String id = "1";
        User user = userRepository.findById(id).get();
        user.addSubscriptionPlan(subscriptionPlan);
        userRepository.save(user);

        return subscriptionPlan;
    }

    private SubscriptionPlan setRenewalDate(CategoryType cType, SubscriptionType sType){
        
        LocalDate oneMonthRenewalDate = setRenewalDateForGivenMonth(startDate, Constants.oneMonth);
        LocalDate threeMonthRenewalDate = setRenewalDateForGivenMonth(startDate, Constants.threeMonth);
        
        SubscriptionPlan subscriptionPlan = null;
        //Music
        if (sType.equals(SubscriptionType.FREE)) {
            subscriptionPlan = new SubscriptionPlan(cType, sType, startDate, oneMonthRenewalDate, Constants.oneMonth, 0);
           
        }

        else if((cType.equals(CategoryType.MUSIC) 
        && sType.equals(SubscriptionType.PERSONAL)) || (cType.equals(CategoryType.PODCAST) 
        && sType.equals(SubscriptionType.PERSONAL))){
            subscriptionPlan = new SubscriptionPlan(cType, sType, startDate, oneMonthRenewalDate, Constants.oneMonth, Constants.musicPersonal);
        }

        else if(cType.equals(CategoryType.MUSIC) 
        && sType.equals(SubscriptionType.PREMIUM)){
           subscriptionPlan = new SubscriptionPlan(cType, sType, startDate, threeMonthRenewalDate, Constants.threeMonth, Constants.musicPremium);
        }

        //video
        else if(cType.equals(CategoryType.VIDEO) 
        && sType.equals(SubscriptionType.PERSONAL)){
            subscriptionPlan = new SubscriptionPlan(cType, sType, startDate, oneMonthRenewalDate, Constants.oneMonth, Constants.videoPersonal);
        }
        else if(cType.equals(CategoryType.VIDEO) 
        && sType.equals(SubscriptionType.PREMIUM)){
           subscriptionPlan = new SubscriptionPlan(cType, sType, startDate, threeMonthRenewalDate, Constants.threeMonth, Constants.videoPremium);
        }

        else if(cType.equals(CategoryType.PODCAST) 
        && sType.equals(SubscriptionType.PREMIUM)){
           subscriptionPlan = new SubscriptionPlan(cType, sType, startDate, threeMonthRenewalDate, Constants.threeMonth, Constants.podcastPremium);
        }
        return subscriptionPlan;
    
    }

    private LocalDate setRenewalDateForGivenMonth(LocalDate startDate, int months){
        
        // LocalDate sDate = subscriptionPlan.getStartDate();
        int daysToSubtract = 10;

        if (months==Constants.oneMonth) {
            return startDate.plusMonths(Constants.oneMonth).minusDays(daysToSubtract);
    
        }
        else{
            return startDate.plusMonths(Constants.threeMonth).minusDays(daysToSubtract);
           
        }
            
    }

    @Override
    public PrintRenewalDetails print() throws AddSubscriptionFailedException{
        //get all subscription list
        List<SubscriptionPlan> sp = subscriptionRepository.findAll();
        int totalAmount = 0;
        //if subscription list is empty then throw error 
        if(sp.size()==0) throw new NoSubscriptionFoundException(ErrorMessages.SUBSCRIPTIONS_NOT_FOUND.toString());

        List<RenewalReminder> rr = new ArrayList<>();
        
        for (SubscriptionPlan subscriptionPlan : sp) {
            String renewalDate = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(subscriptionPlan.getRenewalDate());
            rr.add(new RenewalReminder(subscriptionPlan.getCategory(), renewalDate));
            totalAmount += subscriptionPlan.getPrice();
        }

        String id = "1";
        User user = userRepository.findById(id).get();
        if(user.getTopUp()!=null){
            totalAmount += user.getTopUp().getPrice();
        }
        
        PrintRenewalDetails printRenewalDetails = new PrintRenewalDetails(rr, totalAmount);
        return printRenewalDetails;
    }

}
