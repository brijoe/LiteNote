package org.bridge.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import org.bridge.adapter.ItemAdapter;
import org.bridge.entry.NoteEntry;
import org.bridge.litenote.R;
import org.bridge.util.Logger;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    String TAG = "info";
    private Intent i = new Intent();
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<NoteEntry> items = new ArrayList<>();
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        actionBar = getActionBar();

        initView();
    }

    private void initView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //列数为两列
        int spanCount = 2;
        mLayoutManager = new StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //构建一个临时数据源
        initData();
        mAdapter = new ItemAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);
    }

    //初始化数据
    private void initData() {
        NoteEntry note1 = new NoteEntry(1, "数据1", "一天前");
        NoteEntry note2 = new NoteEntry(2, "今天好开心啊，可以约会啪啪啪了,我有一个女朋友，啦啦啦啦啦啦啦啦啦啦", "一天前");
        NoteEntry note3 = new NoteEntry(3, "数据3", "一天前");
        NoteEntry note4 = new NoteEntry(4, "数据4", "一天前");
        NoteEntry note5 = new NoteEntry(5, "数据5", "一天前");
        NoteEntry note6 = new NoteEntry(1, "数据1", "一天前");
        NoteEntry note7 = new NoteEntry(2, "今天好开心啊，可以约会啪啪啪了,我有一个女朋友，啦啦啦啦啦啦啦啦啦啦", "一天前");
        NoteEntry note8 = new NoteEntry(3, "数据3", "一天前");
        NoteEntry note9 = new NoteEntry(4, "数据4", "一天前");
        NoteEntry note10 = new NoteEntry(5, "数据5", "一天前");
        NoteEntry note11 = new NoteEntry(1, "数据1", "一天前");
        NoteEntry note12 = new NoteEntry(2, "今天好开心啊，可以约会啪啪啪了,我有一个女朋友，啦啦啦啦啦啦啦啦啦啦", "一天前");
        NoteEntry note13 = new NoteEntry(3, "数据3", "一天前");
        NoteEntry note14 = new NoteEntry(4, "数据4", "一天前");
        NoteEntry note15 = new NoteEntry(5, "数据5", "一天前");
        items.add(note1);
        items.add(note2);
        items.add(note3);
        items.add(note4);
        items.add(note5);
        items.add(note6);
        items.add(note7);
        items.add(note8);
        items.add(note9);
        items.add(note10);
        items.add(note11);
        items.add(note12);
        items.add(note13);
        items.add(note14);
        items.add(note15);
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
}
