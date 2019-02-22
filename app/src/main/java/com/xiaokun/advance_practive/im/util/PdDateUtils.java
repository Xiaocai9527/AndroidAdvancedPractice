package com.xiaokun.advance_practive.im.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/22
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class PdDateUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String getTime(long time) {
        return format(time, DEFAULT_DATE_FORMAT);
    }

    /**
     * 格式化时间,自定义标签
     *
     * @param time    时间
     * @param pattern 格式化时间用的标签 yyyy-MM-dd hh:MM:ss  HH为24小时
     * @return
     */
    public static String format(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(time));
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Date getCurDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getCurYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getCurMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前是几号
     *
     * @return
     */
    public static int getCurDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

}
