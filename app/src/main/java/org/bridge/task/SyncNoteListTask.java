package org.bridge.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;

import org.bridge.config.Config;
import org.bridge.data.LiteNoteDB;
import org.bridge.model.NoteBean;

import java.util.List;


public class SyncNoteListTask extends AsyncTask<List<NoteBean>, Void, Boolean> {
    private Context context;
    private LiteNoteDB liteNoteDB;
    private EvernoteNoteStoreClient noteStoreClient;
    public SyncNoteListTask(Context context, LiteNoteDB liteNoteDB, List<NoteBean> notes) {
        this.context = context;
        this.liteNoteDB = liteNoteDB;
        this.execute(notes);

    }

    @Override
    protected Boolean doInBackground(List<NoteBean>... params) {
        this.noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        String LiteNoteBookGuid = null;
        List<NoteBean> noteBeans = params[0];
        try {
            //获取属于liteNote的印象笔记Guid标识码
            List<Notebook> notebooks = noteStoreClient.listNotebooks();
            for (Notebook notebook : notebooks) {
                if (notebook.getName().equals(Config.EVERNOTE_NOTEBOOK)) {
                    LiteNoteBookGuid = notebook.getGuid();
                    break;
                }
            }
            //根据本地笔记状态执行相应的同步操作
            for (int i = 0; i < noteBeans.size(); i++) {
                switch (noteBeans.get(i).getSycState()) {
                    case Config.ST_ADD_NOT_SYNC:
                        //执行同步添加操作
                        createEverNote(noteBeans.get(i), LiteNoteBookGuid);
                        break;
                    case Config.ST_UPDATE_NOT_SYNC:
                        //执行同步更新操作
                        updateEverNote(noteBeans.get(i));
                        break;
                    case Config.ST_DEL_NOT_SYNC:
                        //执行同步删除操作
                        delEverNote(noteBeans.get(i));
                        break;
                    default:
                        break;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result)
            Toast.makeText(context, "测试笔记创建成功！", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "测试笔记创建失败！", Toast.LENGTH_SHORT).show();

    }

    /**
     * 将本地笔记创建到印象笔记中
     *
     * @param noteBean
     * @param noteBookGuid
     * @throws Exception
     */
    private void createEverNote(NoteBean noteBean, String noteBookGuid) throws Exception {
        Note note = new Note();
        note.setNotebookGuid(noteBookGuid);
        note.setTitle(Config.EVERNOTE_NOTEBOOK);
        String content = EvernoteUtil.NOTE_PREFIX
                + noteBean.getContent()
                + EvernoteUtil.NOTE_SUFFIX;
        note.setContent(content);
        note = noteStoreClient.createNote(note);
        //更新本地笔记状态
        noteBean.setEverGuid(note.getGuid());
        noteBean.setSycState(Config.ST_ADD_AND_SYNC);
        liteNoteDB.updateNoteItem(noteBean);
    }

    /**
     * 更新笔记内容
     *
     * @param noteBean
     * @throws Exception
     */
    private void updateEverNote(NoteBean noteBean) throws Exception {
        String noteGuid = noteBean.getEverGuid();
        Note note = new Note();
        note.setGuid(noteGuid);
        String content = EvernoteUtil.NOTE_PREFIX
                + noteBean.getContent()
                + EvernoteUtil.NOTE_SUFFIX;
        note.setContent(content);
        note = noteStoreClient.updateNote(note);
        noteBean.setSycState(Config.ST_UPDATE_AND_SYNC);
        liteNoteDB.updateNoteItem(noteBean);

    }

    /**
     * 删除笔记
     *
     * @param noteBean
     * @throws Exception
     */
    private void delEverNote(NoteBean noteBean) throws Exception {
        String noteGuid = noteBean.getEverGuid();
        noteStoreClient.deleteNote(noteGuid);
        noteBean.setSycState(Config.ST_DEL_AND_SYNC);
        liteNoteDB.updateNoteItem(noteBean);
    }
}
