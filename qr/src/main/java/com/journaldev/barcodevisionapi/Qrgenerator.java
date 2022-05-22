package com.journaldev.barcodevisionapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGEncoder;

public class Qrgenerator extends AppCompatActivity {
    private EditText edtValue;
    Button btnPaynow;
    Spinner from,to;
    private ImageView qrImage;
    ArrayList<String> fromarray,toarray;
    ArrayAdapter<String> fromadapter;
    ArrayAdapter<String> toadapter;
    private String inputValue;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    private Bitmap bitmap;
    String value="";
    String todata,fromdata;
    private String sharepref="modernloginregister";
    SharedPreferences sharedPreferences;
    String qrcodestring="";
    int totalfare=0;
    TextInputEditText edtfare,noofpassenger;
    private QRGEncoder qrgEncoder;
    private AppCompatActivity activity;
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);
        sharedPreferences=getSharedPreferences(sharepref,MODE_PRIVATE);
        value=sharedPreferences.getString("useremail","");
        noofpassenger=findViewById(R.id.noofpassenger);
        edtfare=findViewById(R.id.fare);
        fromarray=new ArrayList<>();
        fromarray.add("Bacoor");
        fromarray.add("Dasma");
        fromarray.add("Imus");
        fromarray.add("Indang");
        fromarray.add("Trece");

        toarray=new ArrayList<>();
        toarray.add("Bacoor");
        toarray.add("Dasma");
        toarray.add("Imus");
        toarray.add("Indang");
        toarray.add("Trece");

        fromadapter=new ArrayAdapter<>(Qrgenerator.this,R.layout.support_simple_spinner_dropdown_item,fromarray);
        toadapter=new ArrayAdapter<>(Qrgenerator.this,R.layout.support_simple_spinner_dropdown_item,toarray);

        from=findViewById(R.id.from);
        to=findViewById(R.id.to);
        btnPaynow=findViewById(R.id.btnPaynow);

        from.setAdapter(fromadapter);
        to.setAdapter(toadapter);
        //qrImage = findViewById(R.id.qr_image);
        activity = this;

        btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.globe.gcash.android");
                if(intent != null){
                    startActivity(intent);
                }else{
                    Toast.makeText(Qrgenerator.this, "No Package", Toast.LENGTH_SHORT).show();

                }
            }
        });
        getuserprofile();
    }


    public void getuserprofile()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://latestsnewsinfo.com/LoginRegisterApp/get.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(activity, ""+response, Toast.LENGTH_SHORT).show();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    JSONObject jsonObject1=jsonArray.getJSONObject(0);

                    String name=jsonObject1.optString("name");
                    String address=jsonObject1.optString("address");
                    String mobile=jsonObject1.optString("mobile");
                    qrcodestring="Name "+name+"  Address "+address+"  Phone  "+mobile;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(Qrgenerator.this, ""+response, Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(Qrgenerator.this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Qrgenerator.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            public HashMap<String, String> getParams()
            {
                HashMap<String, String> p=new HashMap<>();
                p.put("username",value+"");
                return p;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(Qrgenerator.this);
        requestQueue.add(stringRequest);
    }




}