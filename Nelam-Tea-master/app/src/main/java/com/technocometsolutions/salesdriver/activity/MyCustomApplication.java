package com.technocometsolutions.salesdriver.activity;

import android.app.Application;
import android.content.res.Configuration;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

public class MyCustomApplication extends Application {
        // Called when the application is starting, before any other application objects have been created.
        // Overriding this method is totally optional!
		private String androidDeviceId;


	@Override
	public void onCreate() {
	    super.onCreate();
		SessionManager sessionManager=new SessionManager(this);
		androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
				Settings.Secure.ANDROID_ID);
		Log.d("ddDeviceId","DeviceId : "+androidDeviceId);
		String token = FirebaseInstanceId.getInstance().getToken();
		Log.i("ddDeviceId", "FCM Registration Token: " + token);
		sessionManager.setFirebaseToken(token);
		sessionManager.setDeviceId(androidDeviceId);


	}

        // Called by the system when the device configuration changes while your component is running.
        // Overriding this method is totally optional!
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}

        // This is called when the overall system is running low on memory, 
        // and would like actively running processes to tighten their belts.
        // Overriding this method is totally optional!
	@Override
	public void onLowMemory() {
	    super.onLowMemory();
	}
}