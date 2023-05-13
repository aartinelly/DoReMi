package com.example.geektrust;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import com.example.geektrust.entities.ErrorMessages;

@DisplayName("App Test")
public class MainTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    
    
    @Test
    @DisplayName("App Integration Test 1")
    public void Application_Test() throws Exception{
        // Arrange
        String[] arguments = new String[]{"sample_input/input2.txt"};
        
        // Act
        Main.main(arguments);   
        // Assert
        String[] str = outputStreamCaptor.toString().trim().split("\n");
        List<String> l = Arrays.asList(str);
       
        Assertions.assertEquals("INVALID_DATE", l.get(0).trim());
        Assertions.assertEquals("ADD_SUBSCRIPTION_FAILED INVALID_DATE", l.get(1).trim());
        Assertions.assertEquals("SUBSCRIPTIONS_NOT_FOUND", l.get(2).trim());
        Assertions.assertEquals("RENEWAL_REMINDER MUSIC 10-03-2022", l.get(3).trim());
        Assertions.assertEquals("RENEWAL_REMINDER VIDEO 10-05-2022", l.get(4).trim());
        Assertions.assertEquals("RENEWAL_REMINDER PODCAST 10-03-2022", l.get(5).trim());
        Assertions.assertEquals("RENEWAL_AMOUNT 750", l.get(6).trim());
        Assertions.assertEquals("ADD_SUBSCRIPTION_FAILED DUPLICATE_CATEGORY", l.get(7).trim());
        Assertions.assertEquals("ADD_TOPUP_FAILED DUPLICATE_TOPUP", l.get(8).trim());
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

}