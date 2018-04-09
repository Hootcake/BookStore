package com.matt.bookapp;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    private double total;
    private List<Book> bookList;

    public Cart(List<Book> books){
        this.bookList = books;
    }
    public Cart(double total, List<Book> bookList) {
        this.bookList = bookList;
        this.total = total;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
