package org.bridge;

import android.app.Application;

import com.evernote.client.android.EvernoteSession;

import org.bridge.config.Config;
import org.bridge.util.CrashHandler;


public class LiteNoteApp extends Application {
    /**
     * 自定义Application
     */

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化印象笔记session配置
        new EvernoteSession.Builder(this)
                .setEvernoteService(Config.EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(Config.SUPPORT_APP_LINKED_NOTEBOOKS)
                .setForceAuthenticationInThirdPartyApp(true)
//                .setLocale(Locale.SIMPLIFIED_CHINESE)
                .build(Config.CONSUMER_KEY, Config.CONSUMER_SECRET)
                .asSingleton();
        //初始化全部异常捕获器
        CrashHandler crashHandler=CrashHandler.getIntance();
        crashHandler.init(this);

    }
}

