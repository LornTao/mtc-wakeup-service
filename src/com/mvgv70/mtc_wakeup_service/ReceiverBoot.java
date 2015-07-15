package com.mvgv70.mtc_wakeup_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverBoot extends BroadcastReceiver {
	
  private static final String TAG = "mtc-wakeup-service";  

  @Override
  public void onReceive(Context context, Intent intent) 
  {
    String action = intent.getAction();
    Log.d(TAG,"ReceiverBoot.onReceive "+action);
    // стартуем сервис
    if (!ServiceMain.isRunning)
      context.startService(new Intent(context, ServiceMain.class));
  }
}
