package com.hqmy.market.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.reactivex.disposables.Disposable;

public class DateUtil {
    private static int oneDayMs = 86400000;// 一天的毫秒值

    /**
     * 今天0点毫秒值
     *
     * @return
     */
    public static Long getTodayStart() {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date.getTime();
    }

    /**
     * 获取当天的零点时间
     *
     * @return
     */
    public static long getTodayZero() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC+8"));
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        System.out.println("today zero : " + cal.getTimeInMillis());
        return cal.getTimeInMillis();
    }

    /**
     * 今天24点毫秒值
     */
    private static Long getTodayEnd() {
        Date date = new Date();
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date.getTime();
    }

    /**
     * 返回当前时间 格式yyyy-MM+dd HH:mm:ss
     *
     * @return
     */
    public static String getPortTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(System.currentTimeMillis());

        return date;
    }

    /**
     * 返回当前时间 格式yyyyMMddHHmmssS
     *
     * @return
     */
    public static String setPortTime(long times) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH小时mm分");
        String date = format.format(times);

        return date;
    }

    /**
     * 返回当前时间 格式MM/dd
     *
     * @return
     */
    public static String getPortTime2(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        return format.format(time);
    }

    /**
     * 返回当前时间 格式MM/dd HH:mm
     *
     * @return
     */
    public static String getPortTime3(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm");
        return format.format(time);
    }

    /**
     * 返回当前时间 格式yyyy/MM/dd HH:mm
     *
     * @return
     */
    public static String getPortTime4(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return format.format(time);
    }

    /**
     * 返回当前时间 格式yyyy/MM/dd
     *
     * @return
     */
    public static String getPortTime5(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(time);
    }

    /**
     * 返回当前时间 格式yyyy/MM/dd HH:mm:ss
     *
     * @return
     */
    public static String getPortTime6(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(time);
    }

    /**
     * 返回当前时间 格式yyyy年MM月
     *
     * @return
     */
    public static String getPortTimeHeader(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        return format.format(time);
    }

    /**
     * 返回 HH:mm
     *
     * @param time
     * @return
     */
    public static String getDefaultDate(long time) {
        //		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");//yyyy-MM-dd
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(time);
    }

    /**
     * 返回 HH:mm
     *
     * @param time
     * @return
     */
    public static String getDefaultDateStr(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH小时mm分");
        return format.format(time);
    }

    /**
     * 返回 yyyy-MM-dd
     *
     * @param time
     */
    public static String getYearMonthDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(time);
    }

    /**
     * 返回年月日
     *
     * @param time
     */
    public static String getYearMonthDate1(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(time);
    }

    /**
     * 返回月日时分
     *
     * @param time
     */
    public static String getYearMonthDate2(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(time);
    }

    /**
     * 返回星期数
     * <p>
     * int hour=c.get(Calendar.DAY_OF_WEEK);
     * hour中存的就是星期几了，其范围 1~7
     * 1=星期日 7=星期六，其他类推
     *
     * @param time 时间
     * @return 返回星期
     */
    public static String getWeek(long time) {
        Calendar c = Calendar.getInstance();
        c.setTime(parse(time));

        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 获取评论时间格式
     */
    public static String getCommentTime(long time) {

        String date;
        long todayStartTime = getTodayStart();// 获取今日0时毫秒值
        long todayendTime = getTodayEnd();// 24时毫秒值
        if (time >= todayStartTime && time <= todayendTime) { // 今天
            date = "今天";
        } else if (time >= todayStartTime - oneDayMs
                && time <= todayendTime - oneDayMs) { // 昨天
            date = "昨天";
        } else { // 前天
            date = "前天";
        }

        return date;
    }

    /**
     * 要转换的毫秒数
     *
     * @return 把毫秒数转换成 3:39:23的时间格式 如果不满一小时则不包含小时，满一小时则包含
     */
    public static String formatDuring(long mss) {
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60); // 时

        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC")); // 从00开始算

        if (hours > 0) { // 大于一小时
            return hours + ":" + format.format(mss);

        } else { // 不足一小时
            return format.format(mss);
        }
    }

    /**
     * 要转换的毫秒数
     *
     * @return 把毫秒数转换成 3:39:23的时间格式 返回结果始终包含时分秒三部分
     */
    public static String formatDuringContainHour(long mss) {
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60); // 时

        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC")); // 从00开始算

        if (hours > 9) {
            return hours + ":" + format.format(mss);
        } else {
            return "0" + hours + ":" + format.format(mss);
        }

    }

    /**
     * 获取倒计时
     *
     * @return
     */
    public static String getCountDown(long countDown, Disposable mDisposable) {

        if ((countDown - System.currentTimeMillis()) < 0) {
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            return "直播即将开始";
        }
        long secondTime = (countDown - System.currentTimeMillis()) / 1000;
        long days = secondTime / 24 / 60 / 60;
        long hours = secondTime / 60 / 60 % 60;
        long minute = secondTime / 60 % 60;
        long seconds = secondTime % 60;

        return "距离直播还有 " + days + "天" + hours + "时" + minute + "分" + seconds + "秒";
    }

    /**
     * 获取列表倒计时
     *
     * @return
     */
    public static String getCountDownAdapter(long countDown, Disposable mDisposable) {

        if ((countDown - System.currentTimeMillis()) < 0) {
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            return "即将直播";
        }

        long secondTime = (countDown - System.currentTimeMillis()) / 1000;
        long days = secondTime / 24 / 60 / 60;
        long hours = secondTime / 60 / 60 % 60;
        long minute = secondTime / 60 % 60;
        long seconds = secondTime % 60;

        return "距离直播还有 " + days + "天" + hours + "时" + minute + "分" + seconds + "秒";
    }

    /**
     * @param date
     * @return
     */
    public static String getTime(Date date) {//可根据需要自行截取数据显示
        LogUtil.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 把一段时间转化 成秒数
     *
     * @param time 时间长度，必须是类似 03:54:20 的格式的时分秒
     * @return 秒数
     */
    public static Date timeToSecond(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date second = null;
        Calendar calendar = Calendar.getInstance();
        try {
            //			calendar.set(Calendar.YEAR,2015);
            second = sdf.parse(time);
            calendar.setTime(second);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    public static String setMinData(int times) {
        if (times > 60) {
            if (times % 60 == 0) {
                return times / 60 + "小时";
            } else {
                return times / 60 + "小时" + times % 60 + "分钟";
            }
        } else {
            return times + "分钟";
        }
    }

    public static String setMinDataOnly(int times) {
        if (times >= 60) {
            if (times % 60 == 0) {
                return times / 60 + "小时";
            } else {
                return times + "分钟";
            }
        } else {
            return times + "分钟";
        }
    }


    //出生日期字符串转化成Date对象
    public static Date strToData(String times) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(times);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    //出生日期字符串转化成Date对象
    public static Date parse(long times) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(format.format(times));
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtil.i("DataTime", "---" + e.toString());
        }
        return date;
    }

    /**
     * 计算时间差(天数)
     * 不满一天算一整天
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 天数
     */
    public static int calculateDay(Date startTime, Date endTime) {
        return (int) Math.ceil((endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60 * 24.0));
    }

    /**
     * 获取当前粗糙时间(精确到天)
     *
     * @return
     */
    public static Date getCoarseDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 日定位，根据传入的日期参数定位日期的年、月、日，可获得指定日期的前后N天
     *
     * @param date   Date 指定的日期
     * @param amount 定位值，前传负数，后传正数
     * @return Date 定位后的日期
     */
    public static Date dayOrientation(Date date, int amount) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DATE, amount);
        return ca.getTime();
    }


    /**
     * 根据生日，计算年龄
     *
     * @param date     生日
     * @param onlyYear true则精确到年，否则精确到月
     * @return 年龄，最多精确到月，不足一年返回月数，不足一月返回天数
     */
    public static String calculateAge(Date date, boolean onlyYear) {
        Date untilDate = new Date();
        int months = monthsElapses(date, untilDate);
        int years = months / 12;
        String age = "";
        if (years > 0) {
            age = years + "岁";
            if (onlyYear) {
                return age;
            }
        }
        if (months > 0) {
            int restMonths = months % 12;
            if (restMonths > 0) {
                return age + restMonths + "个月";
            }
            return age;
        }
        return calculateDay(date, untilDate) + "天";
    }

    /**
     * 根据生日，计算年龄
     *
     * @param date 生日
     * @return 年龄，精确到月，不足一月返回天数
     */
    public static String calculateAge(Date date) {
        return calculateAge(date, false);
    }

    /**
     * 计算给定两个日期之间相差的月份数。
     * 比如：2017-01-01与2017-01-31之间相差0月；2017-01-01与2017-02-01之间相差1月
     *
     * @param startDate 起始日期
     * @param untilDate 结束日期
     * @return 给定两个日期之间相差的月份数
     */
    public static int monthsElapses(Date startDate, Date untilDate) throws IllegalArgumentException {
        startDate = getCoarseDate(startDate);
        if (startDate.after(untilDate)) throw new IllegalArgumentException("结束日期必须大于起始日期");
        Calendar start = Calendar.getInstance();
        Calendar until = Calendar.getInstance();
        start.setTime(startDate);
        until.setTime(untilDate);
        int months = (until.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12 + until.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int untilDay = until.get(Calendar.DAY_OF_MONTH);
        // 结束日期天数早于起始日期且不是月的最后一天
        if (untilDay < start.get(Calendar.DAY_OF_MONTH) && untilDay < until.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            months--;
        }
        return months;
    }

    /**
     * 计算给定两个日期之间相差的年数。
     * 比如：2016-02-01与2017-01-31之间相差0年；2016-02-01与2017-02-01之间相差1年
     *
     * @param startDate 起始日期
     * @param untilDate 结束日期
     * @return 给定两个日期之间相差的年数
     */
    public static int yearsElapses(Date startDate, Date untilDate) throws IllegalArgumentException {
        startDate = getCoarseDate(startDate);
        if (startDate.after(untilDate)) {
            return 0;
            //            throw new IllegalArgumentException("结束日期必须大于起始日期");
        }
        Calendar start = Calendar.getInstance();
        Calendar until = Calendar.getInstance();
        start.setTime(startDate);
        until.setTime(untilDate);
        int years = until.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        int startMonth = start.get(Calendar.MONTH);
        int months = until.get(Calendar.MONTH) - startMonth;
        if (months > 0) {
            return years;
        }
        int startDay = start.get(Calendar.DAY_OF_MONTH);
        int days = until.get(Calendar.DAY_OF_MONTH) - startDay;
        // 同月份：
        // 1，结束日期天数不早于起始日期
        // 2，结束日期天数早于起始日期，起始日期正好是2-29（闰年中多出的那一天）且结束日期正好是2月28
        if (months == 0 && (days >= 0 || startMonth == 1 && startDay == 29 && days == -1)) {
            return years;
        }
        return years - 1;
    }


    /**
     * 根据毫秒时间戳来格式化字符串
     * 今天显示今天、昨天显示昨天、前天显示前天.
     * 早于前天的显示具体年-月-日，如2017-06-12；
     *
     * @param timeStamp 毫秒值
     * @return 今天 昨天 前天 或者 yyyy-MM-dd HH:mm:ss类型字符串
     */
    public static String dataFormat(long timeStamp) {
        long curTimeMillis = System.currentTimeMillis();
        Date curDate = new Date(curTimeMillis);
        int todayHoursSeconds = curDate.getHours() * 60 * 60;
        int todayMinutesSeconds = curDate.getMinutes() * 60;
        int todaySeconds = curDate.getSeconds();
        int todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000;
        long todayStartMillis = curTimeMillis - todayMillis;
        if (timeStamp >= todayStartMillis) {
            return "今天 " + getDefaultDate(timeStamp);
        }
        int oneDayMillis = 24 * 60 * 60 * 1000;
        long yesterdayStartMilis = todayStartMillis - oneDayMillis;
        if (timeStamp >= yesterdayStartMilis) {
            return "昨天 " + getDefaultDate(timeStamp);
        }
        //        long yesterdayBeforeStartMilis = yesterdayStartMilis - oneDayMillis;
        //        if(timeStamp >= yesterdayBeforeStartMilis) {
        //            return "前天";
        //        }

        int years = yearsElapses(parse(timeStamp), parse(curTimeMillis));


        if (years >= 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(new Date(timeStamp));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            return sdf.format(new Date(timeStamp));
        }
    }

    public static String getTimeDifference(long firstDateMilliSeconds,long secondDateMilliSeconds) {
        long firstMinusSecond = firstDateMilliSeconds - secondDateMilliSeconds;
        long milliSeconds = firstMinusSecond;
        int totalSeconds = (int) (milliSeconds / 1000);
        // 得到总天数
        int days = totalSeconds / (3600 * 24);
        int days_remains = totalSeconds % (3600 * 24);
        // 得到总小时数
        int hours = days_remains / 3600;
        int remains_hours = days_remains % 3600;
        // 得到分种数
        int minutes = remains_hours / 60;
        // 得到总秒数
        int seconds = remains_hours % 60;
        if (days > 0) {
            return days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (hours > 0) {
            return hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (minutes > 0) {
            return minutes + "分" + seconds + "秒";
        } else {
            return seconds + "秒";
        }
    }

}
