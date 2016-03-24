package com.framework.core.app.comm.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

public class DateUtils {
	public static long DAY=24*60*60*1000;
    public static long HOUR=60*60*1000;
    public static long MINUTE=60*1000;
    public static long SECOND=1000;
    public static String YYYYMMDD="yyyy-MM-dd";
    public static String YYYYMMDDHHMM="yyyy-MM-dd HH:mm";
    public static String ymd(Long date){
        return parse2String(date,YYYYMMDD);
    }
    public static String ymdhm(Long date){
        return parse2String(date,YYYYMMDDHHMM);
    }
    public static String parse2String(Long date,String formatter){
        return new DateTime(date==null?time():date).toString(formatter);
    }
    public static long date(){
    	return date(time());
    }
    /**
     * 同 getStartTime
     * @param time
     * @return
     */
    public static long phpdate(){
    	return date()/SECOND;
    }
    public static long date(long time){
    	return DateTime.parse(new DateTime(time).toString(YYYYMMDD)).getMillis();
    }
    /**
     * 同 getStartTime
     * @param time
     * @return
     */
    public static long phpdate(long time){
    	return date(time)/SECOND;
    }
	/**
	 * 获取当天或者指定时间戳所在的零点的时间戳
	 * @param time
	 * @return
	 */
	public static Long getStartTime(Long phptime) {
		Calendar calendar = Calendar.getInstance();
		if(phptime > 0) {
			Date date = new Date(phptime*SECOND);
			calendar.setTime(date);
		}
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime()/SECOND;
	}
	/**
	 * 获取当天或者指定时间戳所在的零点的时间戳
	 * @param time
	 * @return
	 */
	public static Long getStartTime() {
		return getStartTime(phptime());
	}
	
	public static Long getTimeOnToday(Long phptime) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
		Date date = new Date(phptime*SECOND);
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH,month);
		calendar.set(Calendar.DAY_OF_MONTH,day_of_month);
		return calendar.getTime().getTime()/SECOND;
	}
	
	public static long phptime() {
		return time()/SECOND;
	}
	public static long time() {
		return System.currentTimeMillis();
	}
	public static long distanceNextDay(TimeUnit unit){
		long time = DAY-(time()-date());
		return unit.convert(time, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 判断时间
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean judgeShakeTime(Long startTime,Long endTime) {
		long now = time();
		int today = Integer.valueOf(parse2String(now, "yyyyMMdd"));
		int now_time = Integer.valueOf(parse2String(now, "HHmm"));
		int start_day = Integer.valueOf(parse2String(startTime*SECOND, "yyyyMMdd"));
		int start_time = Integer.valueOf(parse2String(startTime*SECOND, "HHmm"));
		int end_day = Integer.valueOf(parse2String(endTime*SECOND, "yyyyMMdd"));
		int end_time = Integer.valueOf(parse2String(endTime*SECOND, "HHmm"));
		end_time=end_time==0?2400:end_time;
		if(today > end_day || today < start_day){
			return false;
		}
		if(now_time > end_time || now_time < start_time){
			return false;
		}
		return true;
	}
	
	
	public static void main(String[] args) {
		long startTime = 1438358400l;
		long endTime = 1483113600l;
		boolean flag = DateUtils.judgeShakeTime(startTime, endTime);
		System.out.println(flag);
		System.out.println(distanceNextDay(TimeUnit.MINUTES));
	}
}
