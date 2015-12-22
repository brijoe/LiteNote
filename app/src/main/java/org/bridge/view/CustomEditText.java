package org.bridge.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.bridge.data.LiteNoteDB;
import org.bridge.model.NoteBean;
import org.bridge.util.DateUtil;

/**
 * 自定义的EditText，用于悬浮窗窗口输入
 */
public class CustomEditText extends EditText {
    /**
     * Context 对象
     */
    private Context context;
    /**
     * 用于数据存储的LiteNoteDB的对象
     */
    private LiteNoteDB liteNoteDB;
    /**
     * home键监听的BroadCastReceiver对象
     */
    private HomeListenerReceiver homeListenerReceiver;

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     */
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //  this.setHint("输入你的想法");
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("焦点变化" + hasFocus);
                setImeVisibility(hasFocus);
            }
        });
        liteNoteDB = LiteNoteDB.getInstance(context);
        homeListenerReceiver = new HomeListenerReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(homeListenerReceiver, homeFilter);
        System.out.println("注册监听");
    }

    /**
     * 用于控制输入法软键盘显示的Runnable对象
     */
    private Runnable mShowImeRunnable = new Runnable() {
        public void run() {
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.showSoftInput(CustomEditText.this, 0);
            }
        }
    };

    /**
     * 控制输入法显示的方法
     *
     * @param visible
     */
    private void setImeVisibility(final boolean visible) {
        if (visible) {
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    /**
     * 监听home键广播的内部类BroadcastReceiver
     */
    class HomeListenerReceiver extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null && reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    // 处理自己的逻辑
                    System.out.println("home键被按下");
                    closeAction();
                }
            }
        }
    }

    /**
     * 保存内容至数据库
     */
    private void saveContent() {
        String content = this.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            NoteBean noteBean = new NoteBean();
            noteBean.setContent(content);
            noteBean.setPubDate(DateUtil.getCurrentTime());
            liteNoteDB.saveNoteItem(noteBean);
            Toast.makeText(context, "内容已保存！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 关闭悬浮窗的一系列操作
     */
    private void closeAction() {
        //隐藏输入法，保存内容，并关闭浮动窗口
        setImeVisibility(false);
        saveContent();
        FloatingWindowManager.closeFloatWindow(context);
        // 反注册home监听
        if (homeListenerReceiver != null) {
            context.unregisterReceiver(homeListenerReceiver);
            System.out.println("反注册监听");
        }
    }

    /**
     * 处理按键响应
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //隐藏输入法，保存内容，并关闭浮动窗口
            closeAction();
        }
        return false;
    }


}