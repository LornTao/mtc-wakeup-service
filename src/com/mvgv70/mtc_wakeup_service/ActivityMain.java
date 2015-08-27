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
