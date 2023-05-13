package com.example.geektrust.serviceTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.geektrust.entities.CategoryType;
import com.example.geektrust.entities.Constants;
import com.example.geektrust.entities.SubscriptionPlan;
import com.example.geektrust.entities.SubscriptionType;
import com.example.geektrust.entities.TopUp;
import com.example.geektrust.entities.TopUpType;
import com.example.geektrust.entities.User;
import com.example.geektrust.exceptions.AddTopUpFailedException;
import com.example.geektrust.repositories.UserRepository;
import com.example.geektrust.services.SubscriptionService;
import com.example.geektrust.services.UserService;

@DisplayName("UserServiceTest")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        System.setOut(new PrintStream(outputStreamCaptor));
        userService =  new UserService(userRepository);
    }

    @DisplayName("addTopUp method should return TopUp Object if given Topup name and month")
    @Test
    public void addTopUp_ShouldReturnTopUp_GivenTopUpNameAndMonth(){
        int price = Constants.topUpFourDevice * Constants.threeMonth;
        TopUp expectedTopUp = new TopUp(TopUpType.FOUR_DEVICE, Constants.threeMonth, price);

        SubscriptionService.setValidDate(true);
        User user = new User();
        SubscriptionPlan sp = new SubscriptionPlan(CategoryType.MUSIC, SubscriptionType.PERSONAL);
        user.addSubscriptionPlan(sp);
        
        // String id ="1";
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        
        TopUp actualTopUp = userService.addTopUp("FOUR_DEVICE", Constants.threeMonth);
        
        Assertions.assertEquals(expectedTopUp, actualTopUp);
    }

    @DisplayName("addTopUp method should return TopUp Object if given Topup name and month")
    @Test
    public void addTopUp_ShouldThrowAddTopUpFailedException_GivenTopUpNameAndMonthIfSubscriptionNotFound(){
       
        User user = new User();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        
        SubscriptionService.setValidDate(true);
        Assertions.assertThrows(AddTopUpFailedException.class, () -> {
            userService.addTopUp("FOUR_DEVICE", Constants.topUpFourDevice);
        });
    }

    @DisplayName("addTopUp method should return TopUp Object if given Topup name and month")
    @Test
    public void addTopUp_ShouldThrowAddTopUpFailedException_GivenDuplicateTopUp(){
       
        int price = Constants.topUpFourDevice * Constants.threeMonth;
        TopUp expectedTopUp = new TopUp(TopUpType.FOUR_DEVICE, Constants.threeMonth, price);
        SubscriptionService.setValidDate(true);
        SubscriptionPlan sp = new SubscriptionPlan(CategoryType.MUSIC, SubscriptionType.PERSONAL);
    
        User user = new User(Arrays.asList(sp), expectedTopUp);
       
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        
        Assertions.assertThrows(AddTopUpFailedException.class, () -> {
            userService.addTopUp("FOUR_DEVICE", Constants.threeMonth);
        });
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
