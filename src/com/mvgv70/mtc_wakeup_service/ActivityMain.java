package com.mvgv70.mtc_wakeup_service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

public class ActivityMain extends Activity implements OnClickListener {
	
	private SharedPreferences prefs;
	Switch swService = null;
	private static final String TAG = "mtc-wakeup-service";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);
	  //
	  prefs = PreferenceManager.getDefaultSharedPreferences(this);
	  swService = (Switch)findViewById(R.id.swServiceEnable);
	  swService.setChecked(getServiceEnable());
	  // список запущенных сервисов
	  ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
      for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
      {
        Log.d(TAG,"------------------------------");
        Log.d(TAG,service.service.flattenToString());
      } 
      Log.d(TAG,"------------------------------");
	  // нужно ли запускать сервис
	  if (getServiceEnable() && !ServiceMain.isRunning)
        startService(new Intent(this, ServiceMain.class));
	  /*
	  ComponentName cn1 = new ComponentName("com.mvgv70.mtcvolume","ServiceMain");
      Log.d(TAG,"cn1="+cn1.toString());
	  Intent intent1 = new Intent();
	  intent1.setComponent(cn1);
	  ComponentName cr1 = startService(intent1);
	  if (cr1 == null)
	    Log.d(TAG,"startService1 == null");
	  else
	    Log.d(TAG,"startService1 = "+cr1.toString());
	  //
	  ComponentName cn2 = new ComponentName("com.mvgv70.mtcvolume",".ServiceMain");
      Log.d(TAG,"cn2="+cn2.toString());
	  Intent intent2 = new Intent();
	  intent2.setComponent(cn2);
	  ComponentName cr2 = startService(intent1);
	  if (cr2 == null)
	    Log.d(TAG,"startService2 == null");
	  else
	    Log.d(TAG,"startService2 = "+cr2.toString());
	  //
	  ComponentName cn3 = new ComponentName("com.mvgv70.mtcvolume","com.mvgv70.mtcvolume.ServiceMain");
      Log.d(TAG,"cn3="+cn3.toString());
	  Intent intent3 = new Intent();
	  intent3.setComponent(cn3);
	  ComponentName cr3 = startService(intent3);
	  if (cr3 == null)
	    Log.d(TAG,"startService3 == null");
	  else
	    Log.d(TAG,"startService3 = "+cr3.toString());
	  */
	}
	
	@Override
	public void onClick(View v) 
	{
	  switch (v.getId()) {
	  case R.id.swServiceEnable:
	    // включение/выключение сервиса
	    setServiceEnable(swService.isChecked());
	    setService(swService.isChecked());
	    break;
	  }
	}
	
	public void setService(boolean state)
	{
	  // остановка сервиса
	  Log.d(TAG,"ActivityMain.startService");
	  if (state)
	  {
	    // старт сервиса
		Log.d(TAG,"ActivityMain.startService");
	    startService(new Intent(this, ServiceMain.class));
	  }
	} 
	
	private boolean getBoolean(String name, boolean defaultValue)
	{
	  return prefs.getBoolean(name, defaultValue);
	}
	
	private void setBoolean(String name, boolean value)
	{
	  Editor editor = prefs.edit();
	  editor.putBoolean(name, value);
	  editor.commit();
	}
	
	public boolean getServiceEnable() 
	{
	  return getBoolean("service.enable", true);
	}
	
	public void setServiceEnable(boolean enable) 
	{
	  setBoolean("service.enable", enable);
	}
}
