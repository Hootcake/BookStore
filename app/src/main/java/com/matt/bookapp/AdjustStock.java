package com.matt.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdjustStock extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
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
        setContentView(R.layout.activity_adjust_stock);
    }


    public void adjustStock(View view) {
        final EditText stock = (EditText) findViewById(R.id.newStockLevel);
        final EditText title = (EditText) findViewById(R.id.itemTitle);

        if(stock != null && title != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       if (dataSnapshot.child("booklist").child(title.getText().toString()).exists()) {
                            dataSnapshot.child("booklist").child(title.getText()
                                .toString()).child("quantity").getRef()
                                .setValue(Integer.parseInt(stock.getText().toString()));
                           Toast.makeText(AdjustStock.this, "Quantity Updated", Toast.LENGTH_SHORT).show();
                         }


                        }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
            );

        }
    }
}
