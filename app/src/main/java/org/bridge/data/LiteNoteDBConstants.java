package org.bridge.data;

/**
 * 数据库常量字段定义类
 */
public class LiteNoteDBConstants {
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;
    /**
     * 数据库名称
     */
    public static final String DB_NAME = "LiteNote.DB";
    /**
     * Notes表名
     */
    public static final String TABLE_NOTES = "Notes";
    /**
     * 主键
     */
    public static final String NOTE_ID = "note_id";
    /**
     * 内容
     */
    public static final String NOTE_CONTENT = "note_content";
    /**
     * 发表时间（用作显示）
     */
    public static final String NOTE_PUBDATE = "note_pubdate";
    /**
     * 内容修改时间（用作排序）
     */
    public static final String NOTE_MODIFYTIME = "note_modifytime";
    /**
     * 同步状态
     */
    public static final String NOTE_SYNCSTATE = "note_syncstate";
    /**
     * EverNote 笔记中对应的guid标识码
     */
    public static final String NOTE_EVERGUID = "note_everguid";
}
