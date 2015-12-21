package org.bridge.task;

import android.content.Context;
import android.os.AsyncTask;

import com.evernote.client.android.EvernoteSession;

/**
 * 异步注销登录任务
 */
public class LogOutTask extends AsyncTask<Void, Void, Boolean> {
    Context context;
    private Callback callback;

    public LogOutTask(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return EvernoteSession.getInstance().logOut();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (callback != null)
            callback.onCall(result);

    }

    public interface Callback {
        public void onCall(boolean result);
    }
}
