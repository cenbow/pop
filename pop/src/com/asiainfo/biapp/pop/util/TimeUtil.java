package com.asiainfo.biapp.pop.util;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class TimeUtil {
	
    public static String getStringTime(String time) {
        String timeValue = time;
        String reValue = null;
        SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHH");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(timeValue.substring(0, 19));
            reValue = d.format(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reValue;
    }

    public static Date StringToDate(String time) {
        Date reValue = null;
        SimpleDateFormat d = new SimpleDateFormat("yyyyMMddhhmmss");
        try {
            reValue = d.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reValue;
    }

    public String timestampToString(Timestamp time) {
        String reValue = null;
        SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHH");
        try {
            reValue = d.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reValue;
    }

    public static Timestamp getTimestamp(String time) {
        String timeValue = time;
        Timestamp reValue = new Timestamp(System.currentTimeMillis());
        try {
            reValue = Timestamp.valueOf(timeValue.substring(0, 19));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reValue;
    }

    public static String nowTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String nowStingDate() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public static String nowStingTime() {
        return new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
    }

    public static String nowStringMillennium() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Date dt = null;
        try {
            dt = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.YEAR, -1000);//���ڼ�1��
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    public static String lastMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String str = new SimpleDateFormat("yyyyMM").format(new Date());
        Date dt = null;
        try {
            dt = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH, -1);//���ڼ�3����
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    public static String lastDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String str = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Date dt = null;
        try {
            dt = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.DAY_OF_YEAR, -1);//���ڼ�10��
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    public static String nextDate(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String str = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Date dt = null;
        try {
            dt = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.DAY_OF_YEAR, day);//���ڼ�10��
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    public static String nextDate(Date dateTime, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = new SimpleDateFormat("yyyy-MM-dd").format(dateTime);
        Date dt = null;
        try {
            dt = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.DAY_OF_YEAR, day);//���ڼ�10��
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    public static Date nowDate() {
        return new Date();
    }

    public static Timestamp getNowTime() {
        return new Timestamp(new Date().getTime());
    }
    
    /**
	 * 根据格式获取字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateString(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = "";
		try {
			dateString = formatter.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateString;
	}
}
