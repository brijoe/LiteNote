package org.bridge.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
    private LiteNoteDB liteNoteDB;

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     */
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("焦点变化" + hasFocus);
                setImeVisibility(hasFocus);
            }
        });
        liteNoteDB = LiteNoteDB.getInstance(context);
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
     * 保存内容至数据库
     */
    private void saveContent() {
        String content = this.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            NoteBean noteBean = new NoteBean();
            noteBean.setContent(content);
            noteBean.setPubDate(DateUtil.getCurrentTime());
            liteNoteDB.saveNoteItem(noteBean);
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
            System.out.println("自定义EditText捕获");
            //隐藏输入法，保存内容，并关闭浮动窗口
            setImeVisibility(false);
            saveContent();
            FloatingWindowManager.closeFloatWindow(context);
        }
        return false;
    }


}