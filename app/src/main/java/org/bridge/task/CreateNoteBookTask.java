package org.bridge.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Notebook;


public class CreateNoteBookTask extends AsyncTask<String, Void, Notebook> {
    private Context context;

    public CreateNoteBookTask(Context context) {
        this.context = context;
        this.execute("LiteNote");
    }

    @Override
    protected Notebook doInBackground(String... params) {
        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        Notebook notebook = new Notebook();
        notebook.setName(params[0]);
        try {
            return noteStoreClient.createNotebook(notebook);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Notebook notebook) {
        if (notebook != null) {
            Toast.makeText(context, "创建成功！", Toast.LENGTH_SHORT).show();
            new SyncNoteListTask(context, notebook);
        } else
            Toast.makeText(context, "创建失败！", Toast.LENGTH_SHORT).show();

    }
}
