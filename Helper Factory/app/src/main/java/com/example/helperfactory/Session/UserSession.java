package com.example.helperfactory.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.helperfactory.Activity.Number_Verification;
import com.example.helperfactory.MainActivity;


public class UserSession {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "UserSessionPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // User name (make variable public to access from outside)
    public static final String KEY_USER_DETAILS = "user_detail";
    // check first time app launch
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public static final String KEY_TOKEN="key_token";



    // Constructor
    public UserSession(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    /**
     * Create login session
     * */
    public void createLoginSession(String user_detail){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER_DETAILS, user_detail);

        // commit changes
        editor.commit();
    }
    /**
     * Get stored session data
     * */
    public String getUserDetails(){

        String userdetail=pref.getString(KEY_USER_DETAILS, null);
        // return user
        return userdetail;
    }
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_LOGIN,false);
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, Number_Verification.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        context.startActivity(i);

    }
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String getKeyToken(){

        String userdetail=pref.getString(KEY_TOKEN,null);
        // return user
        return userdetail;
    }
    public void setKeyToken(String keyToken){

        // Storing name in pref
        editor.putString(KEY_TOKEN,keyToken);

        // commit changes
        editor.commit();
    }
}
