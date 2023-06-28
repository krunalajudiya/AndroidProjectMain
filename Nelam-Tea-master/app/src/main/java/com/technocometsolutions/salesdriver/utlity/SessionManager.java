package com.technocometsolutions.salesdriver.utlity;

import android.content.Context;
import android.content.SharedPreferences;




public class SessionManager {

    public static final String SHARED_PREF_NAME = "com.app.myapplication";
    public static final String KEY_emp_id ="keyemp_id";
    public static final String KEY_first_name ="keyfirst_name";
    public static final String KEY_middle_name ="keymiddle_name";
    public static final String KEY_last_name ="keylast_name";
    public static final String KEY_emp_code ="keyemp_code";
    public static final String KEY_password ="keypassword";
    public static final String KEY_birth_date ="keybirth_date";
    public static final String KEY_gender ="keygender";
    public static final String KEY_blood_group ="keyblood_group";
    public static final String KEY_gender_type ="keygender_type";
    public static final String KEY_reporting_to ="keyreporting_to";
    public static final String KEY_designation ="key_designation";
    public static final String KEY_joining_date ="key_joining_date";
    public static final String KEY_qualification ="key_qualification";
    public static final String KEY_official_email ="key_official_email";
    public static final String KEY_personal_email ="key_personal_email";
    public static final String KEY_mobile_no ="key_mobile_no";
    public static final String KEY_ID ="keyid";
    public static final String KEY_LOGGED_IN = "loggedIn";
    public static final String KEY_FIREBASE_TOKEN = "firebase_token";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_Dealer_ID = "delaer_id";
    public static final String KEY_CHARGE_PER_KM = "charge_per_km";

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public SessionManager(Context context)
    {
        this.context=context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
    }

    public boolean checkLoggedIn(){
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false);
    }



    public void setFirebaseToken(String token)
    {
        editor.putString(KEY_FIREBASE_TOKEN,token);
        editor.commit();
    }

    public void setDeviceId(String deviceId)
    {
        editor.putString(KEY_DEVICE_ID,deviceId);
        editor.commit();
    }
    public void setUsername(String name)
    {
        editor.putString(KEY_USER_NAME,name);
        editor.commit();
    }
    public void setKeyChargePerKm(String km)
    {
        editor.putString(KEY_CHARGE_PER_KM,km);
        editor.commit();
    }

    public void setUserID(String id)
    {
        editor.putString(KEY_USER_ID,id);
        editor.commit();
    }
    public void setDealerID(String id)
    {
        editor.putString(KEY_Dealer_ID,id);
        editor.commit();
    }
    public void isLoggedIn(String keyemp_id,String keyfirst_name,String keymiddle_name,String keylast_name,String keyemp_code,
                           String keypassword,String keybirth_date,String keygender,String keyblood_group,String keygender_type,String keyreporting_to,
                           String key_designation,String key_joining_date,String key_qualification,String key_official_email,String key_personal_email,
                           String key_mobile_no)
    {

        editor.putString(KEY_emp_id,keyemp_id);
        editor.putString(KEY_first_name, keyfirst_name);
        editor.putString(KEY_middle_name, keymiddle_name);
        editor.putString(KEY_last_name, keylast_name);
        editor.putString(KEY_emp_code, keyemp_code);
        editor.putString(KEY_password, keypassword);
        editor.putString(KEY_birth_date, keybirth_date);
        editor.putString(KEY_gender, keygender);
        editor.putString(KEY_blood_group, keyblood_group);
        editor.putString(KEY_gender_type, keygender_type);
        editor.putString(KEY_reporting_to, keyreporting_to);
        editor.putString(KEY_designation, key_designation);
        editor.putString(KEY_joining_date, key_joining_date);
        editor.putString(KEY_qualification, key_qualification);
        editor.putString(KEY_official_email, key_official_email);
        editor.putString(KEY_personal_email, key_personal_email);
        editor.putString(KEY_mobile_no, key_mobile_no);

        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.commit();
    }
    public String getId(){
        return sharedPreferences.getString(KEY_emp_id, "");
    }
    public String getKeyChargePerKm(){
        return sharedPreferences.getString(KEY_CHARGE_PER_KM, "");
    }
    public String getDealerId(){
        return sharedPreferences.getString(KEY_Dealer_ID, "");
    }

    public String getKEY_first_name()
    {
        return sharedPreferences.getString(KEY_first_name,null);
    }
    public String getKEY_firebase_token()
    {
        return sharedPreferences.getString(KEY_FIREBASE_TOKEN,null);
    }
    public String getKeyDeviceId()
    {
        return sharedPreferences.getString(KEY_DEVICE_ID,null);
    }
    public String getKEY_user_name()
    {
        return sharedPreferences.getString(KEY_USER_NAME,null);
    }
    public String getKEY_user_id()
    {
        return sharedPreferences.getString(KEY_USER_ID,null);
    }

    public String getKEY_middle_name()
    {
        return sharedPreferences.getString(KEY_middle_name,null);
    }
    public String getKEY_last_name()
    {
        return sharedPreferences.getString(KEY_last_name,null);
    }
    public String getKEY_emp_code()
    {
        return sharedPreferences.getString(KEY_emp_code,null);
    }
    public String getKEY_password()
    {
        return sharedPreferences.getString(KEY_password,null);
    }
    public String getKEY_birth_date()
    {
        return sharedPreferences.getString(KEY_birth_date,null);
    }
    public String getKEY_gender()
    {
        return sharedPreferences.getString(KEY_gender,null);
    }
    public String getKEY_blood_group()
    {
        return sharedPreferences.getString(KEY_blood_group,null);
    }
    public String getKEY_gender_type()
    {
        return sharedPreferences.getString(KEY_gender_type,null);
    }
    public String getKEY_reporting_to()
    {
        return sharedPreferences.getString(KEY_reporting_to,null);
    }
    public String getKEY_designation()
    {
        return sharedPreferences.getString(KEY_designation,null);
    }
    public String getKEY_joining_date()
    {
        return sharedPreferences.getString(KEY_joining_date,null);
    }
    public String getKEY_qualification()
    {
        return sharedPreferences.getString(KEY_qualification,null);
    }
    public String getKEY_official_email()
    {
        return sharedPreferences.getString(KEY_official_email,null);
    }
    public String getKEY_personal_email()
    {
        return sharedPreferences.getString(KEY_personal_email,null);
    }
    public String getKEY_mobile_no()
    {
        return sharedPreferences.getString(KEY_mobile_no,null);
    }




    public void clear(){
        sharedPreferences.edit().clear().commit();
    }
}
