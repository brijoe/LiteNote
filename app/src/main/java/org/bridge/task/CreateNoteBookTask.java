package org.bridge.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Notebook;

import org.bridge.config.Config;
import org.bridge.data.LiteNoteDB;
import org.bridge.data.LiteNoteSharedPrefs;

import java.util.List;

/**
 * 创建笔记本的异步操作类
 */
public class CreateNoteBookTask extends AsyncTask<Void, Void, Notebook> {
    private Context context;
    private LiteNoteDB liteNoteDB;
    private EvernoteNoteStoreClient noteStoreClient;
    private LiteNoteSharedPrefs liteNoteSharedPrefs;

    public CreateNoteBookTask(Context context) {
        this.context = context;
        this.liteNoteDB = LiteNoteDB.getInstance(context);
        this.liteNoteSharedPrefs = LiteNoteSharedPrefs.getInstance(context);
        this.execute();
    }

    @Override
    protected Notebook doInBackground(Void... params) {
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
            return null;
        }
    }

    @Override
    protected void onPostExecute(Notebook notebook) {
        if (notebook != null) {
            liteNoteSharedPrefs.cacheStringPrefs(Config.SP_EVERNOTE_NOTEBOOK_GUID, notebook.getGuid());
            Toast.makeText(context, "创建成功！", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(context, "创建失败！", Toast.LENGTH_SHORT).show();
    }
}
