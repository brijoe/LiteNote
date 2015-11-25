package org.bridge.view;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.view.WindowManager;

import org.bridge.activity.MainActivity;

/**
 * Created by bridgegeorge on 15/11/25.
 */
public class ConfirmDialog {
    public ConfirmDialog(final Context context, final DelCallback delCallback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("提示");
        dialogBuilder.setMessage("你确定要删除吗？");
        dialogBuilder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (delCallback != null) {
                            delCallback.delNoteItems();
                        }
                    }
                });
        dialogBuilder.setNegativeButton("取消", null);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public interface DelCallback {
        void delNoteItems();
    }
}
