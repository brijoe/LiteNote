package org.bridge.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;


public class SyncNoteListTask extends AsyncTask<Notebook, Void, Boolean> {
    private Context context;
    private Notebook notebook;

    public SyncNoteListTask(Context context, Notebook notebook) {
        this.context = context;
        this.notebook = notebook;
        this.execute(notebook);
    }

    @Override
    protected Boolean doInBackground(Notebook... params) {
        Note note = new Note();
        note.setNotebookGuid(params[0].getGuid());
        note.setTitle("LiteNote");
        String content = EvernoteUtil.NOTE_PREFIX
                + "这是一条测试"
                + EvernoteUtil.NOTE_SUFFIX;
        note.setContent(content);
        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        try {
            noteStoreClient.createNote(note);
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
    //    @Override
//    protected Void doInBackground(List<NoteBean>... params) {
//        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
//        for (NoteBean noteBean :
//                params[0]) {
//
//
//        }
//        return noteStoreClient.createNote(note);
//        return null;
//    }
}
