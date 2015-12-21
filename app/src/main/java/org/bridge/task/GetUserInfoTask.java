package org.bridge.task;

import android.content.Context;
import android.os.AsyncTask;

import com.evernote.client.android.EvernoteSession;
import com.evernote.edam.type.User;

/**
 * 异步获取用户账户信息
 */
public class GetUserInfoTask extends AsyncTask<Void, Void, User> {
    String TAG = "userInfo";
    Context context;
    Callback callback;

    public GetUserInfoTask(Context context, Callback callback) {
        this.context = context;
        this.execute();
        this.callback = callback;
    }

    @Override
    protected User doInBackground(Void... params) {

        try {
            return EvernoteSession.getInstance().getEvernoteClientFactory().getUserStoreClient().getUser();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(User user) {
        if (callback != null) {
            callback.onCall(user);

        }
    }

    public interface Callback {
        public void onCall(User user);

    }
}
