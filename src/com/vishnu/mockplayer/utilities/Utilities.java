package com.vishnu.mockplayer.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/9/13
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utilities {
    public static void displayToast(Context context, CharSequence text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    public static void log(String message) {
        Log.e("Mock Player App Log : ", message);
    }

    public static Bitmap convertUriToImage(Context context, Uri receivedImage) {
        InputStream imageStream = null;
        try {
            imageStream = context.getContentResolver().openInputStream(receivedImage);
        } catch (FileNotFoundException e) {
            com.vishnu.mockplayer.utilities.Utilities.displayToast(context, "Error");
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(imageStream);
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;
        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));
        photo=Bitmap.createScaledBitmap(photo, w, h, true);
        return photo;
    }
}
