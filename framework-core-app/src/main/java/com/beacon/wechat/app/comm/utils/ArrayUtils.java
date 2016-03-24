package com.beacon.wechat.app.comm.utils;

import java.util.List;

public class ArrayUtils {
	public static String EMPTY = "";
	public interface Callback<T>{
		String load(T t);
	}
	public static <T> String join(T[] list,String separator,Callback<T> callback){
		if(list==null||list.length<1){
			return EMPTY;
		}
		if (separator == null) {
            separator = EMPTY;
        }
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            String val = callback.load(list[i]);
            if(val!=null){
            	 buf.append(val);
            }
        }
        return buf.toString();
	} 
	@SuppressWarnings("unchecked")
	public static <T> String join(List<T> list,String separator,Callback<T> callback){
		return join((T[])list.toArray(), separator, callback);
	} 
}
