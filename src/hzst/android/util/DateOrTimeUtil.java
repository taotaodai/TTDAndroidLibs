package hzst.android.util;

import hzst.android.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间格式转换
 *
 * @author Administrator
 */
public class DateOrTimeUtil {
    public enum DateMode {
        //年月日时分
        DATE_MODE_1("yyyy/MM/dd HH:mm", "[0-9]{4}\\/[0-9]{1,2}\\/[0-9]{1,2}\\s[0-9]{1,2}\\:[0-9]{1,2}"),
        DATE_MODE_2("yyyy-MM-dd HH:mm", "[0-9]{4}\\-[0-9]{1,2}\\-[0-9]{1,2}\\s[0-9]{1,2}\\:[0-9]{1,2}"),

        DATE_MODE_YMDHMS_2("yyyy-MM-dd HH:mm:ss","[0-9]{4}\\-[0-9]{1,2}\\-[0-9]{1,2}\\s[0-9]{1,2}\\:[0-9]{1,2}\\:[0-9]{1,2}"),
        //年月日
        DATE_MODE_YMD_1("yyyy/MM/dd", "[0-9]{4}\\/[0-9]{1,2}\\/[0-9]{1,2}"),
        DATE_MODE_YMD_2("yyyy-MM-dd", "[0-9]{4}\\-[0-9]{1,2}\\-[0-9]{1,2}"),
        //年月日 星期
        DATE_MODE_TMDE_1("yyyy/MM/dd EEEE", ""),
        //上午/下午 十分
        DATE_MODE_AHM_1("a HH:mm", "");

        public String format;//日期格式
        public String regExp;//用于匹配该格式日期的正则表达式

        private DateMode(String format, String regExp) {
            this.format = format;
            this.regExp = regExp;
        }
    }

    //年月日时分秒
    public static final String DATE_MODE_1 = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_MODE_2 = "yyyy-MM-dd HH:mm:ss";
    //年月日时分
    public static final String DATE_MODE_YMDHM_1 = "yyyy/MM/dd HH:mm";
    public static final String DATE_MODE_YMDHM_2 = "yyyy-MM-dd HH:mm";
    //年月日
    public static final String DATE_MODE_YMD_1 = "yyyy/MM/dd";
    public static final String DATE_MODE_YMD_2 = "yyyy-MM-dd";
    //月日
    public static final String DATE_MODE_MD_1 = "MM/dd";
    public static final String DATE_MODE_MD_2 = "MM-dd";
    //月日时分
    public static final String DATE_MODE_MDHM_1 = "MM/dd HH:mm";
    public static final String DATE_MODE_MDHM_2 = "MM-dd HH:mm";
    //时分
    public static final String DATE_MODE_HM = "HH:mm";
    //年月日时分秒毫秒，用于文件命名
    public static final String DATE_MODE_DETAIL = "yyyyMMddHHmmssSS";

    public static final long MS_SECEND = 1000;
    public static final long MS_MINUTE = MS_SECEND * 60;
    public static final long MS_HOUR = MS_MINUTE * 60;
    public static final long MS_DAY = MS_HOUR * 24;

    @Deprecated
    public static String getCurrentDate(String mode) {
        SimpleDateFormat sdf = new SimpleDateFormat(mode);
        return sdf.format(new Date());
    }

    public static String getCurrentDate(DateMode mode) {
        SimpleDateFormat sdf = new SimpleDateFormat(mode.format);
        return sdf.format(new Date());
    }

    /**
     * 比较与(当天+时间差)的时间是否是同一天
     *
     * @param mode 需要比较的时间的格式
     * @param date 需要比较的时间
     * @param td   时间差，单位-天(默认为当天)
     * @return
     */
    @Deprecated
    public static boolean isSameDay(String mode, String date, int td) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_MODE_YMD_2);
        String target = sdf.format(new Date(System.currentTimeMillis() + td * MS_DAY));
        String other = convertDateMode(mode, DATE_MODE_YMD_2, date);
        if (target.equals(other)) {
            return true;
        }
        return false;
    }

    public static boolean isSameDay(String date1, String date2) {
        SimpleDateFormat sdf1 = new SimpleDateFormat(parseToMode(date1).format);
        SimpleDateFormat sdf2 = new SimpleDateFormat(parseToMode(date2).format);

        try {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(new Date(sdf1.parse(date1).getTime()));
            c2.setTime(new Date(sdf2.parse(date2).getTime()));
            if (c1.get(Calendar.DAY_OF_MONTH) != c2.get(Calendar.DAY_OF_MONTH)) {
                return false;
            }
            if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)) {
                return false;
            }
            if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {
                return false;
            }
        } catch (ParseException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
            return false;
        }
        return true;
    }

    /**
     * 获取两日期间隔时间(天数)
     *
     * @param mode
     * @param start
     * @param end
     * @return
     */
    @Deprecated
    public static int getDateTDWithDay(String mode, String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat(mode);
        int days = 0;
        try {
            long l = sdf.parse(end).getTime() - sdf.parse(start).getTime();
            days = Math.round(l / MS_DAY);
        } catch (ParseException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
            return days;
        }
        return days;
    }

    /**
     * 获取两日期间隔时间(天数)
     *
     * @param start
     * @param end
     * @return
     */
    public static int getDateTDWithDay(String start, String end) {
        SimpleDateFormat sdfStart = new SimpleDateFormat(parseToMode(start).format);
        SimpleDateFormat sdfEnd = new SimpleDateFormat(parseToMode(end).format);
        int days = 0;
        try {
            long l = sdfEnd.parse(end).getTime() - sdfStart.parse(start).getTime();
            days = Math.round(l / MS_DAY);
        } catch (ParseException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
            return days;
        }
        return days;
    }

    public static int getDateTDWithHour(String start, String end) {
        SimpleDateFormat sdfStart = new SimpleDateFormat(parseToMode(start).format);
        SimpleDateFormat sdfEnd = new SimpleDateFormat(parseToMode(end).format);
        int hours = 0;
        try {
            long l = sdfEnd.parse(end).getTime() - sdfStart.parse(start).getTime();
            hours = Math.round(l / MS_HOUR);
        } catch (ParseException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
            return hours;
        }
        return hours;
    }

    /*
     * 将时间转换为时间戳
     */
    public static Long dateToStampSecond(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(parseToMode(s).format);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime()/1000;
        return ts;
    }

    /**
     *
     * @param stamp 时间戳秒
     * @param mode
     * @return
     */
    public static String stampSecondToDate(long stamp,String mode){
        return new SimpleDateFormat(mode).format(stamp * 1000);
    }

    /**
     * 转换毫秒为时分秒
     *
     * @return
     */
    public static String formatTime(long ms) {
        StringBuffer sb = new StringBuffer(Constants.EMPTY_STRING);
        long hour = ms / MS_HOUR;
        long minute = (ms / MS_MINUTE) % 60;
        long secend = (ms / MS_SECEND) % 60;

        if (ms >= MS_HOUR) {
            sb.append(hour + ":");
        } else {
            if (minute < 10) {
                sb.append("0" + minute + ":");
            } else {
                sb.append(minute + ":");
            }
            if (secend < 10) {
                sb.append("0" + secend);
            } else {
                sb.append(secend);
            }
        }
        return sb.toString();
    }

    /**
     * @param modeCurr   当前格式
     * @param modeTarget 要转换成的格式
     * @param date
     * @return
     */
    @Deprecated
    public static String convertDateMode(String modeCurr, String modeTarget, String date) {
        String dateNew = "";
        SimpleDateFormat sdfOld = new SimpleDateFormat(modeCurr);
        SimpleDateFormat sdfNew = new SimpleDateFormat(modeTarget);
        try {
            dateNew = sdfNew.format(sdfOld.parse(date));
        } catch (Exception e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        return dateNew;
    }

    /**
     * 日期格式转换
     *
     * @param modeTarget
     * @param date
     * @return
     */
    public static String convertDateMode(DateMode modeTarget, String date) {
        String dateNew = "";
        SimpleDateFormat sdfOld = new SimpleDateFormat(parseToMode(date).format);
        SimpleDateFormat sdfNew = new SimpleDateFormat(modeTarget.format);
        try {
            dateNew = sdfNew.format(sdfOld.parse(date));
        } catch (ParseException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        return dateNew;
    }

    /**
     * 解析日期类型
     *
     * @param date
     * @return
     */
    public static DateMode parseToMode(String date) {
        DateMode[] modes = DateMode.values();
        for (int i = 0; i < modes.length; i++) {
            Pattern pattern = Pattern.compile(modes[i].regExp);
            Matcher m = pattern.matcher(date);
            if (m.matches()) {
                return modes[i];
            }
        }
        return null;
    }
}
