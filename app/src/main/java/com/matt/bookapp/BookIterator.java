package com.matt.bookapp;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class BookIterator {
    private List<Book> list = new ArrayList<>();

    public class Iterator {
        private BookIterator bi;
        private java.util.Iterator iterator;
        private Book value;
        private int i;

        public Iterator(BookIterator iter) {
            bi = iter;
        }

        public void first() {
            iterator = bi.list.iterator();
            next();
        }

        public void next() {
            try {
                value = (Book)iterator.next();
            } catch (NoSuchElementException ex) {
                i =  -1;
            }
        }

        public boolean isDone() {
            return i == -1;
        }

        public Book currentValue() {
            return value;
        }
    }

    public void add(Book book) {
        list.add(book);
    }

    public Iterator getIterator() {
        return new Iterator(this);
    }
}
