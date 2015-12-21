package org.bridge.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.bridge.view.FloatingWindowManager;

/**
 * Created by bridgegeorge on 15/12/14.
 */
public class FloatingWindowReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("收到广播");
        FloatingWindowManager.createFloatWindow(context);
    }
}
