package org.bridge.task;

import android.os.AsyncTask;

import com.evernote.client.android.EvernoteSession;

/**
 * 异步注销登录任务
 */
public class LogOutTask extends AsyncTask<Void, Void, Boolean> {
    private Callback callback;

    public LogOutTask(Callback callback) {
        this.callback = callback;
        this.execute();
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
