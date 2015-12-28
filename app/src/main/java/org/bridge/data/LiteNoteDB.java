package org.bridge.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.bridge.config.Config;
import org.bridge.model.NoteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地数据库常用操作类
 */
public class LiteNoteDB {
    private String TAG = "DB";
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
        LiteNoteOpenHelper dbHelper = new LiteNoteOpenHelper(context, LiteNoteDBConstants.DB_NAME, null, LiteNoteDBConstants.VERSION);
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
     * 从数据库中查询所有状态的记录
     *
     * @return
     */
    public List<NoteBean> queryAllStateNoteItem() {
        List<NoteBean> list = new ArrayList<>();
        Cursor cursor = db.query(LiteNoteDBConstants.TABLE_NOTES, null, null, null, null, null, LiteNoteDBConstants.NOTE_ID + " desc");
        while (cursor.moveToNext()) {
            NoteBean noteBean = new NoteBean();
            noteBean.setId(cursor.getInt(cursor.getColumnIndex(LiteNoteDBConstants.NOTE_ID)));
            noteBean.setContent(cursor.getString(cursor.getColumnIndex(LiteNoteDBConstants.NOTE_CONTENT)));
            noteBean.setPubDate(cursor.getString(cursor.getColumnIndex(LiteNoteDBConstants.NOTE_PUBDATE)));
            noteBean.setSyncState(cursor.getInt(cursor.getColumnIndex(LiteNoteDBConstants.NOTE_SYNCSTATE)));
            noteBean.setEverGuid(cursor.getString(cursor.getColumnIndex(LiteNoteDBConstants.NOTE_EVERGUID)));
            list.add(noteBean);
        }
        cursor.close();
        return list;
    }

    /**
     * 从数据库中查询可以显示的所有的Note记录
     *
     * @return
     */
    public List<NoteBean> queryShowStateNoteItem() {
        List<NoteBean> list = new ArrayList<>();
        Cursor cursor = db.query(LiteNoteDBConstants.TABLE_NOTES, null, LiteNoteDBConstants.NOTE_SYNCSTATE + "<=?", new String[]{"3"}, null, null, LiteNoteDBConstants.NOTE_MODIFYTIME + " desc");
        while (cursor.moveToNext()) {
            NoteBean noteBean = new NoteBean();
            noteBean.setId(cursor.getInt(cursor.getColumnIndex(LiteNoteDBConstants.NOTE_ID)));
            noteBean.setContent(cursor.getString(cursor.getColumnIndex(LiteNoteDBConstants.NOTE_CONTENT)));
            noteBean.setPubDate(cursor.getString(cursor.getColumnIndex(LiteNoteDBConstants.NOTE_PUBDATE)));
            noteBean.setModifyTime(cursor.getString(cursor.getColumnIndex(LiteNoteDBConstants.NOTE_MODIFYTIME)));
            noteBean.setSyncState(cursor.getInt(cursor.getColumnIndex(LiteNoteDBConstants.NOTE_SYNCSTATE)));
            noteBean.setEverGuid(cursor.getString(cursor.getColumnIndex(LiteNoteDBConstants.NOTE_EVERGUID)));
            list.add(noteBean);
        }
        cursor.close();
        return list;

    }

    /**
     * 将NoteBean 实例存储到数据库中
     *
     * @param noteBean
     */
    public void saveNoteItem(NoteBean noteBean) {
        if (noteBean != null) {
            ContentValues values = new ContentValues();
            values.put(LiteNoteDBConstants.NOTE_CONTENT, noteBean.getContent());
            values.put(LiteNoteDBConstants.NOTE_PUBDATE, noteBean.getPubDate());
            values.put(LiteNoteDBConstants.NOTE_MODIFYTIME, noteBean.getModifyTime());
            values.put(LiteNoteDBConstants.NOTE_SYNCSTATE, noteBean.getSyncState());
            values.put(LiteNoteDBConstants.NOTE_EVERGUID, noteBean.getEverGuid());
            db.insert(LiteNoteDBConstants.TABLE_NOTES, null, values);
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
            values.put(LiteNoteDBConstants.NOTE_CONTENT, noteBean.getContent());
            values.put(LiteNoteDBConstants.NOTE_MODIFYTIME, noteBean.getModifyTime());
            values.put(LiteNoteDBConstants.NOTE_SYNCSTATE, noteBean.getSyncState());
            values.put(LiteNoteDBConstants.NOTE_EVERGUID, noteBean.getEverGuid());
            db.update(LiteNoteDBConstants.TABLE_NOTES, values, LiteNoteDBConstants.NOTE_ID + "=?", new String[]{String.valueOf(noteBean.getId())});
        }

    }


    /**
     * 设置NoteBean 为删除状态，仅改变状态，并不删除数据
     *
     * @param noteIds
     */
    public void setNotesDelState(int[] noteIds) {
        for (int i = 0; i < noteIds.length; i++) {
            ContentValues values = new ContentValues();
            values.put(LiteNoteDBConstants.NOTE_SYNCSTATE, Config.ST_DEL_NOT_SYNC);
            db.update(LiteNoteDBConstants.TABLE_NOTES, values, LiteNoteDBConstants.NOTE_ID + "=?", new String[]{String.valueOf(noteIds[i])});
        }
    }

    /**
     * 将NoteBean实例从数据库中删除，完全删除
     *
     * @param noteIds
     */
    public void deleteNoteItem(int[] noteIds) {

        for (int i = 0; i < noteIds.length; i++) {

            db.delete("Notes", "note_id=?", new String[]{String.valueOf(noteIds[i])});
        }
    }


}
