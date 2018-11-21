package com.dennistjahyadi.cashless.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Denn on 7/15/2017.
 */

public class SharedPreferenceUtils {

    public static final String SP_NAME = "cashlesssp";
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String FULLNAME = "fullname";
    public static final String BALANCE = "balance";
    public static final String PHONE_NO = "phone_no";
    public static final String ADDRESS = "address";

    public static void setUserBalance(Context context, Integer amount) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE).edit();
        editor.putInt(SharedPreferenceUtils.BALANCE, amount);
        editor.apply();
    }

    public static void setUserId(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE).edit();
        editor.putString(SharedPreferenceUtils.ID, userId);
        editor.apply();
    }

    public static void setUserFullname(Context context, String fullname) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE).edit();
        editor.putString(SharedPreferenceUtils.FULLNAME, fullname);
        editor.apply();
    }

    public static Integer getUserBalance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
        Integer balance = prefs.getInt(SharedPreferenceUtils.BALANCE, 0);

        return balance;
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
        String userId = prefs.getString(SharedPreferenceUtils.ID, null);

        return userId;
    }

    public static SharedPreferences getPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
        return prefs;
    }

    public static SharedPreferences.Editor getPrefsEditor(Context context) {
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE).edit();
        return prefsEditor;
    }
}
