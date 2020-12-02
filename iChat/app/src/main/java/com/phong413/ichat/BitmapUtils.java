package com.phong413.ichat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {

    public static byte[] bitmapToByte(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static Bitmap bitMapFromByte(byte[] data) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        Canvas canvas = new Canvas(bmp);
        return bmp;
    }

    public static Bitmap resizeBitmap(Bitmap b) {
        return Bitmap.createScaledBitmap(b, 100, 100, false);
    }

    public static Bitmap resizeBitmap(Bitmap b, int width, int height) {
        return Bitmap.createScaledBitmap(b, width, height, false);
    }
}
