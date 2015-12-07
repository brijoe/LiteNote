package org.bridge.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.kyleduo.switchbutton.SwitchButton;

import org.bridge.config.Config;
import org.bridge.data.LiteNoteSharedPrefs;
import org.bridge.litenote.R;

/**
 * 设置界面Activity
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    /**
     * sharedPreferences对象
     */
    private LiteNoteSharedPrefs liteNoteSharedPrefs;
    /**
     * 用于控制是否每次启动打开新便签的SwitchButton
     */
    private SwitchButton switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    /**
     * 初始化控件绑定和设置方法å
     */
    private void init() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        liteNoteSharedPrefs = LiteNoteSharedPrefs.getInstance(this);
        switchButton = (SwitchButton) findViewById(R.id.sb_use_toggle);
        Boolean startFlag = liteNoteSharedPrefs.getCacheBooleanPrefs(Config.SP_START_PUB_ACT, false);
        switchButton.setChecked(startFlag);
        switchButton.setOnClickListener(this);
    }

    /**
     * 打开应用市场进行评价
     *
     * @param v
     */
    public void startCommitIntent(View v) {
        String str = "market://details?id=" + getPackageName();
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse(str));
        startActivity(localIntent);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * 监听switchButton 状态改变
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sb_use_toggle:
                liteNoteSharedPrefs.cacheBooleanPrefs(Config.SP_START_PUB_ACT, switchButton.isChecked());
                break;
        }
    }
}
