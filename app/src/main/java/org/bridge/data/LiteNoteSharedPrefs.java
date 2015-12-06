package org.bridge.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 本地SharedPreferences数据的操作类，单例模式
 */
public class LiteNoteSharedPrefs {
    /**
     * 当前类的私有静态对象
     */
    private static LiteNoteSharedPrefs liteNoteSharedPrefs;
    /**
     * SharedPreferences 对象
     */
    private SharedPreferences prefs;
    /**
     * Editor对象用于数据项写入
     */
    private SharedPreferences.Editor editor;

    /**
     * 私有构造方法
     *
     * @param context
     */
    private LiteNoteSharedPrefs(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    }

    /**
     * 同步方法，获取本类的实例
     *
     * @param context
     * @return LiteNoteSharedPrefs 实例
     */
    public synchronized static LiteNoteSharedPrefs getInstance(Context context) {
        if (liteNoteSharedPrefs == null) {
            liteNoteSharedPrefs = new LiteNoteSharedPrefs(context);
        }
        return liteNoteSharedPrefs;
    }

    /**
     * 存储String类型的键值
     *
     * @param sp_key
     * @param sp_value
     */
    public void cacheStringPrefs(String sp_key, String sp_value) {
        editor.putString(sp_key, sp_value);
        editor.commit();

    }

    /**
     * 存储Boolean 类型的键值
     *
     * @param sp_key
     * @param value
     */
    public void cacheBooleanPrefs(String sp_key, Boolean value) {
        editor.putBoolean(sp_key, value);
        editor.commit();
    }

    /**
     * 获取存储的String类型值
     *
     * @param sp_key
     * @return String值
     */
    public String getCacheStringPrefs(String sp_key) {
        return prefs.getString(sp_key, null);
    }

    /**
     * 获取存储的Boolean 类型值
     *
     * @param sp_key
     * @return Boolean值
     */
    public Boolean getCacheBooleanPrefs(String sp_key, Boolean flag) {
        return prefs.getBoolean(sp_key, flag);
    }
}
