package com.framework.core.app.comm.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.core.app.comm.ConfigFactory;
import com.framework.core.app.comm.Constant;

public class AppUtils {
	private final static long VERSION = System.currentTimeMillis();
	private static Logger log = Logger.getLogger(AppUtils.class);
	private static ObjectMapper mapper=new ObjectMapper();
	public static <T> T spring(Class<T> cls){
		return WebApplicationContextUtils
		.getRequiredWebApplicationContext(request().getServletContext())
		.getBean(cls);
//        return ContextLoader.getCurrentWebApplicationContext().getBean(cls);
    }
    public static HttpServletRequest request(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }
    public static HttpServletResponse response(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
    }
    public static String openId(){
    	return getCookie(Constant.COOKIE_OPENID);
    }
    public static String baseurl(){
    	HttpServletRequest request = AppUtils.request();
//    	System.out.println(request.getRemoteAddr());
//    	System.out.println(request.getRemoteHost());
//    	System.out.println(request.getRemotePort());
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
    public static String imageurl(String uri){
        return baseurl()+"/images/"+uri;
    }
    public static String coupon(String uri){
        return ConfigFactory.getString("coupon.base.url")+uri;
    }
    public static String version(){
        return String.valueOf(VERSION);
    }
    public static boolean isAjax(){
    	String obj = request().getHeader("X-Requested-With");
    	if(obj==null){return false;}
    	return "XMLHttpRequest".toUpperCase().equals(obj.toUpperCase());
    }
    /**
     * 重定向
     * @param url
     */
    public static void sendRedirect(String url){
    	try {
			response().sendRedirect(url);
		} catch (IOException e) {
			log.error(e);
		}
    }
    /**
     * 转发
     * @param url
     */
    public static void forward(String url){
    	HttpServletRequest request = request();
    	HttpServletResponse response = response();
		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
    }
    /**
     * 获取当前url
     * @return
     */
    public static String url(){
    	HttpServletRequest request = request();
    	String query = request.getQueryString();
        return request.getRequestURL().toString()+(StringUtils.isBlank(query) ? "" : ("?" + query));
    }
    /**
     * json转对象
     * @param json
     * @param tr
     * @return
     */
    public static <T> T fromJson(String json,TypeReference<T> tr) {
        try {
			return mapper.readValue(json,tr);
		} catch (JsonParseException e) {
			log.error(e);
		} catch (JsonMappingException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
        return null;
    }
    /**
     * 对象转json
     * @param bean
     * @return
     */
    public static String toJson(Object bean){
        try {
			return mapper.writeValueAsString(bean);
		} catch (JsonProcessingException e) {
			log.error(e);
		}
        return null;
    }
    
    /**
     * 设置COOKIE
     */
    public static void setCookie(String name,String value,int maxAge){
        Cookie cookie=new Cookie(name,value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response().addCookie(cookie);
    }
    
    /**
     * 获取COOKIE
     */
    public static String getCookie(String name){
    	Cookie[] cookies=request().getCookies();
    	if(cookies!=null){
            for(Cookie ck : cookies){
                if(name.equalsIgnoreCase(ck.getName())){
                    return ck.getValue();
                }
            }
    	}
        return null;
    }
    
    /**
     * 删除cookie
     * @param name
     */
    public static void deleteCookie(String name){ setCookie(name,"",0);}
    
    /**
     * 概率算法,以Map中的value为权重算概率，权重越大概率越大
     * @return 返回某个value权重对应的key
     */
    public static int getRand(Map<Integer,Integer> proArr,int proSum) {
    	int result = 0;
		Random rand = new Random();
		for(Map.Entry<Integer,Integer> entry:proArr.entrySet()){
	        //System.out.println(entry.getKey()+"--->"+entry.getValue());
	        int randNum = rand.nextInt(proSum);
	        randNum += 1;
	        if(randNum <= entry.getValue()) {
				result = entry.getKey();
				break;
			} else {
				proSum -= entry.getValue();
			}
	        
		} 
		/*for(int i=0;i<proArr.length;i++) {
			int randNum = rand.nextInt(proSum); 
			randNum += 1;
			if(randNum <= proArr[i]) {
				result = i;
				break;
			} else {
				proSum -= proArr[i];
			}
		}*/
		return result;
    }
    
}
