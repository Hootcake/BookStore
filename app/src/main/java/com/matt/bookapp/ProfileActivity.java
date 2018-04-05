package com.matt.bookapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;
    private Button buttonLogout;

    private DatabaseReference databaseReference;
    private EditText editTextName, editTextAddress, editTextCity, editTextCountry, editTextPhone;
    private Button buttonSave, buttonViewProfile;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(com.matt.bookapp.R.menu.menu_profile_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case com.matt.bookapp.R.id.viewProfile:
                finish();
                Intent intent = new Intent(this, ViewProfileActivity.class);
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
        setContentView(com.matt.bookapp.R.layout.activity_profile);


        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        editTextName = (EditText) findViewById(com.matt.bookapp.R.id.editTextName);
        editTextAddress = (EditText) findViewById(com.matt.bookapp.R.id.editTextAddress);
        editTextCountry = (EditText) findViewById(com.matt.bookapp.R.id.editTextCountry);
        editTextCity = (EditText) findViewById(com.matt.bookapp.R.id.editTextCity);
        editTextPhone = (EditText) findViewById(com.matt.bookapp.R.id.editTextPhone);
        buttonSave = (Button) findViewById(com.matt.bookapp.R.id.buttonSave);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(com.matt.bookapp.R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome, " + user.getEmail());

        //buttonLogout = (Button) findViewById(R.id.buttonLogout);
        // buttonViewProfile = (Button) findViewById(R.id.buttonViewProfile);
        // buttonLogout.setOnClickListener(this);

        buttonSave.setOnClickListener(this);

        // buttonViewProfile.setOnClickListener(this);
    }

    public void saveUser(){
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String country = editTextCountry.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        UserInformation userInformation = new UserInformation(name, address, country, city, phone);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child("users").child(user.getUid()).setValue(userInformation);
        Toast.makeText(this, "Information Saved ...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        if(v == buttonSave){
            saveUser();
        }
    }
}
