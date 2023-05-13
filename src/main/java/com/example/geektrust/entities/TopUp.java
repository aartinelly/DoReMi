package com.example.geektrust.entities;

public class TopUp {
    private TopUpType type;
    private int months;
    private int price;
    private String id;
    
    public TopUp() {}

    public TopUp(TopUpType type, int months) {
        this.type = type;
        this.months = months;
    }

    public TopUp(TopUpType type, int months, int price) {
        this(type, months);
        this.price = price;
    }

    
    public String getId() {
        return id;
    }

    public TopUpType getType() {
        return type;
    }
   
    public int getMonths() {
        return months;
    }
   
    public int getPrice() {
        return price;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TopUp other = (TopUp) obj;
        if (months != other.months)
            return false;
        if (price != other.price)
            return false;
        if (type != other.type)
            return false;
        return true;
    }   

}
