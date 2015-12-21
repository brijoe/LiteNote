package org.bridge.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import org.bridge.litenote.R;

public class FloatingWindowManager {
    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;
    private static View floatInputWindow;
    private static WindowManager.LayoutParams params;

    /**
     * 单例模式获取WindowManager 实例
     *
     * @param context
     * @return
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 创建悬浮窗
     *
     * @param context
     */
    public static void createFloatWindow(Context context) {

        WindowManager wm = getWindowManager(context);
        //获取屏幕尺寸
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if (floatInputWindow == null) {
            floatInputWindow = LayoutInflater.from(context).inflate(R.layout.widget_window, null);
            if (params == null) {
                params = new WindowManager.LayoutParams();
                // 设置window 属性
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
                params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                // 设置悬浮窗的长得宽
                params.width = dm.widthPixels * 9 / 10;
                params.height = dm.heightPixels / 3;
                params.y = 200;
                params.dimAmount = 0.5f;
                //对齐方式
                params.gravity = Gravity.TOP;
            }
            wm.addView(floatInputWindow, params);
        }
    }

    /**
     * 移除悬浮窗
     *
     * @param context
     */
    public static void closeFloatWindow(Context context) {
        if (floatInputWindow != null) {
            WindowManager wm = getWindowManager(context);
            wm.removeView(floatInputWindow);
            floatInputWindow = null;
        }
    }
}
