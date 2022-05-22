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

public class Reg extends AppCompatActivity {
TextInputEditText textInputEditTextname, textInputEditTextemail, textInputEditTextpassword, textInputEditMobile, textInputEditaddress;
Button button1;
Button btnLogRegister;
    String name, email, password, mobile, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textInputEditTextname=findViewById(R.id.name);
        textInputEditTextemail=findViewById(R.id.email);
        textInputEditTextpassword=findViewById(R.id.password);
        textInputEditMobile=findViewById(R.id.mobile);
        textInputEditaddress=findViewById(R.id.address);
        button1=findViewById(R.id.button1);
        btnLogRegister=findViewById(R.id.btnLogRegister);


        btnLogRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Log.class);
                startActivity(intent);
                finish();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String name, email, password, mobile, address;

                name = String.valueOf(textInputEditTextname.getText());
                email = String.valueOf(textInputEditTextemail.getText());
                password = String.valueOf(textInputEditTextpassword.getText());
                mobile = String.valueOf(textInputEditMobile.getText());
                address = String.valueOf(textInputEditaddress.getText());

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
                Toast.makeText(Reg.this, ""+response, Toast.LENGTH_SHORT).show();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(Reg.this, ""+response, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Reg.this, Log.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Reg.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            public HashMap<String, String> getParams()
            {
                HashMap<String, String> p=new HashMap<>();
                p.put("email",textInputEditTextname.getText().toString());
                p.put("password",textInputEditTextpassword.getText().toString()+"");
                p.put("address",textInputEditaddress.getText().toString());
                p.put("mobile",textInputEditMobile.getText().toString()+"");
                p.put("name", textInputEditTextname.getText().toString());
                p.put("user","passenger");
                return p;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(Reg.this);
        requestQueue.add(stringRequest);
    }

}
