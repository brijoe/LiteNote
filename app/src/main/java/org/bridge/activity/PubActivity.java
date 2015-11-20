package org.bridge.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.bridge.entry.NoteEntry;
import org.bridge.litenote.R;
import org.bridge.util.Logger;

public class PubActivity extends BaseActivity {
    String TAG = "PubActivity";
    private EditText edtNoteContent;
    private int currentLine = 1;//当前段落
    private StringBuffer sbContent = new StringBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        edtNoteContent = (EditText) findViewById(R.id.edtNoteContent);
        NoteEntry noteEntry = getIntent().getParcelableExtra("NoteItem");//获取传递对象
        edtNoteContent.setText(noteEntry.getContent());//设置内容
        // showNoteContent("");
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

    /**
     * 监听键盘回车事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            Toast.makeText(this,
                    "YOU CLICKED ENTER KEY",
                    Toast.LENGTH_LONG).show();
            sbContent.append(getText()).append("\n");
        }
        return super.dispatchKeyEvent(event);
    }

    private String getText() {
        return edtNoteContent.getText().toString();
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
        int index = edtNoteContent.getSelectionStart();//获取光标位置
        //找到插入列表的位置

        String content = edtNoteContent.getText().toString();
        StringBuilder sb = new StringBuilder();
        sb.append("▶");
        sb.append(content);
        edtNoteContent.setText(sb.toString());
        edtNoteContent.setSelection(edtNoteContent.getText().toString().length());
        Logger.i(TAG, edtNoteContent.getText().toString());
    }

    private void showNoteContent(String content) {

        String test = "第一行abc\n逗比";
        edtNoteContent.setText(test);
    }

}
