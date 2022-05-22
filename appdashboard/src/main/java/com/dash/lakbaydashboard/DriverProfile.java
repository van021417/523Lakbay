package com.dash.lakbaydashboard;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DriverProfile extends AppCompatActivity {
    String Heroes;

    String value;
    String result;
    HttpURLConnection httpURLConnection;
    private String sharepref="modernloginregister";
    TextView name,address, email, mobile, user;
    SharedPreferences sharedPreferences;
    Bundle extras;




    ImageButton address1;
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences=getSharedPreferences(sharepref,MODE_PRIVATE);
        value=sharedPreferences.getString("useremail","");
        Log.d("useremail",value
        );
        getJSON("https://latestsnewsinfo.com/LoginRegisterApp/get.php?useremail="
                +value);

        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        email = (TextView) findViewById(R.id.email);
        mobile = (TextView) findViewById(R.id.mobile);
        user = (TextView) findViewById(R.id.user);
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
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        JSONArray jsonArray = new JSONArray(json);
        String[] heroes = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            address.setVisibility(View.VISIBLE);
            mobile.setVisibility(View.VISIBLE);
            user.setVisibility(View.VISIBLE);

            name.setText(obj.getString("name"));
            email.setText(obj.getString("email"));
            address.setText(obj.getString("address"));
            mobile.setText(obj.getString("mobile"));
            user.setText(obj.getString("user"));
        }
    }
}


