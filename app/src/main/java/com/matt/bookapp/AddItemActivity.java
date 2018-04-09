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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItemActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;
    private Button buttonLogout;

    private DatabaseReference databaseReference;
    private EditText editTextCategory, editTextTitle, editTextAuthor, editTextQuantity, editTextPrice;
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
        setContentView(R.layout.activity_add_item);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        editTextCategory = (EditText) findViewById(R.id.editTextCategory);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextAuthor = (EditText) findViewById(R.id.editTextAuthor);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        buttonSave = (Button) findViewById(R.id.buttonSaveBook);
        FirebaseUser user = firebaseAuth.getCurrentUser();
    }

    public void createItem(View view) {
        double priceText = Double.parseDouble(editTextPrice.getText().toString().trim());
        int quantityText = Integer.parseInt(editTextQuantity.getText().toString().trim());
        String authorText = editTextAuthor.getText().toString().trim();
        String categoryText = editTextCategory.getText().toString().trim();
        String titleText = editTextTitle.getText().toString().trim();
        Book book = new Book(titleText, authorText, categoryText, priceText, quantityText);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child("booklist").child(book.getTitle()).setValue(book);
        final Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }
}

