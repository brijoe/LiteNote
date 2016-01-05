package org.bridge.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import org.bridge.litenote.R;

public class RecSendActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowDisplay();
        setContentView(R.layout.activity_rec_send);
    }

    private void setWindowDisplay() {
        WindowManager wm = getWindowManager();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        //获取屏幕尺寸
        DisplayMetrics dm = getResources().getDisplayMetrics();
        // 设置window 属性
        // params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        // 设置悬浮窗的长得宽
        params.width = dm.widthPixels * 9 / 10;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.dimAmount = 0.5f;
        //对齐方式
        // params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}
