package org.hackerton1501.lkj.sospet.gcm;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.hackerton1501.lkj.sospet.ReceiveSOSActivity;

import java.util.List;

public class NotificationReceiver extends BroadcastReceiver{
    
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isRunning = isServiceRunningCheck(context);
        Log.e("isRunning", "Running : " + isRunning);
//        if(isRunning) {
//            setBackGroundAppToForeGround(context);
//        } else {
            Intent kIntent = new Intent(context, ReceiveSOSActivity.class);
            kIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(kIntent);
//        }
    }
    private void setBackGroundAppToForeGround(Context aContext) {
        ActivityManager activityManager = (ActivityManager) aContext.getSystemService(aContext.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks;
        tasks = activityManager.getRunningTasks(activityManager.getRunningAppProcesses().size());
            int tasksSize = tasks.size();
            for(int i = 0; i < tasksSize;  i++) {
            RunningTaskInfo taskinfo = tasks.get(i);
                    if(taskinfo.topActivity.getPackageName().equals("org.hackerton1501.lkj.sospet")) {
                        
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                            activityManager.moveTaskToFront(taskinfo.id, 0);
                        } else {

                        }
                        
                    }
            }
    }
    
    public boolean isServiceRunningCheck(Context aContext) {
        ActivityManager manager = (ActivityManager) aContext.getSystemService(Activity.ACTIVITY_SERVICE);
        for (RunningTaskInfo task : manager.getRunningTasks(Integer.MAX_VALUE)) {
            Log.e("topActivity", "Activity : " + task.topActivity.getClassName());
            if ("org.hackerton1501.lkj.sospet.MainActivity".equals(task.topActivity.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
