package com.amier.modernloginregister;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Log extends AppCompatActivity {
    TextInputEditText textInputEditTextemail2, textInputEditTextpassword2;
    Button btnRegLogin, btnLogin;
    private String sharedpref="modernloginregister";
    String  email, password;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences=getSharedPreferences(sharedpref, MODE_PRIVATE);
        editor=sharedPreferences.edit();

        textInputEditTextemail2=findViewById(R.id.email2);
        textInputEditTextpassword2=findViewById(R.id.password2);
        btnRegLogin=findViewById(R.id.btnRegLogin);
        btnLogin=findViewById(R.id.btnLogin);

        btnRegLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Reg.class);
                startActivity(intent);
                finish();
            }
        });
//when i opening it closing suddenly
        // do ti once again

         //can you show mobile phoen screen

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = String.valueOf(textInputEditTextemail2.getText());
                password = String.valueOf(textInputEditTextpassword2.getText());

                if ( !email.equals("") && !password.equals("")) {
                    loginuser();

                } else {
                    Toast.makeText(getApplicationContext(), "All Fields Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void loginuser()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://latestsnewsinfo.com/LoginRegisterApp/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Log.this, ""+response, Toast.LENGTH_SHORT).show();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    int success=jsonObject.optInt("success");
                    String level=jsonObject.optString("user");

                    String name=jsonObject.optString("name");
                    editor.putString("useremail",name);
                    editor.apply();
                    if(success==1)
                    {
                        if(level.equalsIgnoreCase("passenger"))
                        {
                            Intent intent=new Intent(Log.this, MainActivity1.class);
                            startActivity(intent);
                        } else if (level.equalsIgnoreCase("admin")) {
                            Intent intent=new Intent(Log.this, AdminDB.class);
                            startActivity(intent);
                        } else if (level.equalsIgnoreCase("driver")) {
                            Intent intent=new Intent(Log.this, DriverDB.class);
                            startActivity(intent);
                        }

                    }
                    else
                    {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(Log.this, ""+response, Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(Qrgenerator.this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Log.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            public HashMap<String, String> getParams()
            {
                HashMap<String, String> p=new HashMap<>();
                p.put("email",email);
                p.put("password",password+"");
                return p;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(Log.this);
        requestQueue.add(stringRequest);
    }
    }