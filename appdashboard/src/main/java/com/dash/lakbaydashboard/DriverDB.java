package com.dash.lakbaydashboard;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.journaldev.barcodevisionapi.PictureBarcodeActivity;


public class DriverDB extends AppCompatActivity implements View.OnClickListener {

    public CardView cardProf, cardSettings, cardEwallet, cardTransaction, cardScanNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverdb);
        cardProf = findViewById(R.id.cardProf);

        cardEwallet = findViewById(R.id.cardEwallet);
        cardTransaction = findViewById(R.id.cardTransaction);
        cardScanNow = findViewById(R.id.scanNow);


        cardProf.setOnClickListener(view -> showToast("Profile"));
        cardEwallet.setOnClickListener(view -> showToast("E-Wallet"));
        cardTransaction.setOnClickListener(view -> showToast("Transaction History"));
        cardScanNow.setOnClickListener(view -> showToast("Scan Now"));
        cardProf.setOnClickListener(this);

        cardEwallet.setOnClickListener(this);
        cardTransaction.setOnClickListener(this);
        cardScanNow.setOnClickListener(this);


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
            i = new Intent(this, DriverProfile.class);
            startActivity(i);
        } else if (id == cardEwallet.getId()) {
            i = new Intent(this, EWallet.class);
            startActivity(i);
        } else if (id == cardTransaction.getId()) {
            i = new Intent(this, DriverTransactionHistory.class);
            startActivity(i);
        } else if (id == cardScanNow.getId()) {
            i = new Intent(this, PictureBarcodeActivity.class);
            startActivity(i);
        }


    }






}