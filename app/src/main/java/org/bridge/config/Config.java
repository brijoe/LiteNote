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
    // SharedPreference键定义
    public static final String SP_START_PUB_ACT = "startPubAct";
    public static final String SP_EVERNOTE_BIND_FLAG = "everNoteBindFlag";
    public static final String SP_EVERNOTE_ACCOUNT = "everNoteAccount";
    //印象笔记认证key,和服务配置
    public static final String CONSUMER_KEY = "bridgegeorge-7821";
    public static final String CONSUMER_SECRET = "fc7a7c762f719987";
    public static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

    public static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;
}
