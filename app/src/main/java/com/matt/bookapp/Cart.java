package com.matt.bookapp;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    private double total;
    private List<Item> itemList;

    public Cart(List<Item> items){
        this.itemList = items;
    }
    public Cart(double total, List<Item> itemList) {
        this.itemList = itemList;
        this.total = total;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
