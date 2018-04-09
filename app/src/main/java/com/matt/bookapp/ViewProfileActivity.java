package com.matt.bookapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewProfileActivity extends AppCompatActivity{

    private static final String TAG = "ViewDatabase";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private ListView listView;
    private String userID;
    private TextView textViewUserEmail;

    //private Button buttonLogout, buttonEditProfile, buttonItem;
    //private TextView editTextName, editTextAddress, editTextCity, editTextCountry, editTextPhone;


    @Override
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
        setContentView(com.matt.bookapp.R.layout.activity_view_profile);

        listView = (ListView) findViewById(com.matt.bookapp.R.id.listview);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textViewUserEmail = (TextView) findViewById(com.matt.bookapp.R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome, " + user.getEmail());

    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation userInf = new UserInformation();

            userInf.setName(ds.child(userID).child("userInfo").getValue(UserInformation.class).getName());
            userInf.setAddress(ds.child(userID).child("userInfo").getValue(UserInformation.class).getAddress());
            userInf.setCountry(ds.child(userID).child("userInfo").getValue(UserInformation.class).getCountry());
            userInf.setCity(ds.child(userID).child("userInfo").getValue(UserInformation.class).getCity());
            userInf.setPhone(ds.child(userID).child("userInfo").getValue(UserInformation.class).getPhone());

            Log.d(TAG, "show Data: name " + userInf.getName());
            Log.d(TAG, "show Data: Address " + userInf.getAddress());
            Log.d(TAG, "show Data: Country " + userInf.getCountry());
            Log.d(TAG, "show Data: City " + userInf.getCity());
            Log.d(TAG, "show Data: Phone " + userInf.getPhone());

            ArrayList<String> array = new ArrayList<>();
            array.add(userInf.getName());
            array.add(userInf.getAddress());
            array.add(userInf.getCountry());
            array.add(userInf.getCity());
            array.add(userInf.getPhone());

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, array);
            listView.setAdapter(adapter);

        }
    }

}
