package com.dash.lakbaydashboard;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.journaldev.barcodevisionapi.Qrgenerator;


public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {

    public CardView cardProf, cardSettings, cardEwallet, cardTransaction, cardRideNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        cardProf = findViewById(R.id.cardProf);

        cardEwallet = findViewById(R.id.cardEwallet);
        cardTransaction = findViewById(R.id.cardTransaction);
        cardRideNow = findViewById(R.id.RideNow);


        cardProf.setOnClickListener(view -> showToast("Profile"));

        cardEwallet.setOnClickListener(view -> showToast("E-Wallet"));
        cardTransaction.setOnClickListener(view -> showToast("Transaction History"));
        cardRideNow.setOnClickListener(view -> showToast("Ride Now"));
        cardProf.setOnClickListener(this);

        cardEwallet.setOnClickListener(this);
        cardTransaction.setOnClickListener(this);
        cardRideNow.setOnClickListener(this);


    }

    private void showToast(String message) {
        Toast.makeText(this, message


                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        Intent i;

        if (id == cardProf.getId()) {
            i = new Intent(this, Profile.class);
            startActivity(i);
        } else if (id == cardEwallet.getId()) {
            i = new Intent(this, EWallet.class);
            startActivity(i);
        }  else if (id == cardRideNow.getId()) {
            i = new Intent(this, Qrgenerator.class);
            startActivity(i);
        }


    }






}