package org.bridge.task;

import android.content.Context;
import android.os.AsyncTask;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;

import org.bridge.LiteNoteApp;
import org.bridge.config.Config;
import org.bridge.data.LiteNoteDB;
import org.bridge.model.NoteBean;
import org.bridge.util.LogUtil;

import java.util.List;

/**
 * 同步笔记至印象笔记服务器的异步任务类
 */

public class SyncNoteListTask extends AsyncTask<List<NoteBean>, Void, Boolean> {
    private final String TAG = "syncNote";
    private LiteNoteDB liteNoteDB;
    private EvernoteNoteStoreClient noteStoreClient;
    private SyncCallBack syncCallBack;

    /**
     * 无参构造
     */
    public SyncNoteListTask() {
        if (!checkEverUserOnline())
            return;
        this.liteNoteDB = LiteNoteDB.getInstance(LiteNoteApp.getAppContext());
        this.syncCallBack = null;
        this.execute(liteNoteDB.queryAllStateNoteItem());
    }

    /**
     * 有参构造
     *
     * @param context
     * @param syncCallBack
     */
    public SyncNoteListTask(Context context, SyncCallBack syncCallBack) {
        if (!checkEverUserOnline())
            return;
        this.liteNoteDB = LiteNoteDB.getInstance(context);
        this.syncCallBack = syncCallBack;
        this.execute(liteNoteDB.queryAllStateNoteItem());

    }

    @Override
    protected void onPreExecute() {
        LogUtil.d(TAG, "onPreExecute 执行");
        if (syncCallBack != null)
            syncCallBack.onPreSync();
    }

    @Override
    protected Boolean doInBackground(List<NoteBean>... params) {
        LogUtil.d(TAG, "doInBackground 执行");
        this.noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        List<NoteBean> noteBeans = params[0];
        try {
            Notebook notebook = createNoteBook();
            if (notebook == null)
                return false;
            else
                //根据本地笔记状态执行相应的同步操作
                for (int i = 0; i < noteBeans.size(); i++) {
                    NoteBean noteBean = noteBeans.get(i);
                    switch (noteBean.getSyncState()) {
                        case Config.ST_ADD_NOT_SYNC:
                            LogUtil.d(TAG, noteBean.getId() + "->执行同步添加操作");
                            //执行同步添加操作
                            createEverNote(noteBean, notebook.getGuid());
                            break;
                        case Config.ST_UPDATE_NOT_SYNC:
                            LogUtil.d(TAG, noteBean.getId() + "->执行同步更新执行操作");
                            //执行同步更新操作
                            updateEverNote(noteBean);
                            break;
                        case Config.ST_DEL_NOT_SYNC:
                            LogUtil.d(TAG, noteBean.getId() + "->执行同步删除操作");
                            //执行同步删除操作
                            delEverNote(noteBean);
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
        LogUtil.d(TAG, "onPostExecute 执行");
        if (syncCallBack != null)
            syncCallBack.onPostSync(result);

    }

    private Notebook createNoteBook() {
        noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        try {
            //检查是否已经创建笔记本
            List<Notebook> notebooks = noteStoreClient.listNotebooks();
            for (Notebook notebook : notebooks) {
                if (notebook.getName().equals(Config.EVERNOTE_NOTEBOOK))
                    return notebook;
            }
            //未创建则重新创建笔记本
            Notebook notebook = new Notebook();
            notebook.setName(Config.EVERNOTE_NOTEBOOK);
            return noteStoreClient.createNotebook(notebook);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(TAG, e.getMessage());
            return null;
        }

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
        noteBean.setSyncState(Config.ST_ADD_AND_SYNC);
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
        Note note = noteStoreClient.getNote(noteGuid, false, false, false, false);
        String content = EvernoteUtil.NOTE_PREFIX
                + noteBean.getContent()
                + EvernoteUtil.NOTE_SUFFIX;
        note.setContent(content);
        note = noteStoreClient.updateNote(note);
        noteBean.setSyncState(Config.ST_UPDATE_AND_SYNC);
        liteNoteDB.updateNoteItem(noteBean);

    }

    /**
     * 删除笔记
     *
     * @param noteBean
     * @throws Exception
     */
    private void delEverNote(final NoteBean noteBean) throws Exception {
        String noteGuid = noteBean.getEverGuid();
        noteStoreClient.deleteNoteAsync(noteGuid, new EvernoteCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                liteNoteDB.deleteNoteItem(new int[]{noteBean.getId()});
            }

            @Override
            public void onException(Exception exception) {

            }
        });
    }

    /**
     * 判断当前用户是否与印象笔记服务器保持连接
     *
     * @return
     */
    private boolean checkEverUserOnline() {
        return EvernoteSession.getInstance().isLoggedIn();
    }

    public interface SyncCallBack {
        void onPreSync();

        void onPostSync(Boolean result);
    }
}
