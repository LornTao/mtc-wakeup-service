package com.mvgv70.mtc_wakeup_service;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class ReceiverBoot extends BroadcastReceiver {
	
  private static final String TAG = "mtc-wakeup-service";
  private final static String WAKE_ON_BOOT_FILE_NAME = Environment.getExternalStorageDirectory().getPath()+"/mtc-wakeup-service/wake-on-boot";

  @Override
  public void onReceive(Context context, Intent intent) 
  {
    String action = intent.getAction();
    Log.d(TAG,"ReceiverBoot.onReceive "+action);
    // стартуем wakeup-сервис
    if (!ServiceMain.isRunning)
    {
      context.startService(new Intent(context, ServiceMain.class));
      File f = new File(WAKE_ON_BOOT_FILE_NAME);
      if (f.exists())
      {
        // стартуем сервисы
        Log.d(TAG,"wake-on-boot: start services");
        context.sendBroadcast(new Intent(context, ReceiverWakeUp.class));
      }
    }
  }
}
