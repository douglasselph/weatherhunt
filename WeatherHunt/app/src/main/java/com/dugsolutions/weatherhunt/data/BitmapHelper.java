package com.dugsolutions.weatherhunt.data;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by dug on 7/21/17.
 */

public class BitmapHelper {

    static BitmapHelper sInstance;

    public static void Init() {
        new BitmapHelper();
    }

    public static BitmapHelper getInstance() {
        return sInstance;
    }

    HashMap<Integer, Bitmap> mMap = new HashMap();

    BitmapHelper() {
        sInstance = this;
    }

    public boolean hasBitmap(Integer code) {
        return mMap.containsKey(code);
    }

    public Bitmap getBitmap(Integer code) {
        return mMap.get(code);
    }

    public void storeBitmap(Integer code, ImageView view) {
        if (view.getDrawable() instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
            mMap.put(code, bitmap);
        }
    }

}
