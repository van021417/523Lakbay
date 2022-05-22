package com.journaldev.barcodevisionapi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class PictureBarcodeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String APPLICATION_ID = "com.journaldev.barcodevisionapi";
    Button btnOpenCamera,detect;
    TextView txtResultBody;
    private BarcodeDetector detector;
    private Uri imageUri;
    TextView txtname,txtaddres,txtphone;
    Bitmap bitmap;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    ImageView viewImage;
    private static final int CAMERA_REQUEST = 101;
    private static final String TAG = "API123";
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private String sharepref="modernloginregister";
    SharedPreferences sharedPreferences;
    String drivername="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_picture);
        sharedPreferences=getSharedPreferences(sharepref,MODE_PRIVATE);
        txtname=findViewById(R.id.txtname);
        txtaddres=findViewById(R.id.txtaddress);
        txtphone=findViewById(R.id.txtphone);
        drivername=sharedPreferences.getString("useremail","");

        detect=findViewById(R.id.detect);
        viewImage=findViewById(R.id.imageView);
        initViews();
        if (savedInstanceState != null) {
            if (imageUri != null) {
                imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
                txtResultBody.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
            }
        }
        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        if (!detector.isOperational()) {
            txtResultBody.setText("Detector initialisation failed");
            return;
        }
        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detect();
            }
        });
    getbarcode();
    }

    private void initViews() {
        txtResultBody = findViewById(R.id.txtResultsBody);
        btnOpenCamera = findViewById(R.id.btnOpenCamera);
        txtResultBody = findViewById(R.id.txtResultsBody);
        btnOpenCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnOpenCamera) {
            ActivityCompat.requestPermissions(PictureBarcodeActivity.this, new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                   selectImage();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void takeBarcodePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "pic.jpg");
        imageUri = FileProvider.getUriForFile(PictureBarcodeActivity.this,
                APPLICATION_ID + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
            outState.putString(SAVED_INSTANCE_RESULT, txtResultBody.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(PictureBarcodeActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(),"");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    viewImage.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                 bitmap = (BitmapFactory.decodeFile(picturePath));
//                Log.w("path of image from gallery......******************.........", picturePath+"");
                viewImage.setImageBitmap(bitmap);
            }
        }
    }
    public void detect()
    {
        if (detector.isOperational() && bitmap != null) {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);
            for (int index = 0; index < barcodes.size(); index++) {
                Barcode code = barcodes.valueAt(index);
                txtResultBody.setText("");
                txtResultBody.setText(txtResultBody.getText() + "\n" + code.displayValue + "\n");
                String[] datasplit=txtResultBody.getText().toString().split(" ");
                txtname.setText(datasplit[1]);
                txtaddres.setText(datasplit[3]);
                txtphone.setText(datasplit[5]);
                int type = barcodes.valueAt(index).valueFormat;
                switch (type) {
                    case Barcode.CONTACT_INFO:
                        Log.i(TAG, code.contactInfo.title);
                        break;
                    case Barcode.EMAIL:
                        Log.i(TAG, code.displayValue);
                        break;
                    case Barcode.ISBN:
                        Log.i(TAG, code.rawValue);
                        break;
                    case Barcode.PHONE:
                        Log.i(TAG, code.phone.number);
                        break;
                    case Barcode.PRODUCT:
                        Log.i(TAG, code.rawValue);
                        break;
                    case Barcode.SMS:
                        Log.i(TAG, code.sms.message);
                        break;
                    case Barcode.TEXT:
                        Log.i(TAG, code.displayValue);
                        break;
                    case Barcode.URL:
                        Log.i(TAG, "url: " + code.displayValue);
                        break;
                    case Barcode.WIFI:
                        Log.i(TAG, code.wifi.ssid);
                        break;
                    case Barcode.GEO:
                        Log.i(TAG, code.geoPoint.lat + ":" + code.geoPoint.lng);
                        break;
                    case Barcode.CALENDAR_EVENT:
                        Log.i(TAG, code.calendarEvent.description);
                        break;
                    case Barcode.DRIVER_LICENSE:
                        Log.i(TAG, code.driverLicense.licenseNumber);
                        break;
                    default:
                        Log.i(TAG, code.rawValue);
                        break;
                }
            }
            storedatabasetodriver();
            if (barcodes.size() == 0) {
                txtResultBody.setText("No barcode could be detected. Please try again.");
            }
        } else {

            txtResultBody.setText("Detector initialisation failed");
        }
    }
    public void getbarcode()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://latestsnewsinfo.com/LoginRegisterApp/getqrcode.php", new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject1 =jsonArray.getJSONObject(i);
                        viewImage.setImageBitmap(Utility.stringToBitmap(jsonObject1.getString("codeimage")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
   //             Toast.makeText(PictureBarcodeActivity.this, ""+response, Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(Qrgenerator.this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            //    Toast.makeText(PictureBarcodeActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            public HashMap<String, String> getParams()
            {
                HashMap<String, String> p=new HashMap<>();
                p.put("uid",2+"");
                return p;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(PictureBarcodeActivity.this);
        requestQueue.add(stringRequest);
    }
    public void storedatabasetodriver()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://latestsnewsinfo.com/LoginRegisterApp/storedatatodriver.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PictureBarcodeActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(PictureBarcodeActivity.this, ""+response, Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(Qrgenerator.this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PictureBarcodeActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            public HashMap<String, String> getParams()
            {
                HashMap<String, String> p=new HashMap<>();
                p.put("userinfo",txtResultBody.getText().toString()+"");
                p.put("drivername",drivername+"");
                return p;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(PictureBarcodeActivity.this);
        requestQueue.add(stringRequest);
    }
}
