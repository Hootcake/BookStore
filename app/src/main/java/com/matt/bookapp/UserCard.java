package com.matt.bookapp;

public class UserCard {
    private String ccv, expiry, cardNumber;

    public UserCard(){

    }

    public UserCard(String ccv, String expiry, String cardNumber){
        this.ccv = ccv;
        this.expiry = expiry;
        this.cardNumber = cardNumber;
    }

    public String getCcv(){
        return this.ccv;
    }
    public String getExpiry(){
        return this.expiry;
    }
    public String getCardNumber(){
        return this.cardNumber;
    }

    public void setCcv(String ccv){
        this.ccv = ccv;
    }
    public void setExpiry(String expiry){
        this.expiry = expiry;
    }
    public void setCardNumber(String cardNumber){
        this.cardNumber = cardNumber;
    }
}
