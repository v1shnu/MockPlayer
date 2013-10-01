package com.vishnu.mockplayer.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.*;

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

    public static InputStream convertUriToStream(Context context, Uri receivedImage) {
        InputStream imageStream = null;
        try {
            imageStream = context.getContentResolver().openInputStream(receivedImage);
        } catch (FileNotFoundException e) {
            com.vishnu.mockplayer.utilities.Utilities.displayToast(context, "Error");
            e.printStackTrace();
        }
        return imageStream;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream imageStream, int reqWidth, int reqHeight) {
        byte[] byteArray = new byte[0];
        byte[] buffer = new byte[1024];
        int length, count=0;
        try {
            while((length = imageStream.read(buffer)) > -1) {
                if(length != 0) {
                    if(count + length > byteArray.length) {
                        byte[] newBuffer = new byte[(count+length) * 2];
                        System.arraycopy(byteArray, 0, newBuffer, 0, count);
                        byteArray = newBuffer;
                    }
                    System.arraycopy(buffer, 0, byteArray, count, length);
                    count+=length;
                }
            }
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(byteArray, 0, count, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(byteArray, 0, count, options);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
