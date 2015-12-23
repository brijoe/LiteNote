package org.bridge.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.bridge.data.LiteNoteDB;
import org.bridge.litenote.R;
import org.bridge.model.NoteBean;
import org.bridge.util.DateUtil;
import org.bridge.util.LogUtil;
import org.bridge.view.ConfirmDialog;

public class PubActivity extends BaseActivity {
    String TAG = "PubEvent";
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
    private String source;
    private boolean listFlag = false;
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
        source = getIntent().getStringExtra("source");

        if (noteBean != null) {
            actionBar.setTitle("编辑");
            edtNoteContent.setText(noteBean.getContent());//设置内容
        }
        edtNoteContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Toast.makeText(PubActivity.this,
                            "YOU CLICKED ENTER KEY",
                            Toast.LENGTH_LONG).show();
                    enterKeyAction();
                    return true;
                }
                return false;
            }
        });
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
                if (noteBean != null)//编辑
                    updateData();
                else//发表
                    saveData();
                break;
            case R.id.action_list:
                addListStyle();
                break;
            case R.id.action_share://分享笔记
                startShareIntent();
                break;
            case R.id.action_delete://删除笔记
                new ConfirmDialog(this, "确定要删除这条笔记吗？", new ConfirmDialog.Callback() {
                    @Override
                    public void perform() {
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
        LogUtil.d(TAG, "插入位置：" + index);
        // sbContent.insert(index, '*');

        StringBuilder sb = new StringBuilder(getText());
        if (isListCanDo(index)) {
            //设置当前行列表样式
            for (int i = index - 1; i >= 0; i--) {
                if (getText().charAt(i - 1) == '\n') {
                    sb.insert(i, "▪");
                    index++;
                    break;
                } else if (i == 1) {
                    sb.insert(0, "▪");
                    index++;
                    break;
                }
            }
        } else {
            //取消当前行列表样式
            for (int i = index - 1; i >= 0; i--) {
                if (getText().charAt(i) == '▪') {
                    sb.replace(i, i + 1, "");
                    index--;
                    break;
                }
            }
        }
        edtNoteContent.setText(sb.toString());
        edtNoteContent.setSelection(index);
        LogUtil.i(TAG, getText());
    }

    /**
     * 根据传入的光标索引，判断当前行是否可以设置列表样式
     *
     * @param index
     * @return true 可以设置列表样式，false 不能设置列表样式
     */
    private boolean isListCanDo(int index) {
        //判断当前段落是要取消样式还是设置样式
        int brIndex = getText().lastIndexOf("\n", index - 1);
        //找到换行符，不在第一行
        if (brIndex != -1) {
            return (getText().charAt(brIndex + 1) != '▪');
        }
        //未找到换行符，在第一行
        else
            return (getText().charAt(0) != '▪');

    }

    private void enterKeyAction() {

        int index = edtNoteContent.getSelectionStart();//获取光标位置
        //找到插入列表的位置
        LogUtil.d(TAG, "键盘回车事件-插入位置：" + index);
        StringBuilder sb = new StringBuilder(getText());
        if (!isListCanDo(index)) {
            sb.insert(index, "\n▪");
            index += 2;
        } else {
            sb.insert(index, "\n");
            index++;

        }
        edtNoteContent.setText(sb.toString());
        edtNoteContent.setSelection(index);
        LogUtil.i(TAG, getText());
    }

    private void showNoteContent(String content) {

        String test = "第一行abc\n逗比";
        edtNoteContent.setText(test);
        edtNoteContent.setSelection(getText().length());
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

    private void startMainIntent() {
        saveData();
        this.finish();
        // startActivity(new Intent(this, MainActivity.class));
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
