package com.beacon.wechat.app.comm;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("1");
		System.err.println(list.contains(String.valueOf(1)));
		
		
		
    	/*int proSum = 1000;
    	int prizeCount = (int)Double.parseDouble("1")*10;//中奖率
		int shakeNone = proSum - prizeCount;
		Map<Integer,Integer> proArr = new HashMap<Integer,Integer>();
		proArr.put(0, prizeCount);
		proArr.put(1, shakeNone);
		int data = getRand(proArr,proSum);
    	System.out.println(data);*/
	}

}
