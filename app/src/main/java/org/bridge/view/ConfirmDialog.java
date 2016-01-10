package org.bridge.view;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;

/**
 * 自定义对话框
 */
public class ConfirmDialog {

    public ConfirmDialog(final Context context, String message, final Callback callback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("提示");
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null) {
                            callback.perform();
                        }
                    }
                });
        dialogBuilder.setNegativeButton("取消", null);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public interface Callback {
        void perform();
    }
}
