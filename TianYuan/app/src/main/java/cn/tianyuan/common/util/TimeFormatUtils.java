package cn.tianyuan.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/21.
 */

public class TimeFormatUtils {
    private static SimpleDateFormat time_ym=new SimpleDateFormat("yyyy.MM");
    private static SimpleDateFormat time_ymd=new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat time_ymd_hh_mm=new SimpleDateFormat("yyyy.MM.dd HH:mm");
    private static SimpleDateFormat time_ymd_hh_mm_ss=new SimpleDateFormat("yyyy.MM.dd HH:mm:SS");
    private static SimpleDateFormat time_ymd_local=new SimpleDateFormat("yyyy年MM月dd日");

    public static String format_yyyy_MM(Date date){
        return time_ym.format(date);
    }

    public static String format_yyyy_MM_dd(Date date){
        return time_ymd.format(date);
    }

    public static String format_yyyy_MM_dd_HH_mm(Date date){
        return time_ymd_hh_mm.format(date);
    }

    public static String format_yyyy_MM_dd_HH_mm_SS(Date date){
        return time_ymd_hh_mm_ss.format(date);
    }

    public static String format_yyyy_MM_dd_week(Date date){
        return time_ymd.format(date)+" "+getWeekday(date);
    }

    public static String format_yyyy_MM_dd_week_local(Date date){
        return time_ymd_local.format(date)+" "+getWeekday(date);
    }


    private static String getWeekday(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
            case Calendar.SUNDAY:
                return "星期天";
        }
        return null;
    }

}
