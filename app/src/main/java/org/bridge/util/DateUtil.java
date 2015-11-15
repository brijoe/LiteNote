package org.bridge.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 此类用于格式化时间显示
 */
public class DateUtil {
    private static final String TAG = "DateUtil";

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
                Logger.i(TAG, timeDeltaStr);
                return timeDeltaStr;
            } else if (hour > 0) {
                timeDeltaStr = hour + "小时 前";
                Logger.i(TAG, timeDeltaStr);
                return timeDeltaStr;
            } else if (min > 0) {
                timeDeltaStr = min + "分钟 前";
                Logger.i(TAG, timeDeltaStr);
                return timeDeltaStr;
            } else {
                timeDeltaStr = s + "秒 前";
                Logger.i(TAG, timeDeltaStr);
                return timeDeltaStr;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDeltaStr;
    }
}
