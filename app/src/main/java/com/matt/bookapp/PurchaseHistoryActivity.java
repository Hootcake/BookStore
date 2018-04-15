package com.matt.bookapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PurchaseHistoryActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ListView listView;
    private String userID;
    private TextView textViewUserEmail;
    List<Book> books = new ArrayList<Book>();
    Book currentBook = new Book();
    EditText editTextUid;

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(com.matt.bookapp.R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for t he layout we created
        setContentView(R.layout.activity_purchase_history);
        editTextUid = (EditText) findViewById(R.id.editTextUserUid);


    }
/*
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void collectHistory(Map<String, Object> PurchaseHistory) {
        ArrayList<Book> bookList = new ArrayList<>();
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : PurchaseHistory.entrySet()) {
            Map user = (Map) entry.getValue();
            //Get phone field and append to list
            String userEmail = (String) user.get("userInfo");
            long bookPrice = (Long) book.get("price");
            int bookQuantity = Integer.parseInt(Long.toString((Long) book.get("quantity")));
            String bookImage = (String) book.get("imageUri");
            Book newBook = new Book(bookTitle, bookAuthor, bookCategory, Double.parseDouble(Long.toString(bookPrice)), bookQuantity, bookImage);
            bookList.add(newBook);
            //Get user map

        }
        LinearLayout layout = (LinearLayout) findViewById(R.id.historyLayout);
        ShapeDrawable sd = new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.GRAY);
        sd.getPaint().setStrokeWidth(10f);
        sd.getPaint().setStyle(Paint.Style.STROKE);
        for (final Book i : bookList) {
            final Book newBook = i;
            final TextView view = new TextView(this);
            Button btn = new Button(this);
            view.setBackground(sd);
            view.setPadding(0, 10, 0, 10);
            view.append("\n Book Title: " + i.getTitle().toString());
            view.append("\n Book Author: " + i.getAuthor().toString());
            view.append("\n Category: " + i.getCategory().toString());
            view.append("\n Price: $" + Double.toString(i.getPrice()));
            view.append("\n Quantity: " + Integer.toString(i.getQuantity()));
            ImageView iView = new ImageView(this);
            Glide.with(this).load(i.getImageUri()).into(iView);

            btn.setText("Add " + i.getTitle().toString() + " to Cart");
            btn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    cart.getBookList().add(newBook);
                    if (i.getQuantity() > 0) {
                        i.setQuantity(i.getQuantity() - 1);
                        view.setText("");
                        view.append("\n Book Title: " + i.getTitle().toString());
                        view.append("\n Book Author: " + i.getAuthor().toString());
                        view.append("\n Category: " + i.getCategory().toString());
                        view.append("\n Price: $" + Double.toString(i.getPrice()));
                        view.append("\n Quantity: " + Integer.toString(i.getQuantity()));

                    } else
                        Toast.makeText(getApplicationContext(), "Book is Sold Out", Toast.LENGTH_SHORT).show();

                }
            });

            layout.addView(iView);
            layout.addView(view);
            layout.addView(btn);

        }
    }*/

    public void purchaseHistory(View view) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        final List<ArrayList<Book>> bookList = new ArrayList<>();
        ref.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final TextView view = new TextView(getApplicationContext());
                    //Get map of users in datasnapshot
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String uid = snapshot.getKey();
                        if(uid.equals(editTextUid.getText().toString())){
                            String userEmail = (String) snapshot.child("userInfo").child("name").getValue().toString();
                            String userPhone = (String) snapshot.child("userInfo").child("phone").getValue().toString();
                            for (DataSnapshot purchaseHistory : snapshot.child("purchaseHistory").getChildren()) {
                                ArrayList<Book> ordersBooks = new ArrayList<>();
                                for (DataSnapshot purchase : purchaseHistory.getChildren()) {
                                    String bookTitle = (String) purchase.child("title").getValue().toString();
                                    String bookAuthor = (String) purchase.child("author").getValue().toString();
                                    String bookCategory = (String) purchase.child("category").getValue().toString();
                                    long bookPrice = (Long) purchase.child("price").getValue();
                                    Book newBook = new Book(bookTitle, bookAuthor, bookCategory, Double.parseDouble(Long.toString(bookPrice)), 0, "");
                                    ordersBooks.add(newBook);
                                }
                                bookList.add(ordersBooks);
                            }

                            LinearLayout layout = (LinearLayout) findViewById(R.id.historyLayout);
                            layout.removeAllViews();

                            Button btn = new Button(getApplicationContext());
                            final TextView bookView = new TextView(getApplicationContext());
                            bookView.setPadding(0, 10, 0, 10);
                            int i = 0;
                            bookView.append("\n\n User's Name: " + userEmail);
                            for (final ArrayList<Book> order : bookList) {
                                i++;
                                bookView.append("\nOrder Number: " + i + "\n");
                                for(Book book: order){
                                    bookView.append("\n Book Title:" + book.getTitle().toString());
                                    bookView.append("\n Book Author: " + book.getAuthor().toString());
                                    bookView.append("\n Category: " + book.getCategory().toString());
                                    bookView.append("\n Price: $" + Double.toString(book.getPrice()));
                                }
                            }
                                layout.addView(bookView);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }
            });

    }
}
