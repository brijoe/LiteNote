package org.bridge.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式化时间显示类
 */
public class DateUtil {
    private static final String TAG = "DateUtil";

    /**
     * 获取当前的时间戳
     *
     * @return
     */
    public static String getTimestamp() {
        return String.valueOf(new Date().getTime());
    }

    /**
     * 按照格式获取当前时间
     *
     * @return 格式化后的当前时间
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式
        Date now = new Date();//当前时间
        return sdf.format(now);
    }

    /**
     * 计算当前时间与传入时间的差值，并格式化返回
     *
     * @param time
     * @return 格式化后的时间差
     */
    public static String formatTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式
        Date now = new Date();//当前时间
        String timeDeltaStr = null;//格式化的时间差字符串
        try {
            Date date = sdf.parse(time);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

            if (day > 0) {
                if (day == 1)
                    timeDeltaStr = "昨天";
                else
                    timeDeltaStr = day + "天 前";
                LogUtil.i(TAG, timeDeltaStr);
                return timeDeltaStr;
            } else if (hour > 0) {
                timeDeltaStr = hour + "小时 前";
                LogUtil.i(TAG, timeDeltaStr);
                return timeDeltaStr;
            } else if (min > 0) {
                timeDeltaStr = min + "分钟 前";
                LogUtil.i(TAG, timeDeltaStr);
                return timeDeltaStr;
            } else {
                timeDeltaStr = "刚刚";
                LogUtil.i(TAG, timeDeltaStr);
                return timeDeltaStr;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDeltaStr;
    }
}
