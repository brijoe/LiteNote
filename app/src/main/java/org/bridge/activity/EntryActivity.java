package org.bridge.activity;

import android.content.Intent;
import android.os.Bundle;

import org.bridge.config.Config;
import org.bridge.data.LiteNoteSharedPrefs;
import org.bridge.litenote.R;

/**
 * 程序入口Activity类，确定Activity跳转方向和及其他初始化操作
 */
public class EntryActivity extends BaseActivity {
    private LiteNoteSharedPrefs liteNoteSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        reDirectedIntent();
    }

    /**
     * 控制活动跳转函数
     */
    private void reDirectedIntent() {
        liteNoteSharedPrefs = LiteNoteSharedPrefs.getInstance(this);
        Intent intent = new Intent();
        // 读取缓存字段，决定跳转活动
        Boolean flag = liteNoteSharedPrefs.getCacheBooleanPrefs(Config.SP_START_PUB_ACT);
        if (flag) {
            intent.setClass(this, PubActivity.class);
        } else {
            intent.setClass(this, MainActivity.class);
        }
        startActivity(intent);
        finish();

    }
}
