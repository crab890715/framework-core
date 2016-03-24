package com.beacon.wechat.app.comm.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUtils {
	public static void main(String[] args) {
		/*String phone = "13539770000";
        //检查phone是否是合格的手机号(标准:1开头，第二位为3,5,8，后9位为任意数字)
        System.out.println(phone + ":" + phone.matches("^1[0-9]{10}$")); //true   
        
        String regex = "^1[0-9]{10}$";  
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(phone);  
        if(matcher.matches()){  
            System.out.println("这是合法的Email");  
        }else{  
            System.out.println("这是非法的Email");  
        }  */
        
        /*List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        for(int i=0;i<list.size();i++) {
        	if(i == 2) {
        		list.remove(i);
        	}
        	System.out.println(i+"~~"+list.get(i));
        }*/
        
        Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		for(int i=0;i<100;i++) {
			//System.out.println(format.format(date)+RandomUtils.generateNumber());
			//System.out.println(new Random().nextInt(2));
			//System.out.println(String.format("%02d", new Random().nextInt(99)));
			System.out.println(60+3*(60+1989-2021)/12);
		}
	}
}
