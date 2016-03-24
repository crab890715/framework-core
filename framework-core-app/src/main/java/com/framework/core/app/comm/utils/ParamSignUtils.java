package com.framework.core.app.comm.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

public class ParamSignUtils {
	private static Logger log = Logger.getLogger(ParamSignUtils.class);
	public final static String SECRET="beacon2014";
	public static void main(String[] args)  
    {  
        HashMap<String, String> signMap = new HashMap<String, String>();  
        signMap.put("a","01");  
        signMap.put("c","02");  
        signMap.put("b","03");  
        String secret="test";  
        Map<String, String> SignHashMap=ParamSignUtils.sign(signMap, secret);  
        System.out.println("SignHashMap:"+SignHashMap);  
        List<String> ignoreParamNames=new ArrayList<String>();  
        ignoreParamNames.add("a");  
        Map<String, String> SignHashMap2=ParamSignUtils.sign(signMap,ignoreParamNames, secret);  
        System.out.println("SignHashMap2:"+SignHashMap2);  
    }  
	public static HashMap<String, String> sign(Map<String, String> paramValues) {  
        return sign(paramValues,SECRET);  
    }
    public static HashMap<String, String> sign(Map<String, String> paramValues,  
            String secret) {  
        return sign(paramValues, null, secret);  
    }  
  
    /** 
     * @param paramValues 
     * @param ignoreParamNames 
     * @param secret 
     * @return 
     */  
    public static HashMap<String, String> sign(Map<String, String> paramValues,  
            List<String> ignoreParamNames, String secret) {  
            HashMap<String, String> signMap = new HashMap<String, String>();  
            StringBuilder sb = new StringBuilder();  
            List<String> paramNames = new ArrayList<String>(paramValues.size());  
            paramNames.addAll(paramValues.keySet());  
            if (ignoreParamNames != null && ignoreParamNames.size() > 0) {  
                for (String ignoreParamName : ignoreParamNames) {  
                    paramNames.remove(ignoreParamName);  
                }  
            }  
            Collections.sort(paramNames);  
            sb.append(secret);  
            for (String paramName : paramNames) {  
                sb.append(paramName).append(paramValues.get(paramName));  
            }  
            sb.append(secret);  
//            byte[] md5Digest = md5(sb.toString());  
//            String sign = byte2hex(md5Digest);  
            
            signMap.put("param", sb.toString());  
            signMap.put("sign", DigestUtils.md5Hex(sb.toString()));  
            return signMap;  
    }  
  
}
