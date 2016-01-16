package org.bridge.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.bridge.service.SyncService;

/**
 * 进行接收同步任务广播接收器
 */
public class SyncAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SyncService.class);
        context.startService(i);
    }
}
