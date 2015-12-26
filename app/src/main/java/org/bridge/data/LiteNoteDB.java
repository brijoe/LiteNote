package org.bridge.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.bridge.model.NoteBean;

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
    /**
     * 数据库操作对象
     */
    private static LiteNoteDB liteNoteDB;
    /**
     * SQLiteDatabase 对象，用于数据库CRUD操作
     */
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
     * 从数据库中查询可以显示所有的Note记录
     *
     * @return
     */
    public List<NoteBean> queryAllNoteItem() {
        List<NoteBean> list = new ArrayList<NoteBean>();
        Cursor cursor = db.rawQuery("select * from Notes where note_syncstate=? or note_syncstate=? order by id desc", new String[]{"0", "1"});
        while (cursor.moveToNext()) {
            NoteBean noteBean = new NoteBean();
            noteBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            noteBean.setContent(cursor.getString(cursor.getColumnIndex("note_content")));
            noteBean.setPubDate(cursor.getString(cursor.getColumnIndex("note_pubdate")));
            list.add(noteBean);
        }
        cursor.close();
        return list;

    }

    /**
     * 将NoteEntry 实例存储到数据库中
     *
     * @param noteBean
     */
    public void saveNoteItem(NoteBean noteBean) {
        if (noteBean != null) {
            ContentValues values = new ContentValues();
            values.put("note_content", noteBean.getContent());
            values.put("note_pubdate", noteBean.getPubDate());
            db.insert("Notes", null, values);
        }
    }

    /**
     * 更新NoteBean 实例的内容
     *
     * @param noteBean
     */
    public void updateNoteItem(NoteBean noteBean) {
        if (noteBean != null) {
            ContentValues values = new ContentValues();
            values.put("note_content", noteBean.getContent());
            values.put("note_syncstate", noteBean.getSycState());
            db.update("Notes", values, "id=?", new String[]{String.valueOf(noteBean.getId())});
        }

    }

    /**
     * 将NoteBean实例从数据库中删除
     *
     * @param noteIds
     */
    public void deleteNoteItem(int[] noteIds) {
        for (int i = 0; i < noteIds.length; i++) {
            db.delete("Notes", "id=?", new String[]{String.valueOf(noteIds[i])});
        }
    }


}
