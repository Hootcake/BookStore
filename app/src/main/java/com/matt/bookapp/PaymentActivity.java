package com.matt.bookapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by matth on 14/12/2017.
 */

public class PaymentActivity extends Activity implements View.OnClickListener {

    final boolean[] click = new boolean[1];
    EditText dateEditText;
    EditText cardEditText;
    EditText ccvEditText;
    boolean success = true;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.matt.bookapp.R.layout.activity_payment);


        dateEditText = (EditText) findViewById(com.matt.bookapp.R.id.expiry_date);
        cardEditText = (EditText) findViewById(com.matt.bookapp.R.id.card_number);
        ccvEditText = (EditText) findViewById(com.matt.bookapp.R.id.cvc);
        dateEditText.addTextChangedListener(dateWatcher);
        cardEditText.addTextChangedListener(cardNumberWatcher);
        checkFieldsForEmptyValues();
    }

    private TextWatcher cardNumberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {
            if (count <= cardEditText.getText().toString().length()
                    &&(cardEditText.getText().toString().length()==4
                    ||cardEditText.getText().toString().length()==9
                    ||cardEditText.getText().toString().length()==14)){
                cardEditText.setText(cardEditText.getText().toString()+" ");
                int pos = cardEditText.getText().length();
                cardEditText.setSelection(pos);
            }else if (count >= cardEditText.getText().toString().length()
                    &&(cardEditText.getText().toString().length()==4
                    ||cardEditText.getText().toString().length()==9
                    ||cardEditText.getText().toString().length()==14)){
                cardEditText.setText(cardEditText.getText().toString().substring(0,cardEditText.getText().toString().length()-1));
                int pos = cardEditText.getText().length();
                cardEditText.setSelection(pos);
            }
            count = cardEditText.getText().toString().length();
            if(count < 19){
                cardEditText.setError("Enter a 16 Digit Card Number");
                success = false;
            }
        }
    };
    private TextWatcher dateWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (working.length()==2 && before ==0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working)>12) {
                    isValid = false;
                } else {
                    working+="/";
                    dateEditText.setText(working);
                    dateEditText.setSelection(working.length());
                }
            }
            else if (working.length()==7 && before ==0) {
                String enteredYear = working.substring(3);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                if (Integer.parseInt(enteredYear) < currentYear) {
                    isValid = false;

                }
            } else if (working.length()!=7) {
                isValid = false;
            }

            if (!isValid) {
                dateEditText.setError("Enter a valid date: MM/YYYY");
                success = false;
            } else {
                dateEditText.setError(null);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    };
    private void checkFieldsForEmptyValues() {
        Button b = (Button) findViewById(com.matt.bookapp.R.id.btn_pay);
    }

    public void onSuccessfulPaymentClick(View view){
        final Intent intent = new Intent(this, ViewProfileActivity.class);
        EditText nameEditText = (EditText) findViewById(com.matt.bookapp.R.id.card_name);
        EditText cvcEditText = (EditText) findViewById(com.matt.bookapp.R.id.cvc);
        String dateText = dateEditText.getText().toString().trim();
        String nameText = nameEditText.getText().toString().trim();
        String ccvText = cvcEditText.getText().toString().trim();
        String numberText = cardEditText.getText().toString().trim();
        if(nameText.isEmpty() || nameText.length() == 0 || nameText.equals("") || nameText == null) {
            nameEditText.setError("Name is Empty");
            success = false;
        }
        if(ccvText.isEmpty() || ccvText.length() == 0 || ccvText.equals("") || ccvText == null){
            success = false;
            cvcEditText.setError("CVC is Empty");
        }
        if(ccvText.length() < 3 && ccvText.length() > 0){
            success = false;
            cvcEditText.setError("CVC is Incomplete");
        }
        if(dateText.length() != 7){
            success = false;
            dateEditText.setError("Enter a Valid Date");
        }
        if(numberText.length() < 16) {
            success = false;
            cardEditText.setError("Enter a 16 Digit Number");
        }
        else {
            UserCard card = new UserCard(ccvText,dateText,numberText);
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            databaseReference.child("users").child(user.getUid()).child("cardInfo").setValue(card);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        click[0] = true;
    }
}
