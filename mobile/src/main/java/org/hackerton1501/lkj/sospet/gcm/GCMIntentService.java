/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hackerton1501.lkj.sospet.gcm;

import java.util.Iterator;
import java.util.List;

import kr.co.i2max.mobile.nia.HomeFrameActivity;
import kr.co.i2max.mobile.nia.R;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GCMIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1501;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMIntentService() {
        super("GCMIntentService");
    }
    public static final String TAG = "GCMIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
    	Log.i(TAG, "GCM onHandleIntent: start");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(), 0);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString(), 0);
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification(extras.getString("message"), 1);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, int badge) {
    	Log.d("sendNotification", msg);
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        wakeUp();
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
    	    Intent  kIntent = new Intent();
    	    kIntent.setAction("kr.co.i2max.RECEIVE_NOTI");
    	    PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, kIntent, PendingIntent.FLAG_UPDATE_CURRENT | Notification.FLAG_AUTO_CANCEL);
    	
    	
    	if (Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 > Build.VERSION.SDK_INT) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
            .setSmallIcon(icon)
            .setContentTitle("알림 메세지")
            .setWhen(when)
            .setStyle(new NotificationCompat.BigTextStyle())
            .setContentText(msg)
            .setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    	} else {
    	    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this); 
    	    mBuilder.setSmallIcon(R.drawable.ic_launcher);//required 
    	    mBuilder.setContentTitle("알림 메세지");//required 
    	    mBuilder.setContentText(msg);//required 
//    	    mBuilder.setTicker("tickerText");//optional 
    	    mBuilder.setNumber(10);//optional 
    	    mBuilder.setAutoCancel(true); 
    	    mBuilder.setContentIntent(contentIntent);
    	    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); 
    	    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
          
    	}
        //badge 표시 처리
        int badgeCount = badge;
//        Intent intent = ExIntentUtil.setPushBadge(badgeCount, getPackageName(), LoadActivity.class.getName());
//        sendBroadcast(intent);
    }
    private boolean isActivityBackgroundRunning(Context aContext)
    {
        ActivityManager activityManager = (ActivityManager) aContext.getSystemService(aContext.ACTIVITY_SERVICE);
        List<RunningTaskInfo> info;
        info = activityManager.getRunningTasks(activityManager.getRunningAppProcesses().size());
        int i = 0; 
        for (Iterator iterator = info.iterator(); iterator.hasNext();)  {
            RunningTaskInfo runningTaskInfo = (RunningTaskInfo) iterator.next();
            Log.e("ABCApplication","running Activity Name : " + runningTaskInfo.topActivity.getClassName());
            if(runningTaskInfo.topActivity.getClassName().equals("kr.co.i2max.mobile.nia.HomeFrameActivity")) {
                
                Log.e("ABCApplication","ABCApplication is running ");
                if(i == 0) {
                    return false;
                } else {
                    return true;
                }
            }
            i++;
        }
        return true;
    }
    private boolean isActivityForegroundRunning(Context aContext) {
        ActivityManager activityManager = (ActivityManager) aContext.getSystemService(aContext.ACTIVITY_SERVICE);
        List<RunningTaskInfo> info;
        info = activityManager.getRunningTasks(activityManager.getRunningAppProcesses().size());
        int i = 0; 
        for (Iterator iterator = info.iterator(); iterator.hasNext();)  {
            RunningTaskInfo runningTaskInfo = (RunningTaskInfo) iterator.next();
            Log.e("ABCApplication","running Activity Name : " + runningTaskInfo.topActivity.getClassName());
            if(runningTaskInfo.topActivity.getClassName().equals("kr.co.i2max.mobile.nia.HomeFrameActivity")) {
                
                Log.e("ABCApplication","ABCApplication is running ");
                if(i == 0) {
                    return true;
                } else {
                    return false;
                }
            }
            i++;
        }
        return false;
    }
    protected void wakeUp() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
    }
}
