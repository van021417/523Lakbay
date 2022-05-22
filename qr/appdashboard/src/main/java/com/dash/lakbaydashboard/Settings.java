package com.dash.lakbaydashboard;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dash.lakbaydashboard.databinding.ActivityUpdateBinding;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {
    ActivityUpdateBinding binding;
    String id, name, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_update);

        id=getIntent().getStringExtra("id");
        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");

        binding.name.setText(name);
        binding.email.setText(email);

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    private void update(){

        String url="http://192.168.1.24/LoginRegister/update.php?id=" + id;
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Settings.this, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Settings.this, error.toString(), Toast.LENGTH_SHORT).show();


            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("email",binding.email.getText().toString());
                map.put("name",binding.name.getText().toString());
                return map;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }
}

