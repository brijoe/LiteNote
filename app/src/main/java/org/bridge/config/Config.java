package org.bridge.config;

import com.evernote.client.android.EvernoteSession;

/**
 * 保存应用程序基本常量和其它配置选项
 */
public class Config {
    //请求码定义
    public static final int REQ_ADD = 1;
    public static final int REQ_EDIT = 2;
    public static final int REQ_BIND = 3;
    //笔记状态码定义
    //本地已添加，但未同步
    public static final int ST_ADD_NOT_SYNC = 0;
    //本地已添加，且同步添加成功
    public static final int ST_ADD_AND_SYNC = 1;
    //本地更新，但未同步
    public static final int ST_UPDATE_NOT_SYNC = 2;
    //本地更新，且同步更新成功
    public static final int ST_UPDATE_AND_SYNC = 3;
    //本地删除，但未同步
    public static final int ST_DEL_NOT_SYNC = 4;
    // SharedPreference键定义
    public static final String SP_START_PUB_ACT = "startPubAct";
    public static final String SP_EVERNOTE_ACCOUNT = "everNoteAccount";
    public static final String SP_EVERNOTE_BIND_FLAG = "everNoteBindFlag";
    public static final String SP_EVERNOTE_NOTEBOOK_GUID = "noteBookGuid";
    //印象笔记认证key,和服务配置
    public static final String CONSUMER_KEY = "bridgegeorge-7821";
    public static final String CONSUMER_SECRET = "fc7a7c762f719987";

    public static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    public static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;
    public static final String EVERNOTE_NOTEBOOK = "LiteNote";
}
