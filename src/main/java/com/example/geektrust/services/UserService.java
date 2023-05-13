package com.example.geektrust.services;

import java.util.List;

import com.example.geektrust.entities.Constants;
import com.example.geektrust.entities.ErrorMessages;
import com.example.geektrust.entities.SubscriptionPlan;
import com.example.geektrust.entities.TopUp;
import com.example.geektrust.entities.TopUpType;
import com.example.geektrust.entities.User;
import com.example.geektrust.exceptions.AddTopUpFailedException;
import com.example.geektrust.repositories.UserRepository;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public TopUp addTopUp(String topUpName, int months) throws AddTopUpFailedException{
        //check if there is subscription by startDate, if yes then add topup else throw exception
            TopUpType topUpType = null;
            if(topUpName.equals("FOUR_DEVICE")) topUpType = TopUpType.FOUR_DEVICE;
            if(topUpName.equals("TEN_DEVICE")) topUpType = TopUpType.TEN_DEVICE;
    
            //throw error if invalid date
            if(!SubscriptionService.isValidDate()){
                throw new AddTopUpFailedException(ErrorMessages.ADD_TOPUP_FAILED.toString()+" "+ErrorMessages.INVALID_DATE.toString());
            }

            //throw error if subscription does not exists
            String id = "1";
            User user = userRepository.findById(id).get();
            
            if (user.getSubscriptionPlan().isEmpty()) {
                throw new AddTopUpFailedException(ErrorMessages.ADD_TOPUP_FAILED.toString()+" "+ErrorMessages.SUBSCRIPTIONS_NOT_FOUND.toString());    
            }
            //throw error if there is duplicate topup 
            if (user.getTopUp()!=null) {
                throw new AddTopUpFailedException(ErrorMessages.ADD_TOPUP_FAILED.toString()+" "+ErrorMessages.DUPLICATE_TOPUP.toString());
            }

            //save topup
            TopUp topup = null;
            int price = 0;
            if(topUpType.equals(TopUpType.FOUR_DEVICE)){
                price = months * Constants.topUpFourDevice;
               topup = new TopUp(topUpType, months, price);
            }
            else{
               price =  months * Constants.topUpTenDevice;
               topup = new TopUp(topUpType, months, price);
            }


            // user.setTopUp(topup);
            List<SubscriptionPlan> sp = user.getSubscriptionPlan();
            user = new User(sp, topup);
            userRepository.save(user);
            return topup;
        }
    

}
