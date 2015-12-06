package org.bridge.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import org.bridge.util.Logger;

/**
 * Activity基类，用来进行调试
 */
public class BaseActivity extends Activity {
    private String TAG = "AC_STATE";
    private String CLASSNAME = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.i(TAG, CLASSNAME + "-onCreate");
        super.onCreate(savedInstanceState);
        // setStatusBar();
    }

    @Override
    protected void onStart() {
        Logger.i(TAG, CLASSNAME + "-onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Logger.i(TAG, CLASSNAME + "-onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Logger.i(TAG, CLASSNAME + "-onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Logger.i(TAG, CLASSNAME + "-onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Logger.i(TAG, CLASSNAME + "-onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Logger.i(TAG, CLASSNAME + "-onRestart");
        super.onRestart();
    }

    /**
     * 设置Android 4.4以上版本使用沉浸式状态栏
     */
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
