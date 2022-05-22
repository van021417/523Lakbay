package com.journaldev.barcodevisionapi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;

public class Utility {
    public static String Name;
    public static String Number;
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static Bitmap stringToBitmap(String base64)
    {
        if(base64.isEmpty())
            return null;
        byte[] imageBytes= Base64.decode(base64, Base64.DEFAULT);
        Bitmap img= BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
        return  img;
    }
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static String bitmapToString(Bitmap bmp1)
    {
        if(bmp1==null)
            return "";
        Bitmap bmp= Bitmap.createScaledBitmap(bmp1,250,250,false);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.WEBP,100,baos);
        byte[] imageBytes=baos.toByteArray();
        String imageString= Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }
}