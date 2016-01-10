package org.bridge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.bridge.config.Config;
import org.bridge.data.LiteNoteDB;
import org.bridge.litenote.R;
import org.bridge.model.NoteBean;
import org.bridge.util.DateUtil;
import org.bridge.util.KeyBoardUtil;

/**
 * 提供给第三方应用保存纯文本内容至LiteNote的Activity
 */
public class RecSendActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtSharedContent;
    private Button btnRecCancel;
    private Button btnRecSave;
    private LiteNoteDB liteNoteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowDisplay();
        setContentView(R.layout.activity_rec_send);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        //view 绑定
        edtSharedContent = (EditText) findViewById(R.id.edtSharedContent);
        btnRecCancel = (Button) findViewById(R.id.btnRecCancel);
        btnRecSave = (Button) findViewById(R.id.btnRecSave);
        //数据库操作对象初始化
        liteNoteDB = LiteNoteDB.getInstance(this);
        //接受并设置文字
        Intent intent = getIntent();
        String sharedStr = intent.getStringExtra(Intent.EXTRA_TEXT);
        edtSharedContent.setText(sharedStr);
        //设置监听
        btnRecCancel.setOnClickListener(this);
        btnRecSave.setOnClickListener(this);
        KeyBoardUtil.openKeybord(edtSharedContent, this);

    }

    /**
     * 设置窗口属性
     */
    private void setWindowDisplay() {
        WindowManager wm = getWindowManager();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        //获取屏幕尺寸
        DisplayMetrics dm = getResources().getDisplayMetrics();
        // 设置window 属性
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        // 设置悬浮窗的长得宽
        params.width = dm.widthPixels * 9 / 10;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.dimAmount = 0.5f;
        params.gravity = Gravity.TOP;
        params.y = 300;
        //设置属性给窗口
        getWindow().setAttributes(params);
    }

    /**
     * 保存按钮执行的操作
     */

    private void saveAction() {
        Intent i = new Intent();
        String content = edtSharedContent.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            NoteBean noteBean = new NoteBean();
            noteBean.setContent(content);
            noteBean.setSyncState(Config.ST_ADD_NOT_SYNC);
            noteBean.setPubDate(DateUtil.getCurrentTime());
            noteBean.setModifyTime(DateUtil.getTimestamp());
            liteNoteDB.saveNoteItem(noteBean);
            Toast.makeText(this, "内容已保存~~", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * view点击监听事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRecCancel:
                finish();
                break;
            case R.id.btnRecSave:
                saveAction();
                break;
        }

    }
}
