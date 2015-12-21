package org.bridge.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import org.bridge.util.LogUtil;

/**
 * Activity基类，用来进行调试
 */
public class BaseActivity extends Activity {
    private String TAG = "AC_STATE";
    private String CLASSNAME = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.i(TAG, CLASSNAME + "-onCreate");
        super.onCreate(savedInstanceState);
        //所有Activity只允许竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onStart() {
        LogUtil.i(TAG, CLASSNAME + "-onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        LogUtil.i(TAG, CLASSNAME + "-onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtil.i(TAG, CLASSNAME + "-onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtil.i(TAG, CLASSNAME + "-onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.i(TAG, CLASSNAME + "-onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        LogUtil.i(TAG, CLASSNAME + "-onRestart");
        super.onRestart();
    }

}
