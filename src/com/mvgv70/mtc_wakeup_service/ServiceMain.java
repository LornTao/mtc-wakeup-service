package com.mvgv70.mtc_wakeup_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServiceMain extends Service {
	
	private static final String TAG = "mtc-wakeup-service";
	public static boolean isRunning = false;
	
	@Override
	public void onCreate() 
	{
      Log.d(TAG,"WakeService.OnCreate");
	  isRunning = true;
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
	  return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
	  super.onStartCommand(intent, flags, startId);
	  Log.d(TAG,"WakeService.onStartCommand");
	  return START_STICKY;
	}
	
}