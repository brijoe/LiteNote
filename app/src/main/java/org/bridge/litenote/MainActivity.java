package org.bridge.litenote;

import android.app.Activity;
import android.content.Intent;
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
                break;
            case R.id.action_feedback://添加反馈
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        startActivity(i);//页面跳转
        return true;
    }
}
