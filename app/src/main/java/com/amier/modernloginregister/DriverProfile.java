package com.amier.modernloginregister;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class DriverProfile extends AppCompatActivity {
    String Heroes;
    String result;
    String value;
    private String sharepref="modernloginregister";
    TextView driverName,driverAddress, driverEmail, driverMobile, driverUser;
    SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverprofile);
        sharedPreferences=getSharedPreferences(sharepref,MODE_PRIVATE);
        value=sharedPreferences.getString("useremail","");
        Toast.makeText(this, ""+value, Toast.LENGTH_SHORT).show();
        Log.d("useremail",value
        );
        getJSON("https://latestsnewsinfo.com/LoginRegisterApp/get.php?useremail="+value);

        driverName = (TextView) findViewById(R.id.driverName);
        driverAddress = (TextView) findViewById(R.id.driverAddress);
        driverEmail = (TextView) findViewById(R.id.driverEmail);
        driverMobile = (TextView) findViewById(R.id.driverMobile);
        driverUser = (TextView) findViewById(R.id.user);
        getprofile();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void getJSON(final String urlWebservice) {

        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
        //        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        //        try {
        //            loadIntoListView(s);
        //        } catch (JSONException e) {
        //            e.printStackTrace();
        //        }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebservice);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json).append("\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
    private void loadIntoListView (String json) throws JSONException{
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        JSONArray jsonArray = new JSONArray(json);
        String[] heroes = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            driverName.setVisibility(View.VISIBLE);
            driverEmail.setVisibility(View.VISIBLE);
            driverAddress.setVisibility(View.VISIBLE);
            driverMobile.setVisibility(View.VISIBLE);
            driverUser.setVisibility(View.VISIBLE);

            driverName.setText(obj.getString("name"));
            driverEmail.setText(obj.getString("email"));
            driverAddress.setText(obj.getString("address"));
            driverMobile.setText(obj.getString("mobile"));
            driverUser.setText(obj.getString("user"));
        }
    }
    public void getprofile()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://latestsnewsinfo.com/LoginRegisterApp/get.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(DriverProfile.this, ""+response, Toast.LENGTH_SHORT).show();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        driverName.setVisibility(View.VISIBLE);
                        driverEmail.setVisibility(View.VISIBLE);
                        driverAddress.setVisibility(View.VISIBLE);
                        driverMobile.setVisibility(View.VISIBLE);
                        driverUser.setVisibility(View.VISIBLE);

                        driverName.setText(obj.getString("name"));
                        driverEmail.setText(obj.getString("email"));
                        driverAddress.setText(obj.getString("address"));
                        driverMobile.setText(obj.getString("mobile"));
                        driverUser.setText(obj.getString("user"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(DriverProfile.this, ""+response, Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(Qrgenerator.this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DriverProfile.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            public HashMap<String, String> getParams()
            {
                HashMap<String, String> p=new HashMap<>();
                p.put("useremail",value);

//                p.put("level",1+"");
                return p;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(DriverProfile.this);
        requestQueue.add(stringRequest);
    }
}


