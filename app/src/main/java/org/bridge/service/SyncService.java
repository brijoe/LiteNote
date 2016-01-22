package org.bridge.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import org.bridge.receiver.SyncAlarmReceiver;
import org.bridge.task.SyncNoteListTask;
import org.bridge.util.LogUtil;

/**
 * 后台自动同步service
 */
public class SyncService extends Service {
    private final String TAG = "SyncService";

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "SyncService onCreate");
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "SyncService onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "SyncService onStartCommand");
        //开始定时任务

        new Thread(new Runnable() {
            @Override
            public void run() {
                new SyncNoteListTask();
            }
        }).start();
        //设置定时周期
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // int period = 3 * 60 * 1000;//周期3分钟
        int period = 5 * 1000;//周期3分钟
        long triggerAtTime = SystemClock.elapsedRealtime() + period;
        Intent i = new Intent(this, SyncAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
