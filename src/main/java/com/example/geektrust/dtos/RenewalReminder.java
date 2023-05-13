package com.example.geektrust.dtos;

import com.example.geektrust.entities.CategoryType;

public class RenewalReminder {
    private CategoryType categoryType;
    private String renewalDate;

    public RenewalReminder(CategoryType categoryType, String renewalDate){
        this.categoryType = categoryType;
        this.renewalDate = renewalDate;
    }
    
    public CategoryType getCategoryType() {
        return categoryType;
    }

    public String getRenewalDate() {
        return renewalDate;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RenewalReminder other = (RenewalReminder) obj;
        if (categoryType != other.categoryType)
            return false;
        if (renewalDate == null) {
            if (other.renewalDate != null)
                return false;
        } else if (!renewalDate.equals(other.renewalDate))
            return false;
        return true;
    }  

}
