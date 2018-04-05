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

public class ItemActivity extends Activity {
    List<Item> items = new ArrayList<Item>();
    Cart cart = new Cart(items);
    Item currentItem = new Item();

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(com.matt.bookapp.R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case com.matt.bookapp.R.id.editProfile:
                finish();
                Intent intent = new Intent(this, ProfileActivity.class);
                this.startActivity(intent);
                return true;
            case com.matt.bookapp.R.id.listItem:
                finish();
                Intent intent1 = new Intent(this, ItemActivity.class);
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
        Cart cart = new Cart(items);
        cart.setItemList(items);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child("ItemList");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectItems((Map<String,Object>) dataSnapshot.getValue());
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
    private void collectItems(Map<String,Object> ItemList) {

        ArrayList<String> items = new ArrayList<>();
        ArrayList<Item> itemList = new ArrayList<>();
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : ItemList.entrySet()){
            Map item = (Map) entry.getValue();
            //Get phone field and append to list
            items.add((String) item.get("name"));
            String itemName = (String) item.get("name");
            long itemPrice = (Long) item.get("price");
            String itemDescription = (String) item.get("description");
            Item newItem = new Item(itemName, itemPrice, itemDescription);
            itemList.add(newItem);
            //Get user map

        }
        LinearLayout layout = (LinearLayout)  findViewById(com.matt.bookapp.R.id.itemLayout);
        ShapeDrawable sd = new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.GRAY);
        sd.getPaint().setStrokeWidth(10f);
        sd.getPaint().setStyle(Paint.Style.STROKE);
        for(Item i : itemList){
            final Item newItem = i;
            TextView view = new TextView(this);
            Button btn = new Button(this);
            view.setBackground(sd);
            view.setPadding(0,10,0,10);
            view.append("\n Item Name: " + i.getName().toString());
            view.append("\n Item Description: " + i.getDescription().toString());
            view.append("\n Price: $" + Long.toString(i.getPrice()));
            btn.setText("Add " + i.getName().toString() + " to Cart");
            btn.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v){
                    cart.getItemList().add(newItem);
                    Toast.makeText(getApplicationContext(), newItem.getName() + " Added to cart", Toast.LENGTH_SHORT).show();
                }
            });
            layout.addView(view);
            layout.addView(btn);

        }

    }
/*
    public void onCheckoutClick(View view){
       final Intent intent = new Intent(this, CartActivity.class);
        if(cart.getItemList().size() == 0){
            Toast.makeText(getApplicationContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
        }
        else{
            intent.putExtra("cart", cart);
            startActivity(intent);
        }
    }
    */

}
