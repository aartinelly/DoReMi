package com.example.geektrust.dtos;

import java.util.List;

public class PrintRenewalDetails {
    private List<RenewalReminder> renewalReminders;
    private int amount;

    public PrintRenewalDetails(List<RenewalReminder> renewalReminders, int amount){
        this.renewalReminders = renewalReminders;
        this.amount = amount;
    }

    
    public List<RenewalReminder> getRenewalReminders() {
        return renewalReminders;
    }


    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PrintRenewalDetails other = (PrintRenewalDetails) obj;
        if (amount != other.amount)
            return false;
        if (renewalReminders == null) {
            if (other.renewalReminders != null)
                return false;
        } else if (!renewalReminders.equals(other.renewalReminders))
            return false;
        return true;
    }

}
