package com.lenovo.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by noahkong on 17-6-5.
 */

public class DateUtil {
    /**
     * 时间戳转字符串
     *
     * @param formatPatten 格式化规则 eg HH:ss
     * @param timestamp    时间戳
     * @return
     */
    public static String timestamp2String(String formatPatten, long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat(formatPatten);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return format.format(calendar.getTime());
    }
}
