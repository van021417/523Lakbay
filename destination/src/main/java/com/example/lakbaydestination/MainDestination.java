package com.example.lakbaydestination;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainDestination extends AppCompatActivity {
    EditText fromLocation, toLocation;
    TextView text, text1, text2;
    String sType;
    double lat1=0, long1=0, lat2=0, long2=0;
    int flag=0;

    private EditText edtValue;
    Button btnPaynow, ShowFare;
    String value="";
    private String sharepref="modernloginregister";
    SharedPreferences sharedPreferences;
    String qrcodestring="";
    TextInputEditText noofpassenger;
    private AppCompatActivity activity;
double p;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        noofpassenger=findViewById(R.id.noofpassenger);
        fromLocation = findViewById(R.id.fromLocation);
        toLocation = findViewById(R.id.toLocation);
        text = findViewById(R.id.text);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        Places.initialize(getApplicationContext(),"AIzaSyCwZwKO-2e7HsfBSkGB_IoriKtcr5aNCIU");

        sharedPreferences=getSharedPreferences(sharepref,MODE_PRIVATE);
        value=sharedPreferences.getString("useremail","");

        btnPaynow=findViewById(R.id.btnPaynow);
        ShowFare=findViewById(R.id.ShowFare);

        activity = this;


        fromLocation.setFocusable(false);
        fromLocation.setOnClickListener(v -> {
            sType = "source";

            List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY,fields).build(MainDestination.this);
                    startActivityForResult(intent,100);

        });
        toLocation.setFocusable(false);
        toLocation.setOnClickListener(v -> {
            sType = "destination";

            List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY,fields)
                    .build(MainDestination.this);
                    startActivityForResult(intent,100);

        });
        text.setText("O.0 km");
        text1.setText("Fare");

        btnPaynow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.globe.gcash.android");
                if(intent != null){
                    startActivity(intent);
                }else{
                    Toast.makeText(MainDestination.this, "No Package", Toast.LENGTH_SHORT).show();

                }
            }
        });
        getuserprofile();
        ShowFare.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                distance (lat1,long1,lat2,long2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            if (sType.equals("source")){
                flag++;
                fromLocation.setText(place.getAddress());
                String sFrom = String.valueOf(place.getLatLng());
                sFrom = sFrom.replaceAll("lat/lng: ", "");
                sFrom = sFrom.replace("(","");
                sFrom = sFrom.replace(")","");
                String[] split = sFrom.split(",");
                lat1 = Double.parseDouble(split[0]);
                long1 = Double.parseDouble(split[1]);
            } else {
                flag++;
                toLocation.setText(place.getAddress());
                toLocation.setText(place.getAddress());
                String sTo = String.valueOf(place.getLatLng());
                sTo = sTo.replaceAll("lat/lng: ", "");
                sTo = sTo.replace("(","");
                sTo = sTo.replace(")","");
                String[] split = sTo.split(",");
                lat2 = Double.parseDouble(split[0]);
                long2 = Double.parseDouble(split[1]);
                }

        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),
                    Toast.LENGTH_SHORT).show();

        }

    }

    private void distance(double lat1, double long1, double lat2, double long2) {
        p = Double.parseDouble(noofpassenger.getText().toString());
        double longDiff = long1-long2;
        double distance = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(longDiff));

        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = distance*60*1.1515;
        distance = distance*1.609344;
        distance = distance*1.609344;
        double fare=9;
        double finalfare;
        double fare1 = distance - 5;
        fare = fare+(fare1 * 1.55);
        finalfare = fare * p;

        text.setText(String.format(Locale.US, "%.2f /person",fare));
        text1.setText(String.format(Locale.US, "%.1f Kilometers",distance));
        text2.setText(String.format(Locale.US, "Total Fare: %.2f",finalfare));

    }

    private double rad2deg(double distance) {
        return (distance*180.0/Math.PI);

    }

    private double deg2rad(double lat1) {
        return (lat1*Math.PI/180.0);
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
                Toast.makeText(MainDestination.this, ""+response, Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(Qrgenerator.this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainDestination.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue= Volley.newRequestQueue(MainDestination.this);
        requestQueue.add(stringRequest);
    }
}
