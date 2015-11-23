package org.bridge.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.bridge.entry.NoteEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地数据库常用操作类
 */
public class LiteNoteDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "lite_note";
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;
    private static LiteNoteDB liteNoteDB;
    private SQLiteDatabase db;

    /**
     * 构造方法私有化
     *
     * @param context
     */
    private LiteNoteDB(Context context) {
        LiteNoteOpenHelper dbHelper = new LiteNoteOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取LiteNote实例
     *
     * @param context
     * @return LiteNote
     */
    public synchronized static LiteNoteDB getInstance(Context context) {
        if (liteNoteDB == null) {
            liteNoteDB = new LiteNoteDB(context);
        }
        return liteNoteDB;
    }

    /**
     * 从数据库中查询所有的Note记录
     *
     * @return
     */
    public List<NoteEntry> queryAllNoteItem() {
        List<NoteEntry> list = new ArrayList<NoteEntry>();
        Cursor cursor = db.query("Notes", null, null, null, null, null, "id desc");
        while (cursor.moveToNext()) {
            NoteEntry noteEntry = new NoteEntry();
            noteEntry.setId(cursor.getInt(cursor.getColumnIndex("id")));
            noteEntry.setContent(cursor.getString(cursor.getColumnIndex("note_content")));
            noteEntry.setPubDate(cursor.getString(cursor.getColumnIndex("note_pubdate")));
            list.add(noteEntry);
        }
        return list;

    }

    /**
     * 将NoteEntry 实例存储到数据库中
     *
     * @param noteEntry
     */
    public void saveNoteItem(NoteEntry noteEntry) {
        if (noteEntry != null) {
            ContentValues values = new ContentValues();
            values.put("note_content", noteEntry.getContent());
            values.put("note_pubdate", noteEntry.getPubDate());
            db.insert("Notes", null, values);
        }
    }

    /**
     * 更新NoteEntry 实例的内容
     *
     * @param noteEntry
     */
    public void updateNoteItem(NoteEntry noteEntry) {
        if (noteEntry != null) {
            ContentValues values = new ContentValues();
            values.put("note_content", noteEntry.getContent());
            db.update("Notes", values, "id=?", new String[]{String.valueOf(noteEntry.getId())});
        }

    }

    /**
     * 将NoteEntry实例从数据库中删除
     *
     * @param noteIds
     */
    public void deleteNoteItem(int[] noteIds) {
        for (int i = 0; i < noteIds.length; i++) {
            db.delete("Notes", "id=?", new String[]{String.valueOf(noteIds[i])});
        }
    }


}
