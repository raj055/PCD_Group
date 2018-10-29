package com.pcdgroup.hp.pcd_group.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name MySharedPreferences
 * @description In this class one time login user to save login details to local memory
 */

public class MySharedPreferences {

    private static final String PREFERENCES_FILENAME = "userinfo";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_USERPASSWORD = "userpassword";
    private static final String PREF_USERTYPE = "usertype";
    private static final String PREF_DISCOUNTVALUE = "discountvalue";


    public static void storeUsername(Context context, String username, String userpassword, String usertype, String discountvalue) {
        // check to see if the user is already logged in
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE).edit();
        editor.putString(PREF_USERNAME, username);
        editor.putString(PREF_USERPASSWORD, userpassword);
        editor.putString(PREF_USERTYPE, usertype);
        editor.putString(PREF_DISCOUNTVALUE, discountvalue);
        editor.commit();
    }

    public static String getUsername(Context context) {
        // check to see if the user is already logged in
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE);
        String username = prefs.getString(PREF_USERNAME, null);
        return username;
    }

    public static String getPassword(Context context) {
        // check to see if the user is already logged in
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE);
        String password = prefs.getString(PREF_USERPASSWORD, null);
        return password;
    }

    public static String getUsertype(Context context) {
        // check to see if the user is already logged in
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE);
        String usertype = prefs.getString(PREF_USERTYPE, null);
        return usertype;
    }

    public static String getDiscountValue(Context context) {
        // check to see if the user is already logged in
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE);
        String discountValue = prefs.getString(PREF_DISCOUNTVALUE, null);
        return discountValue;
    }
}
