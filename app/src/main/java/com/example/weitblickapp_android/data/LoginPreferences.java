package com.example.weitblickapp_android.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class LoginPreferences {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Login_Save_Pref";


    // All Shared Preferences Keys
    private static final String SAVE_LOGIN = "saveLogin";


    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    // Constructor
    public LoginPreferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void saveLogin(String name, String password) {
        // Storing login value as TRUE
        editor.putBoolean(SAVE_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_PASSWORD, password);

        // commit changes
        editor.commit();
    }

    public void setToSave() {
        editor.putBoolean(SAVE_LOGIN, true);
    }

    public void setTo_NOT_Save() {
        editor.putBoolean(SAVE_LOGIN, false);
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // return user
        return user;
    }

    public String getUserName() {

        return pref.getString(KEY_NAME, null);
    }

    public String getPassword() {

        return pref.getString(KEY_PASSWORD, null);
    }

    /**
     * Clear session details
     */
    public void clearLoginPreferences() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }


    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoginSaved() {
        return pref.getBoolean(SAVE_LOGIN, false);
    }

}
