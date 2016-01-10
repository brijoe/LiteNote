package org.bridge.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.*;
import android.os.Process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 全局未捕获异样处理类
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private Context mContext;
    private static CrashHandler sIntance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private static final String FLIE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/LiteNoteCrash/log/";

    /**
     * 私有无参构造方法
     */
    private CrashHandler() {

    }

    /**
     * 获取CrashHandler实例
     *
     * @return
     */
    public static CrashHandler getIntance() {
        return sIntance;
    }

    /**
     * 初始化方法
     *
     * @param context
     */
    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    /**
     * 获取软件版本及设备信息
     *
     * @param pw
     * @throws PackageManager.NameNotFoundException
     */
    @SuppressWarnings("deprecation")
    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        //软件版本号
        pw.print("App Version:");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        //Android 版本号
        pw.print("OS version:");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        //手机制造商
        pw.print("Vendor:");
        pw.println(Build.MANUFACTURER);
        //手机型号
        pw.print("Model:");
        pw.println(Build.MODEL);
        //CPU架构
        pw.print("CPU ABI:");
        pw.println(Build.CPU_ABI);

    }

    /**
     * 将异常信息写入SD卡
     *
     * @param ex
     * @throws IOException
     */
    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            LogUtil.d(TAG, "sdcard unmounted,skip dump exception");
            return;
        }
        LogUtil.d(TAG, "SD卡路径" + PATH);
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        File file = new File(PATH + FLIE_NAME + time + FILE_NAME_SUFFIX);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 当程序中有未捕获的异常，系统将自动调用此方法
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            //导出异常信息到SD卡
            dumpExceptionToSDCard(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ex.printStackTrace();
        //如果系统提供了默认的异常处理器，则交给系统去结束程序，否则由自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }
    }
}
