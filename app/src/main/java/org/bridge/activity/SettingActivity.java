package org.bridge.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.edam.type.User;
import com.kyleduo.switchbutton.SwitchButton;

import org.bridge.config.Config;
import org.bridge.data.LiteNoteSharedPrefs;
import org.bridge.litenote.R;
import org.bridge.task.GetUserInfoTask;
import org.bridge.task.LogOutTask;
import org.bridge.util.LogUtil;
import org.bridge.view.ConfirmDialog;

/**
 * 设置界面Activity
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "settingActivity";
    /**
     * sharedPreferences对象
     */
    private LiteNoteSharedPrefs liteNoteSharedPrefs;
    /**
     * 用于控制是否每次启动打开新便签的SwitchButton
     */
    private SwitchButton switchButton;
    /**
     * 绑定设置的文本信息
     */
    private TextView txtBind;
    /**
     * 点击绑定设置的viewGroup区域
     */
    private LinearLayout vgBind;
    /**
     * 绑定是否成功的标志
     */
    private Boolean bindFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    /**
     * 初始化控件绑定和设置方法
     */
    private void init() {
        //ActionBar 设置向上返回
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //控件绑定
        liteNoteSharedPrefs = LiteNoteSharedPrefs.getInstance(this);
        switchButton = (SwitchButton) findViewById(R.id.sb_use_toggle);
        txtBind = (TextView) findViewById(R.id.bind_text);
        vgBind = (LinearLayout) findViewById(R.id.bind_evernote);
        //事件监听器设置
        switchButton.setOnClickListener(this);
        vgBind.setOnClickListener(this);

        //switchButton 设置
        Boolean startFlag = liteNoteSharedPrefs.getCacheBooleanPrefs(Config.SP_START_PUB_ACT, false);
        switchButton.setChecked(startFlag);
        //绑定设置项文字动态设定
        bindFlag = liteNoteSharedPrefs.getCacheBooleanPrefs(Config.SP_EVERNOTE_BIND_FLAG, false);
        bindAction(bindFlag);

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
        //重新获取绑定标识
        bindFlag = liteNoteSharedPrefs.getCacheBooleanPrefs(Config.SP_EVERNOTE_BIND_FLAG, false);
        switch (v.getId()) {
            case R.id.bind_evernote:
                if (bindFlag) {
                    //已经绑定，执行解绑
                    new ConfirmDialog(this, "确定要解除绑定吗？", new ConfirmDialog.Callback() {
                        @Override
                        public void perform() {
                            logout();
                        }
                    });
                } else {
                    //尚未绑定，执行绑定
                    EvernoteSession.getInstance().authenticate(this);
                }

                break;
            case R.id.sb_use_toggle:
                liteNoteSharedPrefs.cacheBooleanPrefs(Config.SP_START_PUB_ACT, switchButton.isChecked());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //印象笔记绑定回调
            case EvernoteSession.REQUEST_CODE_LOGIN:
                LogUtil.i(TAG, "印象笔记绑定回调");
                if (resultCode == Activity.RESULT_OK) {
                    bindAction(true);
                }
                break;
        }
    }

    private void logout() {
        new LogOutTask(new LogOutTask.Callback() {
            @Override
            public void onCall(boolean result) {
                if (result) {
                    bindAction(false);
                } else {
                    //解绑失败
                    Toast.makeText(SettingActivity.this, "解绑失败，请稍后尝试~~", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void bindAction(boolean bindFlag) {
        if (bindFlag) {
            liteNoteSharedPrefs.cacheBooleanPrefs(Config.SP_EVERNOTE_BIND_FLAG, true);
            String account = liteNoteSharedPrefs.getCacheStringPrefs(Config.SP_EVERNOTE_ACCOUNT);
            if (!TextUtils.isEmpty(account)) {
                txtBind.setText("解除与" + account + "的绑定");
                return;
            }
//执行异步任务获取
            new GetUserInfoTask(this, new GetUserInfoTask.Callback() {
                @Override
                public void onCall(User user) {
                    if (user != null) {
                        txtBind.setText("解除与" + user.getUsername() + "的绑定");
                        liteNoteSharedPrefs.cacheStringPrefs(Config.SP_EVERNOTE_ACCOUNT, user.getUsername());
                        LogUtil.i(TAG, user.getUsername());
                    } else
                        Toast.makeText(SettingActivity.this, "获取账号信息失败~", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            liteNoteSharedPrefs.cacheStringPrefs(Config.SP_EVERNOTE_ACCOUNT, null);
            liteNoteSharedPrefs.cacheBooleanPrefs(Config.SP_EVERNOTE_BIND_FLAG, false);
            txtBind.setText("自动同步到印象笔记");
        }
    }
}
