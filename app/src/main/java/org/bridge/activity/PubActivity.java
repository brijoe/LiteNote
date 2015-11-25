package org.bridge.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.bridge.data.LiteNoteDB;
import org.bridge.model.NoteBean;
import org.bridge.litenote.R;
import org.bridge.util.DateUtil;
import org.bridge.util.Logger;
import org.bridge.view.ConfirmDialog;

public class PubActivity extends BaseActivity {
    String TAG = "PubActivity";
    /**
     * 文本编辑框
     */
    private EditText edtNoteContent;
    /**
     * 用于数据操作的数据库对象
     */
    private LiteNoteDB liteNoteDB;
    /**
     * MainActivity传递的NoteEntry对象
     */
    private NoteBean noteBean;
    private int currentLine = 1;//当前段落
    private StringBuffer sbContent = new StringBuffer("\n");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        edtNoteContent = (EditText) findViewById(R.id.edtNoteContent);
        liteNoteDB = LiteNoteDB.getInstance(this);
        noteBean = getIntent().getParcelableExtra("NoteItem");//获取传递对象
        if (noteBean != null) {
            actionBar.setTitle("编辑");
            edtNoteContent.setText(noteBean.getContent());//设置内容
        }
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
                if (noteBean != null)
                    updateData();
                else
                    saveData();
                break;
            case R.id.action_list:
                addListStyle();
                break;
            case R.id.action_share://分享笔记
                startShareIntent();
                break;
            case R.id.action_delete://删除笔记
                new ConfirmDialog(this, new ConfirmDialog.DelCallback() {
                    @Override
                    public void delNoteItems() {
                        delData();
                    }
                });
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
        Logger.d(TAG, "插入位置：" + index);
        // sbContent.insert(index, '*');
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

    /**
     * 将数据保存到数据库
     */
    private void saveData() {
        Intent i = new Intent();
        String content = getText();
        if (!TextUtils.isEmpty(content)) {
            NoteBean noteBean = new NoteBean();
            noteBean.setContent(content);
            noteBean.setPubDate(DateUtil.getCurrentTime());
            liteNoteDB.saveNoteItem(noteBean);
            i.putExtra("add", true);

        } else {
            i.putExtra("add", false);
        }
        setResult(RESULT_OK, i);
        finish();
    }

    /**
     * 更新此条记录的内容
     */
    private void updateData() {
        Intent i = new Intent();
        if (!getText().equals(noteBean.getContent())) {
            noteBean.setContent(getText());
            liteNoteDB.updateNoteItem(noteBean);
            i.putExtra("edit", true);
        } else {
            i.putExtra("edit", false);
        }
        setResult(RESULT_OK, i);
        finish();
    }

    /**
     * 删除当前数据
     */
    private void delData() {
        Intent i = new Intent();
        if (noteBean != null) {
            liteNoteDB.deleteNoteItem(new int[]{noteBean.getId()});
            i.putExtra("delete", true);
        } else {
            i.putExtra("delete", false);
        }
        setResult(RESULT_OK, i);
        finish();
    }
}
