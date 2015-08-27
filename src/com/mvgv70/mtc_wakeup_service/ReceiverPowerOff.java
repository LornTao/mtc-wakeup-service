package com.mvgv70.mtc_wakeup_service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class ReceiverPowerOff extends BroadcastReceiver {
	
  private static final String TAG = "mtc-wakeup-service";
  @SuppressLint("SdCardPath")
  public static final String RUNNUNG_REVICE_LIST = "/data/data/com.mvgv70.mtc_wakeup_service/running-services";
  public final static String WAKE_ACTIVE_SERVICES = Environment.getExternalStorageDirectory().getPath()+"/mtc-wakeup-service/wake-only-active";
	  
  @Override
  public void onReceive(Context context, Intent intent) 
  {
    // выключение магнитолы или уход в сон
    File f = new File(WAKE_ACTIVE_SERVICES);
    // если файла wake-only-active нет - ничего не делаем  
    if (!f.exists()) return;
    if (intent.getStringExtra("class").equals("poweroff"))
    {
      Log.d(TAG,"build running-services");
      // сохранение списка запущенных сервисов в файл
      ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
      try
      {
        FileWriter writer = new FileWriter(RUNNUNG_REVICE_LIST);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
          writer.write(service.service.getClassName());
          writer.write(System.getProperty("line.separator"));
        }
        writer.close();
      }
      catch (IOException e)
      {
        Log.e(TAG,e.getMessage());
      }
    }
  }

}
