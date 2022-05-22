package com.amier.modernloginregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DriverDB extends AppCompatActivity implements View.OnClickListener {

    public CardView cardProf, cardAnalytics, cardSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbdriver);

        cardProf = findViewById(R.id.cardProf);
        cardAnalytics = findViewById(R.id.cardAnalytics);
        cardSettings = findViewById(R.id.cardSettings);

        cardProf.setOnClickListener(view -> showToast("Profile"));
        cardAnalytics.setOnClickListener(view -> showToast("Analytics"));
        cardSettings.setOnClickListener(view -> showToast("Settings"));

        cardProf.setOnClickListener(this);
        cardAnalytics.setOnClickListener(this);
        cardSettings.setOnClickListener(this);
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
        } else if (id == cardAnalytics.getId()) {
            i = new Intent(this, DriverAnalytics.class);
            startActivity(i);
        } else if (id == cardSettings.getId()) {
            i = new Intent(this, Settings.class);
            startActivity(i);
        }
    }






}