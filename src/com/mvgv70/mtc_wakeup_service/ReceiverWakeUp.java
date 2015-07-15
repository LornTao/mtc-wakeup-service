package com.mvgv70.mtc_wakeup_service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class ReceiverWakeUp extends BroadcastReceiver {
	
  private static final String TAG = "mtc-wakeup-service";  
  private final static String INI_FILE_NAME = Environment.getExternalStorageDirectory().getPath()+"/mtc-wakeup-service/mtc-wakeup-service.ini";

  @Override
  public void onReceive(Context context, Intent intent) 
  {
    String action = intent.getAction();
    Log.d(TAG,"ReceiverWakeUp.onReceive "+action);
    // стартуем сервисы
    startServices(context);
  }
  
  // запуск сервисов
  private void startServices(Context context)
  {
    BufferedReader br;
    try 
    {
      br = new BufferedReader(new FileReader(INI_FILE_NAME));
      try 
      {
        String line = br.readLine();
        while (line != null) 
        {
          Log.d(TAG,line);
          if (line.startsWith("intent:"))
          {
            // посылаем интент
            String intentName = line.substring(7, line.length());
            Log.d(TAG,"send intent "+intentName);
           context.sendBroadcast(new Intent(intentName));
           }
          else if (line.startsWith("service:"))
          {
            // запуск сервиса
            String serviceName = line.substring(8, line.length());
            int pos = serviceName.indexOf("/");
            if (pos >= 0)
            {
              // разбираем формат имя пакета/имя сервиса
              String packageName = serviceName.substring(0,pos);
              String className = serviceName.substring(pos+1,serviceName.length());
              Log.d(TAG,"run service "+packageName+"/"+className);
              ComponentName cn = new ComponentName(packageName,className);
              Intent intent = new Intent();
              intent.setComponent(cn);
              try
              {
                // пытаемся стартовать сервис
                ComponentName cr = context.startService(intent);
                if (cr == null)
                  Log.w(TAG,"service "+serviceName+" not found");
              }
              catch (Exception e)
              {	      
                Log.e(TAG,e.getMessage());
              }
            }
            else
              Log.w(TAG,"incorrect service declaration");
	      }
	      else if (line.startsWith("activity:"))
	      {
	    	// запуск активити
	    	String activityName = line.substring(9, line.length());
	        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(activityName);
	        if (appIntent != null)
	          context.startActivity(appIntent);
	        else
	          Log.w(TAG,"no activity found for "+activityName);
	      }
          else if (line.startsWith("root:"))
          {
            // запуск команды от имени root
            String cmd = line.substring(5, line.length());
            Log.d(TAG,"exec "+cmd);
            executeCmd(cmd);
	      }
	      else if (line.startsWith("#"))
	      {
            // комментарий
	      }
	      else if (line.trim().isEmpty())
	      {
            // пустая строка
	      }
	      else
	        Log.w(TAG,"unknown string: "+line);
		  // следующая строка
		  line = br.readLine();
		}
	  } 
	  finally
	  {
	    br.close();
      }
	} 
    catch (Exception e) 
    {
	  Log.e(TAG,e.getMessage());
	}
  }
  
  //выполнение команды с привилегиями root
  private boolean executeCmd(String cmd)
  {
    Log.d(TAG,"> "+cmd);
    // su (as root)
    Process process = null;
 	DataOutputStream os = null;
    InputStream err = null;
 	boolean errflag = true;
 	try 
 	{
 	  process = Runtime.getRuntime().exec("su");
 	  os = new DataOutputStream(process.getOutputStream());
 	  err = process.getErrorStream();
 	  os.writeBytes(cmd+" \n");
      os.writeBytes("exit \n");
      os.flush();
      os.close();
      process.waitFor();
      // анализ ошибок
      byte[] buffer = new byte[1024];
      int len = err.read(buffer);
      if (len > 0)
      {
        String errmsg = new String(buffer,0,len);
        Log.e(TAG,errmsg);
      } 
      else
        errflag = false;
    } 
 	catch (IOException e) 
 	{
      Log.e(TAG,"IOException: "+e.getMessage());
    }
 	catch (InterruptedException e) 
 	{
      Log.e(TAG,"InterruptedException: "+e.getMessage());
    }
 	return (!errflag);
  }

}
