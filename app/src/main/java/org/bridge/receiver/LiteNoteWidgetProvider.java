package org.bridge.receiver;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.bridge.activity.MainActivity;
import org.bridge.activity.PubActivity;
import org.bridge.litenote.R;
import org.bridge.view.FloatingWindowManager;

public class LiteNoteWidgetProvider extends AppWidgetProvider {
    private final String ACTION = "org.bridge.action.SHOW_FLOATING_WINDOW";

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(ACTION)) {
//            Toast.makeText(context, "弹出了！！！！", Toast.LENGTH_SHORT).show();
//            FloatingWindowManager.createFloatWindow(context);
//        }
//        Log.i("yao", "HelloWidgetProvider --> onReceive");
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            Intent intent1 = new Intent(context, PubActivity.class);
            PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);
            Intent intent2 = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
            Intent intent3 = new Intent(context, FloatingWindowReceiver.class);
            intent3.setAction(ACTION);
            PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.btnStartPub, pendingIntent1);
            views.setOnClickPendingIntent(R.id.tvStartMain, pendingIntent2);
            views.setOnClickPendingIntent(R.id.layoutStartWindow, pendingIntent3);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    /**
     * 当一个App Widget从桌面上删除时调用
     *
     * @param context
     * @param appWidgetIds
     */

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.i("yao", "HelloWidgetProvider --> onDeleted");
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当这个App Widget第一次被放在桌面上时调用(同一个App Widget可以被放在桌面上多次，所以会有这个说法)
     *
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        Log.i("yao", "HelloWidgetProvider --> onEnabled");
        super.onEnabled(context);
    }

    /**
     * 当这个App Widget的最后一个实例被从桌面上移除时会调用该方法。
     *
     * @param context
     */
    //
    @Override
    public void onDisabled(Context context) {
        Log.i("yao", "HelloWidgetProvider --> onDisabled");
        super.onDisabled(context);
    }


}
