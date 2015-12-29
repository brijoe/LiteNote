package org.bridge.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.bridge.view.FloatingWindowManager;

/**
 * 自定义的广播接收器用于接收悬浮窗显示广播
 */
public class FloatingWindowReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("收到广播");
        FloatingWindowManager.createFloatWindow(context);
    }
}
