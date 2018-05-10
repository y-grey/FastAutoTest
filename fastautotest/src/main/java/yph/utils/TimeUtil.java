package yph.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by _yph on 2016/2/11 0011.
 */
public class TimeUtil {

    //format : "yyyy-MM-dd HH:mm:ss"
    public static String getTime(String format) {
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return new SimpleDateFormat(format).format(curDate);
    }

    public static String getTime() {
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return new SimpleDateFormat("yyyyMMddHHmmss").format(curDate);
    }

    public static String getTimeAndAddOne(String format, int i) {
        return addOne(new Date(System.currentTimeMillis()), format, i);
    }

    public static String addOne(Date curDate, String format, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(curDate);
        cal.add(i, 1);
        return new SimpleDateFormat(format).format(cal.getTime());
    }

    public static String timeP(long oldtime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(oldtime));
    }

    public static long timeSubtract(String time2) {
        return timeSubtract(getTime("yyyy-MM-dd HH:mm:ss"), time2);
    }

    public static long timeSubtract(String time1, String time2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(time1);
            Date d2 = df.parse(time2);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            return diff/(1000 * 60);
//            System.out.println("" +diff/(1000 * 60));
//            long days = diff / (1000 * 60 * 60 * 24);
//            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
//            System.out.println("" + days + "天" + hours + "小时" + minutes + "分");
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getWeek(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(parseDate(str, "yyyy-MM-dd HH:mm"));
        return week;
    }

    //根据日期取得星期几
    public static String getWeek_(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(parseDate(str, "yyyy-MM-dd"));
        return week;
    }

    public static int daysOfTwo(Date fDate, Date oDate) {

        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(fDate);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(oDate);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;

    }

    /**
     * String转换为时间
     *
     * @param str
     * @return
     */
    public static Date parseDate(String str, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date addTime = null;
        try {
            addTime = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return addTime;
    }

    /**
     * 传入的时间在现在时间之前,注：不能比較只有 時分 的情況
     */
    public static boolean isTimeBeforeNow(String str, String format) {
        return parseDate(str, format).before(new Date(System.currentTimeMillis()));
    }

    /**
     * 传入的时间在现在时间之后,注：不能比較只有 時分 的情況
     */
    public static boolean isTimeAfterNow(String str, String format) {
        return parseDate(str, format).after(new Date(System.currentTimeMillis()));
    }

    /**
     * 将日期转换为字符串
     *
     * @param date
     * @return
     */
    public static String ParseDateToString(Date date) {
        return ParseDateToString(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将日期转换为字符串（重载）
     *
     * @param date
     * @param format:时间格式，必须符合yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String ParseDateToString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(date);
    }

    /**
     * 将UMT时间转换为本地时间
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date ParseUTCDate(String str) {
        //格式化2012-03-04T23:42:00+08:00
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CHINA);
        try {
            Date date = formatter.parse(str);
            return date;
        } catch (ParseException e) {
            //格式化Sat, 17 Mar 2012 11:37:13 +0000
            //Sat, 17 Mar 2012 22:13:41 +0800
            try {
                SimpleDateFormat formatter2 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.CHINA);
                Date date2 = formatter2.parse(str);

                return date2;
            } catch (ParseException ex) {
                return null;
            }
        }
    }

    /**
     * 将时间转换为中文
     *
     * @param datetime
     * @return
     */
    public static String DateToChineseString(Date datetime) {
        Date today = new Date();
        long seconds = (today.getTime() - datetime.getTime()) / 1000;

        long year = seconds / (24 * 60 * 60 * 30 * 12);// 相差年数
        long month = seconds / (24 * 60 * 60 * 30);//相差月数
        long date = seconds / (24 * 60 * 60);     //相差的天数
        long hour = (seconds - date * 24 * 60 * 60) / (60 * 60);//相差的小时数
        long minute = (seconds - date * 24 * 60 * 60 - hour * 60 * 60) / (60);//相差的分钟数
        long second = (seconds - date * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);//相差的秒数

        if (year > 0) {
            return year + "年前";
        }
        if (month > 0) {
            return month + "月前";
        }
        if (date > 0) {
            return date + "天前";
        }
        if (hour > 0) {
            return hour + "小时前";
        }
        if (minute > 0) {
            return minute + "分钟前";
        }
        if (second > 0) {
            return second + "秒前";
        }
        return "未知时间";
    }

    /**
     * 将时间转换为分钟 如02：00 :01转换为120
     */
    public static String timeToMinute(String time) {
        String sArray[] = time.split(":");
        return Integer.valueOf(sArray[0]) * 60 + Integer.valueOf(sArray[1]) + "";
    }

    /**
     * 将时间转换为分钟 如02：00 :01转换为120
     */
    public static String timeTosecond(String time) {
        String sArray[] = time.split(":");
        return Integer.valueOf(sArray[0]) * 360 + Integer.valueOf(sArray[1]) * 60 + Integer.valueOf(sArray[2]) + "";
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(String year, String month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, Integer.valueOf(year));
        a.set(Calendar.MONTH, Integer.valueOf(month) - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
