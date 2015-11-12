package org.bridge.litenote;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.bridge.util.Logger;

public class MainActivity extends Activity {
    String TAG = "info";
    private Intent i = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bindEverNote://绑定印象笔记
                break;
            case R.id.action_setting://打开设置界面
                Logger.i(TAG, "点击了设置界面");
                i.setClass(this, SettingActivity.class);
                startActivity(i);//页面跳转
                break;
            case R.id.action_feedback://添加反馈
                startSendEmailIntent();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * 发送反馈邮件的方法
     */
    private void startSendEmailIntent() {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:1650730996@qq.com"));
        data.putExtra(Intent.EXTRA_SUBJECT, "反馈");
        data.putExtra(Intent.EXTRA_TEXT, "输入您的意见或建议！");
        startActivity(data);
    }
}
