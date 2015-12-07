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

}
