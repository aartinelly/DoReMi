package com.example.geektrust.serviceTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.geektrust.dtos.PrintRenewalDetails;
import com.example.geektrust.dtos.RenewalReminder;
import com.example.geektrust.entities.CategoryType;
import com.example.geektrust.entities.Constants;
import com.example.geektrust.entities.SubscriptionPlan;
import com.example.geektrust.entities.SubscriptionType;
import com.example.geektrust.entities.TopUp;
import com.example.geektrust.entities.TopUpType;
import com.example.geektrust.entities.User;
import com.example.geektrust.exceptions.AddSubscriptionFailedException;
import com.example.geektrust.exceptions.InvalidDateException;
import com.example.geektrust.exceptions.NoSubscriptionFoundException;
import com.example.geektrust.repositories.ISubscriptionRepository;
import com.example.geektrust.repositories.UserRepository;
import com.example.geektrust.services.SubscriptionService;

@DisplayName("SubscriptionServiceTest")
@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private SubscriptionService subscriptionService;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        System.setOut(new PrintStream(outputStreamCaptor));
        subscriptionService = new SubscriptionService(subscriptionRepository, userRepository);
    }

    @DisplayName("startSubscription method should return local date if given valid input")
    @Test
    public void startSubscription_ShouldReturnLocalDate_GivenValidInput(){
        String startDate = "20-02-2022";

        String expectedDate = "20-02-2022";
        String actualDate = subscriptionService.startSubscription(startDate);

        Assertions.assertEquals(expectedDate, actualDate);

    }

    @DisplayName("startSubscription method should throw exception if given invalid input")
    @Test
    public void startSubscription_ShouldThrowException_GivenInValidInput(){
        String startDate = "07-19-2022";

        Assertions.assertThrows(InvalidDateException.class, () -> subscriptionService.startSubscription(startDate));

    }

    @DisplayName("addSubscription method should return subscription plan if given category and plan type")
    @Test
    public void addSubscription_ShouldReturnSubscriptionPlan_GivenCategoryAndPlanType(){
        
        String startD = "20-02-2022";
        String renewalD = "10-03-2022";
        LocalDate startDate = LocalDate.parse(startD, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate renewalDate = LocalDate.parse(renewalD, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        
        SubscriptionPlan expected = new SubscriptionPlan(CategoryType.MUSIC, SubscriptionType.PERSONAL, startDate, renewalDate, Constants.oneMonth, Constants.musicPersonal);
        String id ="1";
        User expectedUser = new User(id);

        when(subscriptionRepository.findByCategory(CategoryType.MUSIC)).thenReturn(Optional.empty());
        when(subscriptionRepository.save(any())).thenReturn(expected);
        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        // expectedUser.addSubscriptionPlan(expected);
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);
        subscriptionService.setStartDate(startDate);
        subscriptionService.setValidDate(true);
        SubscriptionPlan actual = subscriptionService.addSubscription("MUSIC", "PERSONAL");

        Assertions.assertEquals(expected, actual);
    }


    @DisplayName("addSubscription method should throw error if given invalid date")
    @Test
    public void addSubscription_ShouldThrowError_GivenInvalidDate(){
        subscriptionService.setValidDate(false);

        Assertions.assertThrows(AddSubscriptionFailedException.class, () -> {
            subscriptionService.addSubscription("MUSIC", "PERSONAL");
        });

    }

    @DisplayName("addSubscription method should throw error if given duplicate Subscription plan")
    @Test
    public void addSubscription_ShouldThrowError_GivenDuplicateSubscription(){
        SubscriptionPlan sp = new SubscriptionPlan(CategoryType.MUSIC, SubscriptionType.PERSONAL);
        subscriptionService.setValidDate(true);
        when(subscriptionRepository.findByCategory(CategoryType.MUSIC)).thenReturn(Optional.of(sp));

        Assertions.assertThrows(AddSubscriptionFailedException.class, () -> {
            subscriptionService.addSubscription("MUSIC", "PERSONAL");
        });

    }

    @DisplayName("print method should return PrintRenewalDetails object")
    @Test
    public void print_shouldReturnPrintRenewalDetails_OnCallingPrintMethod(){
        String sDate = "20-02-2022";
        LocalDate startDate = LocalDate.parse(sDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        
        SubscriptionPlan sp1 = new SubscriptionPlan(CategoryType.MUSIC, SubscriptionType.PERSONAL, 
        startDate, LocalDate.parse("2022-03-10"), Constants.oneMonth, Constants.musicPersonal);
        SubscriptionPlan sp2 = new SubscriptionPlan(CategoryType.VIDEO, SubscriptionType.PREMIUM, 
        startDate, LocalDate.parse("2022-03-10"), Constants.threeMonth, Constants.videoPremium);

        List<SubscriptionPlan> expectedList = new ArrayList<>();
        expectedList.add(sp1);
        expectedList.add(sp2);

        int price = Constants.topUpFourDevice * Constants.threeMonth;
        TopUp topUp = new TopUp(TopUpType.FOUR_DEVICE, Constants.threeMonth, price);

        User user = new User(new ArrayList<>(), topUp);
        when(subscriptionRepository.findAll()).thenReturn(expectedList);
        String id = "1";
        when(userRepository.findById(id)).thenReturn(Optional.of(user));


        //mock renewal details
        RenewalReminder rr1 = new RenewalReminder(CategoryType.MUSIC, "10-03-2022");
        RenewalReminder rr2 = new RenewalReminder(CategoryType.VIDEO, "10-03-2022");
        List<RenewalReminder> list = Arrays.asList(rr1, rr2);

        int amount = 750;
        PrintRenewalDetails expectedPrintRenewalDetails = new PrintRenewalDetails(list, amount);

        PrintRenewalDetails actualRenewalDetails = subscriptionService.print();

        Assertions.assertEquals(expectedPrintRenewalDetails, actualRenewalDetails);

    }

    @DisplayName("print method should throw NoSubscriptionFoundException when Subsciption list is empty")
    @Test
    public void print_shouldThrowNoSubscriptionFoundException_OnCallingPrintMethod(){
        
        when(subscriptionRepository.findAll()).thenReturn(new ArrayList<>());

        Assertions.assertThrows(NoSubscriptionFoundException.class, () -> {
            subscriptionService.print();
        });
        

    }


    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
