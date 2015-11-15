package org.bridge.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import org.bridge.litenote.R;
import org.bridge.util.Logger;

public class PubActivity extends BaseActivity {
    String TAG = "PubActivity";
    private EditText edtNoteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        edtNoteContent = (EditText) findViewById(R.id.edtNoteContent);
    }

    /**
     * 分享内容的方法
     */
    private void startShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, edtNoteContent.getText().toString());
        startActivity(Intent.createChooser(intent, "选择要分享到的APP"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pub, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_list:
                addListStyle();
                break;
            case R.id.action_share://分享笔记
                startShareIntent();
                break;
            case R.id.action_delete://删除笔记
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * 为文本添加列表样式的方法
     */
    private void addListStyle() {
        String content = edtNoteContent.getText().toString();
        StringBuilder sb = new StringBuilder();
        sb.append("▶");
        sb.append(content);
        edtNoteContent.setText(sb.toString());
        edtNoteContent.setSelection(edtNoteContent.getText().toString().length());
        Logger.i(TAG, edtNoteContent.getText().toString());
    }

}
