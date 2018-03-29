package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Qi Jianli
 *         <p>
 *         Created on 2017/8/25 0023.
 */
public class DateUtils {

    /**
     * 截取日期获得年
     *
     * @param date
     */
    public static int getYear(Date date) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 截取日期获得月
     *
     * @param date
     */
    public static int getMonth(Date date) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) +1;
    }

    /**
     * 截取日期获得天
     *
     * @param date
     */
    public static int getDay(Date date) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DATE);
    }

    /**
     * 获得月初第一天日期
     * @param
     */
    public static String getFirstDayOfMonth (int year,int month) throws Exception {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //设置日历中月份的第1天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
    /**
     * 获得月末最后一天日期
     * @param
     */
    public static String getEndDayOfMonth (int year,int month) throws Exception {
        Calendar cal= Calendar.getInstance();
        cal.set(year,month,0);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(cal.getTime());
    }


    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime)throws Exception {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(f.parse(datetime));
        // 指示一个星期中的某天
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 时间戳转日期字符串
     *
     * @param datetime
     * @return
     */
    public static String dateTimeToDate(String datetime)throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Long changeTime = new Long(datetime);
        return format.format(changeTime);
    }

}
