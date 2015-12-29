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
import android.view.WindowManager;
import android.widget.EditText;

import org.bridge.config.Config;
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
            LogUtil.d(TAG, "noteBean-" + noteBean.getId() + " " + noteBean.getSyncState() + " " + noteBean.getEverGuid());
        }
        edtNoteContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                LogUtil.d(TAG, "键盘执行事件");
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
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
        intent.putExtra(Intent.EXTRA_TEXT, getContentText());
        startActivity(Intent.createChooser(intent, "选择要分享到的APP"));
    }

    /**
     * 获取当前文本编辑区的内容
     *
     * @return 内容的String对象
     */
    private String getContentText() {
        return edtNoteContent.getText().toString();
    }

    /**
     * 构造菜单项
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pub, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 菜单项被选择时的响应
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                handleDataOnExit();
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
        LogUtil.d(TAG, "光标位置：" + index);
        StringBuilder sb = new StringBuilder(getContentText());
        int brIndex = getContentText().lastIndexOf("\n", index - 1);
        int listDotIndex = (brIndex == -1) ? 0 : brIndex + 1;
        LogUtil.i(TAG, "当前行列表标志：" + isListCanDo(index) + "，brIndex=" + brIndex + "listDotIndex=" + listDotIndex);
        if (isListCanDo(index)) {
            //设置当前行列表样式
            sb.insert(listDotIndex, "▪");
            index++;
            LogUtil.d(TAG, "执行插入列表符");
        } else {
            //取消当前行列表样式
            sb.replace(listDotIndex, listDotIndex + 1, "");
            index--;
            LogUtil.d(TAG, "取消当前列表符");
        }
        edtNoteContent.setText(sb.toString());
        edtNoteContent.setSelection(index);
        LogUtil.i(TAG, getContentText());
    }

    /**
     * 根据传入的光标索引，判断当前行是否可以设置列表样式
     *
     * @param index
     * @return true 可以设置列表样式，false 不能设置列表样式
     */
    private boolean isListCanDo(int index) {
        //判断当前段落是要取消样式还是设置样式
        int brIndex = getContentText().lastIndexOf("\n", index - 1);
        int listDotIndex = (brIndex == -1) ? 0 : brIndex + 1;
        LogUtil.d(TAG, "isListCanDo---" + listDotIndex + "");
        if (!TextUtils.isEmpty(getContentText()) && listDotIndex < getContentText().length())
            return getContentText().charAt(listDotIndex) != '▪';
        else
            return true;
    }

    /**
     * 按下键盘回车键时执行的换行操作
     */
    private void enterKeyAction() {

        int index = edtNoteContent.getSelectionStart();//获取光标位置
        LogUtil.d(TAG, "键盘回车事件-光标位置：" + index);
        StringBuilder sb = new StringBuilder(getContentText());
        if (!isListCanDo(index)) {
            sb.insert(index, "\n▪");
            index += 2;
        } else {
            sb.insert(index, "\n");
            index++;

        }
        edtNoteContent.setText(sb.toString());
        edtNoteContent.setSelection(index);
        LogUtil.i(TAG, getContentText());
    }

    /**
     * 当退出当前Activity时执行的数据处理操作
     */
    private void handleDataOnExit() {
        if (noteBean != null)//编辑
            updateData();
        else//发表
            saveData();
    }

    /**
     * 新增，数据将数据保存到数据库
     */
    private void saveData() {
        Intent i = new Intent();
        String content = getContentText();
        if (!TextUtils.isEmpty(content)) {
            NoteBean noteBean = new NoteBean();
            noteBean.setContent(content);
            noteBean.setSyncState(Config.ST_ADD_NOT_SYNC);
            noteBean.setPubDate(DateUtil.getCurrentTime());
            noteBean.setModifyTime(DateUtil.getTimestamp());
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
        LogUtil.d(TAG, "执行更新记录-" + noteBean.getEverGuid());
        Intent i = new Intent();
        if (!getContentText().equals(noteBean.getContent())) {
            LogUtil.d(TAG, "内容变动-" + noteBean.getEverGuid());
            noteBean.setContent(getContentText());
            noteBean.setModifyTime(DateUtil.getTimestamp());
            if (noteBean.getSyncState() == Config.ST_ADD_AND_SYNC)
                noteBean.setSyncState(Config.ST_UPDATE_NOT_SYNC);
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


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean softkeyShow = (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        //返回键按下而且软件盘处于隐藏状态执行退出数据处理
        if (keyCode == KeyEvent.KEYCODE_BACK && softkeyShow) {
            handleDataOnExit();
            LogUtil.d(TAG, "返回键退出触发");
        }
        return super.onKeyUp(keyCode, event);
    }
}
