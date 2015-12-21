package org.bridge.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;

import org.bridge.adapter.NoteItemAdapter;
import org.bridge.config.Config;
import org.bridge.data.LiteNoteDB;
import org.bridge.data.LiteNoteSharedPrefs;
import org.bridge.litenote.R;
import org.bridge.model.NoteBean;
import org.bridge.task.GetUserInfoTask;
import org.bridge.util.LogUtil;
import org.bridge.view.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "info";
    /**
     * 指定菜单项要跳转的Intent
     */
    private Intent i = new Intent();
    /**
     * 用于构造卡片布局的RecyclerView
     */
    private RecyclerView mRecyclerView;
    /**
     * 用于指定RecyclerView布局的布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;
    /**
     * NoteItem适配器
     */
    private NoteItemAdapter mAdapter;
    /**
     * NoteItem数据对象列表
     */
    private List<NoteBean> items = new ArrayList<>();
    /**
     * 当前Activity 的ActionBar 对象
     */
    private ActionBar actionBar;
    /**
     * 需要初始化的DB对象
     */
    private LiteNoteDB liteNoteDB;
    /**
     * SharedPreference对象
     */
    private LiteNoteSharedPrefs liteNoteSharedPrefs;
    /**
     * 撤销按钮
     */
    private ImageButton btnUndo;
    /**
     * 删除按钮
     */
    private ImageButton btnDelete;
    /**
     * 表示删除操作是否被激活
     */
    private Boolean isDelActivated = false;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.i(TAG, "oncreate执行");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        liteNoteSharedPrefs = LiteNoteSharedPrefs.getInstance(this);
        // 读取缓存字段，决定跳转活动
        boolean startPrefs = liteNoteSharedPrefs.getCacheBooleanPrefs(Config.SP_START_PUB_ACT, false);
        if (startPrefs) {
            Intent intent = new Intent();
            intent.setClass(this, PubActivity.class);
            startActivity(intent);
        }
        //初始化
        init();
        // invalidateOptionsMenu();

    }

    /**
     * 初始化控件绑定和设置方法
     */
    private void init() {
        actionBar = getActionBar();
        btnUndo = (ImageButton) findViewById(R.id.undo_btn);
        btnDelete = (ImageButton) findViewById(R.id.delete_btn);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnUndo.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        //创建一个数据库实例
        liteNoteDB = LiteNoteDB.getInstance(this);
        //构建一个数据源
        initData();
    }

    /**
     * 初始化RecyclerView布局并设置数据
     */
    private void initData() {
        //创建布局
        mLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        items = liteNoteDB.queryAllNoteItem();
        mAdapter = new NoteItemAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LogUtil.i(TAG, "菜单项构造执行");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        //读取印象笔记绑定字段，决定菜单显示
        Boolean bindPrefs = liteNoteSharedPrefs.getCacheBooleanPrefs(Config.SP_EVERNOTE_BIND_FLAG, false);
        if (bindPrefs) {
            MenuItem menuItem = menu.findItem(R.id.action_bindEverNote);
            menuItem.setTitle(R.string.syncNow);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bindEverNote://绑定印象笔记
                if (liteNoteSharedPrefs.getCacheBooleanPrefs(Config.SP_EVERNOTE_BIND_FLAG, false)) {
                    //已经绑定成功，执行同步任务

                } else {
                    //尚未绑定，执行绑定
                    EvernoteSession.getInstance().authenticate(MainActivity.this);

                }
                break;
            case R.id.action_setting://打开设置界面
                LogUtil.i(TAG, "点击了设置界面");
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

    public void handleDelActionLayout(Boolean activated) {
        if (activated) {
            actionBar.hide();
            // this.onResume();
        } else {
            actionBar.show();
        }
        isDelActivated = activated;
    }

    public void setDelActionCount(int count) {
        ((TextView) findViewById(R.id.delete_tips)).setText("选择了" + count + "个");
    }

    public void delNoteItems() {
        mAdapter.performDelAction(liteNoteDB);
        initData();
        handleDelActionLayout(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isDelActivated) {
            handleDelActionLayout(false);
            mAdapter.cancelDel();
            return false;
        } else
            return super.onKeyDown(keyCode, event);
    }

    /**
     * 打开发表Note Activity的方法
     */
    public void startPubNoteIntent() {
        Intent i = new Intent(this, PubActivity.class);
        startActivityForResult(i, Config.REQ_ADD);
    }

    /***
     * 进行编辑Note的方法
     *
     * @param noteEntry
     */
    public void startEditNoteIntent(NoteBean noteEntry) {
        Intent i = new Intent(this, PubActivity.class);
        i.putExtra("NoteItem", noteEntry);
        startActivityForResult(i, Config.REQ_EDIT);
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

    /**
     * 接受PubActivity返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.REQ_ADD:
                if (resultCode == RESULT_OK) {
                    boolean flag_add = data.getBooleanExtra("add", false);
                    boolean flag_delete = data.getBooleanExtra("delete", false);
                    if (flag_add || flag_delete) {
                        initData();
                    }
                }
                break;
            case Config.REQ_EDIT:
                if (resultCode == RESULT_OK) {
                    boolean flag_edit = data.getBooleanExtra("edit", false);
                    boolean flag_delete = data.getBooleanExtra("delete", false);
                    if (flag_edit || flag_delete) {
                        initData();
                    }
                }
                break;
            //印象笔记绑定回调
            case EvernoteSession.REQUEST_CODE_LOGIN:
                LogUtil.i(TAG, "印象笔记绑定回调");
                if (resultCode == Activity.RESULT_OK) {
                    // handle success
                    // MenuItem item = (MenuItem) findViewById(R.id.action_bindEverNote);
                    liteNoteSharedPrefs.cacheBooleanPrefs(Config.SP_EVERNOTE_BIND_FLAG, true);
                    MenuItem item = menu.findItem(R.id.action_bindEverNote);
                    item.setTitle("立即同步");
                    // new CreateNoteBookTask(this);
                    new GetUserInfoTask(this, null);
                } else {
                    liteNoteSharedPrefs.cacheBooleanPrefs(Config.SP_EVERNOTE_BIND_FLAG, false);
                }
                break;
        }
    }

    /**
     * view 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.undo_btn:
                handleDelActionLayout(false);
                mAdapter.cancelDel();
                break;
            case R.id.delete_btn:
                //弹出删除确认对话框
                if (mAdapter.getDelCount() == 0) {
                    Toast.makeText(this, "没有选择要删除的笔记！", Toast.LENGTH_SHORT).show();
                    return;
                }
                new ConfirmDialog(this, "确定要删除吗？", new ConfirmDialog.Callback() {
                    @Override
                    public void perform() {

                        delNoteItems();
                    }
                });
                break;
        }
    }


}
