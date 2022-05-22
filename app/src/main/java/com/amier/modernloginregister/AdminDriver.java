package com.amier.modernloginregister;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AdminDriver extends AppCompatActivity {
    TextInputEditText driverName, driverEmail, driverPassword, driverMobile, driverAddress;
    Button addDriver;
    String name, email, password, mobile, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindriver);

        driverName=findViewById(R.id.driverName);
        driverEmail=findViewById(R.id.driverEmail);
        driverPassword=findViewById(R.id.driverPassword);
        driverMobile=findViewById(R.id.driverMobile);
        driverAddress=findViewById(R.id.driverAddress);
        addDriver=findViewById(R.id.addDriver);


        addDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, email, password, mobile, address;

                name = String.valueOf(driverName.getText());
                email = String.valueOf(driverEmail.getText());
                password = String.valueOf(driverPassword.getText());
                mobile = String.valueOf(driverMobile.getText());
                address = String.valueOf(driverAddress.getText());

                if (!name.equals("") && !email.equals("") && !password.equals("") && !mobile.equals("") && !address.equals("")) {
                    register();
                } else {
                    Toast.makeText(getApplicationContext(), "All Fields Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void register()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://latestsnewsinfo.com/LoginRegisterApp/signup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AdminDriver.this, ""+response, Toast.LENGTH_SHORT).show();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(AdminDriver.this, ""+response, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AdminDriver.this, AdminDB.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminDriver.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            public HashMap<String, String> getParams()
            {
                HashMap<String, String> p=new HashMap<>();
                p.put("email",driverEmail.getText().toString());
                p.put("password",driverPassword.getText().toString()+"");
                p.put("address",driverAddress.getText().toString());
                p.put("mobile",driverMobile.getText().toString()+"");
                p.put("name", driverName.getText().toString());
                p.put("user","driver");
                return p;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(AdminDriver.this);
        requestQueue.add(stringRequest);
    }

}

