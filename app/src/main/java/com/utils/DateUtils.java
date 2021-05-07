package com.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final String PATTERN_1 = "yyyy-MM-dd_HH-mm-ss";
    public static final String PATTERN_2 = "MMddHHmmss";
    public static final String PATTERN_3 = "yyyy.MM.dd";
    public static final String PATTERN_4 = "yyyyMMdd";
    public static final String PATTERN_5 = "yyyy-MM-dd";
    public static final String PATTERN_6 = "yyyy-MM";
    public static final String PATTERN_7 = "HH:mm:ss";
    public static final String PATTERN_8 = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_9 = "yyyy|MM|dd HH:mm:ss";
    public static final String PATTERN_22 = "yyyy年MM月dd日 HH:mm:ss";
    public static final String PATTERN_23 = "yy年MM月dd日";
    public static final String PATTERN_10 = "HH:mm";
    public static final String PATTERN_11 = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_12 = "MM-dd HH:mm";
    public static final String PATTERN_13 = "MM/dd HH:mm";
    public static final String PATTERN_14 = "yy";
    public static final String PATTERN_15 = "HH:mm";
    public static final String PATTERN_16 = "dd";
    public static final String PATTERN_17 = "mm分ss秒";
    public static final String PATTERN_18 = "MM-dd";
    public static final String PATTERN_19 = "yyyy.MM.dd EEEE";
    public static final String PATTERN_20 = "yyyyMM";
    public static final String PATTERN_21 = "yyyy年MM月";
    public static final String PATTERN_24 = "yyyy年MM季";
    public static final String PATTERN_25 = "yyyy年";
    public static final String PATTERN_26 = "yyyy";
    //
    private static final String PATTERN_DEFAULT = PATTERN_8;

    //
    public final long SEC_LONG = 1000;
    public final long MIN_LONG = SEC_LONG * 60;
    public final long HOUR_LONG = MIN_LONG * 60;
    public final long DAY_LONG = HOUR_LONG * 24;

    private Calendar calendar;

    public DateUtils(String dateStr, String pattern) throws Exception {

        pattern = null == pattern ? PATTERN_DEFAULT : pattern;

        getCalendar().setTime(new SimpleDateFormat(pattern).parse(dateStr));

    }

    public DateUtils(Calendar calendar) {
        this.calendar = calendar;
    }

    public DateUtils(Date date) {
        getCalendar().setTime(date);
    }

    public DateUtils(Timestamp timestamp) {
        getCalendar().setTimeInMillis(timestamp.getTime());
    }

    public DateUtils(java.sql.Date sqlDate) {
        getCalendar().setTime(sqlDate);
    }

    public DateUtils(long longDate) {
        getCalendar().setTimeInMillis(longDate);
    }

    public DateUtils(int year, int month, int day, int hour, int minute,
                     int second) {
        getCalendar().set(year, month - 1, day, hour, minute, second);
    }

    public DateUtils() {
        getCalendar();
    }

    public Calendar getCalendar() {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        return calendar;
    }

    public Date getDate() {
        return getCalendar().getTime();
    }

    public Date getTimestamp() {
        return new Timestamp(getCalendar().getTimeInMillis());
    }

    public java.sql.Date getSqlDate() {
        return new java.sql.Date(getCalendar().getTimeInMillis());
    }

    public String getString(String pattern) {

        pattern = null == pattern ? PATTERN_DEFAULT : pattern;

        return new SimpleDateFormat(pattern).format(getCalendar().getTime());

    }

    public long getLong() {
        return getCalendar().getTimeInMillis();
    }

    public D_Values dValue(DateUtils du) {
        return new D_Values(this, du);
    }

    public DateUtils calculate(int year, int month, int day, int hour,
                               int minute, int second) {

        getCalendar();
        calendar.add(Calendar.YEAR, year);
        calendar.add(Calendar.MONTH, month);
        calendar.add(Calendar.DATE, day);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        calendar.add(Calendar.MINUTE, minute);
        calendar.add(Calendar.SECOND, second);
        return this;
    }

    public DateUtils calculateDay(int day) {
        getCalendar().add(Calendar.DATE, day);
        return this;
    }

    public DateUtils calculateMinute(int minute) {
        getCalendar().add(Calendar.MINUTE, minute);
        return this;
    }

    public DateUtils calculateHour(int hour) {
        getCalendar().add(Calendar.HOUR_OF_DAY, hour);
        return this;
    }

    public DateUtils setDateTime(int year, int month, int day, int hour,
                                 int minute, int second) {

        getCalendar();
        if (year >= 0) {
            calendar.set(Calendar.YEAR, year);
        }
        if (month >= 0) {
            calendar.set(Calendar.MONTH, month - 1);
        }
        if (day >= 0) {
            calendar.set(Calendar.DATE, day);
        }
        if (hour >= 0) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute >= 0) {
            calendar.set(Calendar.MINUTE, minute);
        }
        if (second >= 0) {
            calendar.set(Calendar.SECOND, second);
        }
        return this;
    }

    public DateUtils setDate(int year, int month, int day) {

        getCalendar();
        if (year >= 0) {
            calendar.set(Calendar.YEAR, year);
        }
        if (month >= 0) {
            calendar.set(Calendar.MONTH, month - 1);
        }
        if (day >= 0) {
            calendar.set(Calendar.DATE, day);
        }

        return this;
    }

    public DateUtils setTime(int hour, int minute, int second) {

        getCalendar();
        if (hour >= 0) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute >= 0) {
            calendar.set(Calendar.MINUTE, minute);
        }
        if (second >= 0) {
            calendar.set(Calendar.SECOND, second);
        }

        return this;
    }

    public int getDay() {
        return getCalendar().get(Calendar.DATE);
    }

    public int getHour() {
        return getCalendar().get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return getCalendar().get(Calendar.MINUTE);
    }

    public int getSecond() {
        return getCalendar().get(Calendar.SECOND);
    }

    public int getMonth() {
        return getCalendar().get(Calendar.MONTH) + 1;
    }

    public int getYear() {
        return getCalendar().get(Calendar.YEAR);
    }

    public String getYear2() {
        String str = String.valueOf(getCalendar().get(Calendar.YEAR));
        return str.substring(str.length() - 2);
    }

    public class D_Values {

        public DateUtils foo;
        public DateUtils bar;

        public D_Values(DateUtils foo, DateUtils bar) {
            this.foo = foo;
            this.bar = bar;

            msec_all_dv = foo.getLong() - bar.getLong();

            day_dv = msec_all_dv / DAY_LONG;

            long temp = msec_all_dv % DAY_LONG;

            hour_dv = temp / HOUR_LONG;
            temp = temp % HOUR_LONG;
            min_dv = temp / MIN_LONG;
            temp = temp % MIN_LONG;
            sec_dv = temp / SEC_LONG;
            msec_dv = temp % SEC_LONG;

            day_all_dv = day_dv;
            hour_all_dv = hour_dv + day_all_dv * 24;
            min_all_dv = min_dv + hour_all_dv * 60;
            sec_all_dv = sec_dv + min_all_dv * 60;

        }

        // 单独计算的相差 总天数或总描述 互相间没有关系
        public long day_all_dv;// 相差的总 天数
        public long hour_all_dv;// 总小时数
        public long min_all_dv;
        public long sec_all_dv;
        public long msec_all_dv;// 相差的总 毫秒数

        // 一系列的 相差的 日 时 分 秒
        // 一下 相加 等于msec_all_dv
        public long day_dv;// 两个时间相差的天数
        public long hour_dv;// 两个时间相差的小时，抛出天数后剩余的相差的小时数.下同
        public long min_dv;// 分
        public long sec_dv;// 秒
        public long msec_dv;// 毫秒

    }

    public static void main(String[] args) {
        // System.out.println(new DateUtils("11:11:11", PATTERN_7)
        // .getString(PATTERN_1));
        // System.out.println(new DateUtils().getString(PATTERN_1));
        // System.out.println(new DateUtils().getDay());
        // System.out.println(new DateUtils().getMonth());
        // System.out.println("=====");
        // System.out.println(new DateUtils().calculate(0, 1, 0, 0, 0, 0)
        // .getString(PATTERN_1));
        // System.out
        // .println(new DateUtils().calculate(0, 1, 0, 0, 0, 0).getDay());
        // System.out.println(new DateUtils().calculate(0, 1, 0, 0, 0, 0)
        // .getMonth());
        // DateUtils dateUtils = new DateUtils();
        // long a = dateUtils.getLong();
        // long b = dateUtils.calculateDay(-1).getLong();
        // System.out.println(a - b);
        DateUtils dateUtils = new DateUtils(1000 * 90);

        System.out.println(dateUtils.getString(DateUtils.PATTERN_17));
    }

}
