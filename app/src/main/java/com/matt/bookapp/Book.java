package com.matt.bookapp;
import java.io.Serializable;
public class Book implements Serializable{

    private static final long serialVersionUID = 1L;
    private String title, author, category;
    private double price;
    private int quantity;
    private String imageUri;

    public Book(){

    }

    public Book(String title, String author, String category, double price, int quantity, String imageUri){
        this.title = title;
        this.author = author;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.imageUri = imageUri;
    }

    public String getCategory(){
        return category;
    }

    public String getTitle(){
        return title;
    }

    public double getPrice(){
        return price;
    }

    public String getAuthor(){
        return author;
    }

    public int getQuantity() { return quantity; }

    public String getImageUri() { return imageUri; }

    public void setTitle(String title){
        this.title = title;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setImageUri(String imageUri){this.imageUri = imageUri;}
}
