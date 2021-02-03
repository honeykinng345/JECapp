package j.e.c.com.Others;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Prefrence {

    static SharedPreferences prefs;

    public static void saveProfileImage(Bitmap imageBitmap, Context context){
        if (prefs == null)
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("ImageUriString", encodeTobase64(imageBitmap)).apply();
    }

    public static void saveProfileImage(Uri imageUri, Context context){
        if (prefs == null)
            prefs = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            prefs.edit().putString("ImageUriString", encodeTobase64(imageBitmap)).apply();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Bitmap getProfileImage(Context context){
        if (prefs == null)
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String imageString = prefs.getString("ImageUriString", null);
        if (imageString == null)
            return null;
        return decodeBase64(imageString);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        //Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static void saveFcmToken(String token, Context context){
        if (prefs == null)
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("FcmToken", token).apply();
    }

    public static String getFcmToken(Context context){
        if (prefs == null)
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("FcmToken", null);
    }
}
