package org.bridge.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.bridge.litenote.R;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
}
