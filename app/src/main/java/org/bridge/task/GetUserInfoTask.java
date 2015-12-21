package org.bridge.task;

import android.content.Context;
import android.os.AsyncTask;

import com.evernote.client.android.EvernoteSession;
import com.evernote.edam.type.User;

import org.bridge.config.Config;
import org.bridge.data.LiteNoteSharedPrefs;

/**
 * 异步获取用户账户信息
 */
public class GetUserInfoTask extends AsyncTask<Void, Void, User> {
    String TAG = "userInfo";
    Context context;
    Callback callback;

    public GetUserInfoTask(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.execute();
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
        if (user != null && callback == null) {
            LiteNoteSharedPrefs liteNoteSharedPrefs = LiteNoteSharedPrefs.getInstance(context);
            liteNoteSharedPrefs.cacheStringPrefs(Config.SP_EVERNOTE_ACCOUNT, user.getUsername());

        }
    }

    public interface Callback {
        public void onCall(User user);

    }
}
