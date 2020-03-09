package com.shi.annie.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2020/3/9 6:57 下午
 */
public class DateUtil {

    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
    public static final String yyyyMMdd = "yyyy-MM-dd";
    public static final String HHmmss = "HH:mm:ss";

    private static final Object lock = new Object();
    private volatile static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();

    /**
     * 线程安全，单例获取SDF
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        //经典双加锁
        if (tl == null) {
            synchronized (lock) {
                tl = sdfMap.get(pattern);
                if (tl == null) { tl = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
                    sdfMap.put(pattern, tl);
                }
            }
        }
        return tl.get();
    }


    public static String formatDate(Date date) {
        return new SimpleDateFormat(yyyyMMddHHmmss).format(date);
    }

    public static String formatDate(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(yyyyMMddHHmmss);
        return formatter.format(date);
    }

    public static String formatDateSafely(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static String formatDate(LocalDateTime date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(date);
    }

    public static Date toDate(String dateStr) {
        try {
            return new SimpleDateFormat(yyyyMMddHHmmss).parse(dateStr);
        } catch (ParseException e) {
            System.out.printf("DateUtil -> 调用日期工具类转换异常，要转换的文本:【%s】,转换格式:【%s】", dateStr, yyyyMMddHHmmss);
            e.printStackTrace();
        }
        return null;
    }

    public static Date toDate(String dateStr, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            System.out.printf("DateUtil -> 调用日期工具类转换异常，要转换的文本:【%s】,转换格式:【%s】", dateStr, pattern);
            e.printStackTrace();
        }
        return null;
    }

    public static Date toDateSafely(String dateStr, String pattern) {
        try {
            return getSdf(pattern).parse(dateStr);
        } catch (ParseException e) {
            System.out.printf("DateUtil -> 调用日期工具类转换异常，要转换的文本:【%s】,转换格式:【%s】", dateStr, pattern);
            e.printStackTrace();
        }
        return null;
    }

    public static Date toDate(Long timeMillis) {
        return new Date(timeMillis);
    }

    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String now() {
        return formatDate(new Date());
    }


}
