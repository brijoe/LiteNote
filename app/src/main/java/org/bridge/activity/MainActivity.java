package org.bridge.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.bridge.adapter.NoteItemAdapter;
import org.bridge.db.LiteNoteDB;
import org.bridge.entry.NoteEntry;
import org.bridge.litenote.R;
import org.bridge.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
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
    private List<NoteEntry> items = new ArrayList<>();
    /**
     * 当前Activity 的ActionBar 对象
     */
    private ActionBar actionBar;
    /**
     * 需要初始化的DB对象
     */
    private LiteNoteDB liteNoteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getActionBar();
        init();

    }

    private void initView() {

    }

    private void init() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //列数为两列
        int spanCount = 2;
        mLayoutManager = new StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //创建一个数据库实例
        liteNoteDB = LiteNoteDB.getInstance(this);
        //构建一个数据源
        initData();
        mAdapter = new NoteItemAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
    }

    //初始化数据
    private void initData() {

        items = liteNoteDB.queryAllNoteItem();
        // mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
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

    public void handleDelActionLayout(Boolean activated) {
        if (activated) {
            actionBar.hide();
            this.onResume();
        } else {
            actionBar.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handleDelActionLayout(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 打开发表Note Activity的方法
     */
    public void startPubNoteIntent() {
        Intent i = new Intent(this, PubActivity.class);
        startActivity(i);
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
        super.onActivityResult(requestCode, resultCode, data);
    }
}
