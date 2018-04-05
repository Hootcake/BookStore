package com.matt.bookapp;
import java.io.Serializable;
public class Item implements Serializable{

    private static final long serialVersionUID = 1L;
    private String name;
    private long price;
    private String description;


    public Item(){

    }

    public Item(String name, long price, String description){
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName(){
        return name;
    }

    public long getPrice(){
        return price;
    }

    public String getDescription(){
        return description;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(long price){
        this.price = price;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
