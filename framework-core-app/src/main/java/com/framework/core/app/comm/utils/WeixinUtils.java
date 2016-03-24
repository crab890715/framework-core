package com.framework.core.app.comm.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.core.app.vo.Mobile;

public class WeixinUtils {
	public static String UTF8 = "UTF-8";
	/**
	 * 短信接口地址
	 */
	public static String SMS_URL = "http://222.73.117.158/msg/HttpBatchSendSM";
	/**
	 * 短信接口账号
	 */
	public static String SMS_ACCOUNT = "Vip-biken";
	/**
	 * 短信接口密码
	 */
	public static String SMS_PWD = "Beacon2015";
	/**
	 * 短信接口是否需要状态报告
	 */
	public static String SMS_NEEDSTATUS = "true";
	
	public static Mobile getCityByMobile(String mobile){
		String url = "http://v.showji.com/Locating/showji.com20150416273007.aspx";
		Map<String,String> params = new HashMap<>();
		params.put("output", "json");
		params.put("m", mobile);
		params.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
		String json = HttpUtils.get(url, params, UTF8);
		return AppUtils.fromJson(json,new TypeReference<Mobile>(){});
	}
	/**
	 * 微信签名算法
	 * @param strSrc
	 * @return
	 */
	public static String sha1(String strSrc) {  
        MessageDigest md = null;  
        String strDes = null;  
  
        byte[] bt = strSrc.getBytes();  
        try {  
            md = MessageDigest.getInstance("SHA-1");  
            md.update(bt);  
            strDes = bytes2Hex(md.digest()); //to HexString  
        } catch (NoSuchAlgorithmException e) {  
            System.out.println("Invalid algorithm.");  
            return null;  
        }  
        return strDes;  
    }
	public static String bytes2Hex(byte[] bts) {  
        String des = "";  
        String tmp = null;  
        for (int i = 0; i < bts.length; i++) {  
            tmp = (Integer.toHexString(bts[i] & 0xFF));  
            if (tmp.length() == 1) {  
                des += "0";  
            }  
            des += tmp;  
        }  
        return des;  
    } 
	/**
     * 验证手机号码
     * @param mobile
     * @return
     */
    public static boolean valideMobile(String mobile) {
    	if(StringUtils.isBlank(mobile)) {
    		return false;
    	}
    	String regex = "^1[0-9]{10}$";
    	return mobile.matches(regex);
    }
    
    /**
     * 发送短信接口
     * @param mobile
     * @param content
     * @return
     */
    public static String sendMsg(String mobile,String content) {
		Map<String,String> params = new HashMap<>();
		params.put("account", WeixinUtils.SMS_ACCOUNT);
		params.put("pswd", WeixinUtils.SMS_PWD);
		params.put("msg", content);
		params.put("mobile", mobile);
		params.put("needstatus", WeixinUtils.SMS_NEEDSTATUS);
		return HttpUtils.post(WeixinUtils.SMS_URL, params, UTF8);
    }
    
	public static int random(int min,int max){
		return (int) Math.round(Math.random()*(max-min)+min);
	}
	public static void main(String[] args) {
		/*Mobile mobile = getCityByMobile("13510278755");
		System.err.println(mobile.getCity());*/
		
		//System.out.println(WeixinUtils.sendMsg("13590489155","恭喜您在年会抽奖环节获得了一等奖，请凭223365码在晚会结束后前往后台领取奖品。"));
		
		int[] testMembers = {12184,6631};
		Arrays.sort(testMembers); 
		System.out.println(Arrays.binarySearch(testMembers, 6632));
	}
}
