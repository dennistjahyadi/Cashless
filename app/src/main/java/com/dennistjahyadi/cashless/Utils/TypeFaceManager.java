package com.dennistjahyadi.cashless.Utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Denn on 9/17/2016.
 */
public class TypeFaceManager {
    private static Typeface typefaceFontAwesome;


    public static Typeface getFontAwesomeTypeface(Context context) {
        if (typefaceFontAwesome == null) {
            typefaceFontAwesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
        }
        return typefaceFontAwesome;
    }

}
