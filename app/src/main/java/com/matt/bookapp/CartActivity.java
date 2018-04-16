package com.matt.bookapp;

import android.app.Activity;
import android.content.ClipData;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by matth on 14/12/2017.
 */

public class CartActivity extends Activity {
        public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(com.matt.bookapp.R.menu.main_menu, menu);
        return true;
        }
        private FirebaseAuth firebaseAuth;

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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Intent intent = getIntent();
        final Cart cart = (Cart) intent.getSerializableExtra("cart");
        List<Book> cartItems = cart.getBookList();
        ShapeDrawable sd = new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.GRAY);
        sd.getPaint().setStrokeWidth(10f);
        sd.getPaint().setStyle(Paint.Style.STROKE);
        LinearLayout layout = (LinearLayout) findViewById(R.id.checkoutList);
        TextView total = (TextView) findViewById(R.id.basketTotal);
        double basketTotal = 0.00;
        for (final Book book : cartItems) {
            final TextView tvTotal = total;
            final LinearLayout currentLayout = layout;
            final Book currentItem = book;
            final EditText comment = new EditText(this);
            final RatingBar rating = new RatingBar(this);
            Button submitComment = new Button(this);
            final TextView view = new TextView(this);
            Button btn = new Button(this);
            view.setBackground(sd);
            view.setPadding(0, 10, 0, 10);
            view.append("\n Item Name: " + book.getTitle());
            view.append("\n Item Price: " + book.getPrice());
            basketTotal += book.getPrice();
            final double currentTotal = basketTotal;
            layout.addView(view);
            btn.setText("Remove " + book.getTitle().toString() + " from Cart");
            layout.addView(btn);
            layout.addView(comment);
            layout.addView(rating);
            layout.addView(submitComment);
            final Button currentBtn = btn;
            final TextView currentView = view;
            btn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    cart.getBookList().remove(currentItem);
                    currentLayout.removeView(currentBtn);
                    currentLayout.removeView(currentView);
                    double newTotal = currentTotal - currentItem.getPrice();
                    tvTotal.setText("Total Cost:            $" + newTotal);
                    Toast.makeText(getApplicationContext(), currentItem.getTitle() + " Removed from cart", Toast.LENGTH_SHORT).show();
                    discountBookCount(cart, newTotal);
                }
            });
            submitComment.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v){;
                    if(book.getComments()==null){
                        List<Comments> comments = new ArrayList<Comments>();
                        book.setComments(comments);
                    }
                    Comments c = new Comments(comment.getText().toString(), rating.getNumStars());
                    book.getComments().add(c);
                }
            });
        }
        total.setText("Total Cost:          $" + basketTotal+'0');
        discountBookCount(cart, basketTotal);
    }

    private void discountBookCount(Cart cart, double total) {
        TextView basketTotal = (TextView) findViewById(R.id.basketTotal);
        int size = cart.getBookList().size();
        if(size >= 10){
            total = total * .9;
            basketTotal.setText("Total Cost:          $" + total+'0');
        }
    }

    public void onPaymentClick(View view) {
        Intent intent2 = getIntent();
        final Cart cart = (Cart) intent2.getSerializableExtra("cart");
        List<Book> cartItems = cart.getBookList();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        for (final Book book : cartItems) {
            databaseReference.child("booklist").child(book.getKey()).setValue(book);
        }
        databaseReference.child("users").child(user.getUid()).child("purchaseHistory").child(randomString()).setValue(cartItems);
        final Intent intent = new Intent(this, HomePageActivity.class);

        startActivity(intent);
    }

    protected String randomString() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) {
            int index = (int) (rnd.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
