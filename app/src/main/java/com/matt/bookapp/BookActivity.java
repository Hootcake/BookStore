package com.matt.bookapp;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookActivity extends Activity {
    List<Book> books = new ArrayList<Book>();
    Cart cart = new Cart(books);
    Book currentBook = new Book();

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(com.matt.bookapp.R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem book){
        switch (book.getItemId()){
            case com.matt.bookapp.R.id.editProfile:
                finish();
                Intent intent = new Intent(this, ProfileActivity.class);
                this.startActivity(intent);
                return true;
            case com.matt.bookapp.R.id.listItem:
                finish();
                Intent intent1 = new Intent(this, BookActivity.class);
                this.startActivity(intent1);
                return true;
            case com.matt.bookapp.R.id.logout:
                firebaseAuth.signOut();
                finish();
                Intent intent2 = new Intent(this, LoginActivity.class);
                this.startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(book);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cart cart = new Cart(books);
        cart.setBookList(books);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child("booklist");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectBooks((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        // Set the layout for the layout we created
        setContentView(com.matt.bookapp.R.layout.activity_item);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    private void collectBooks(Map<String,Object> BookList) {

        ArrayList<String> books = new ArrayList<>();
        ArrayList<Book> bookList = new ArrayList<>();
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : BookList.entrySet()){
            Map book = (Map) entry.getValue();
            //Get phone field and append to list
            books.add((String) book.get("name"));
            String bookTitle = (String) book.get("name");
            String bookAuthor = (String) book.get("author");
            String bookCategory = (String) book.get("category");
            double bookPrice = (Double) book.get("price");
            String bookDescription = (String) book.get("description");
            int bookQuantity = (Integer) book.get("quantity");
            Book newBook = new Book(bookTitle, bookAuthor, bookCategory, bookPrice, bookQuantity);
            bookList.add(newBook);
            //Get user map

        }
        LinearLayout layout = (LinearLayout)  findViewById(com.matt.bookapp.R.id.itemLayout);
        ShapeDrawable sd = new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.GRAY);
        sd.getPaint().setStrokeWidth(10f);
        sd.getPaint().setStyle(Paint.Style.STROKE);
        for(Book i : bookList){
            final Book newBook = i;
            TextView view = new TextView(this);
            Button btn = new Button(this);
            view.setBackground(sd);
            view.setPadding(0,10,0,10);
            view.append("\n Book Title: " + i.getTitle().toString());
            view.append("\n Book Author: " + i.getAuthor().toString());
            view.append("\n Category: " + i.getCategory().toString());
            view.append("\n Price: $" + Double.toString(i.getPrice()));
            btn.setText("Add " + i.getTitle().toString() + " to Cart");
            btn.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v){
                    cart.getBookList().add(newBook);
                }
            });
            layout.addView(view);
            layout.addView(btn);

        }

    }
/*
    public void onCheckoutClick(View view){
       final Intent intent = new Intent(this, CartActivity.class);
        if(cart.getBookList().size() == 0){
            Toast.makeText(getApplicationContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
        }
        else{
            intent.putExtra("cart", cart);
            startActivity(intent);
        }
    }
    */

}