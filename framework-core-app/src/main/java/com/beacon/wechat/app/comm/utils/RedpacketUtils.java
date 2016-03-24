package com.beacon.wechat.app.comm.utils;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RedpacketUtils {
	public static int random(int total,int max,int min,int len){
		Random random = new Random();
		for(int i=0;i<len;i++){
			int safeValue = (total-(len-i)*min)/(len-i);
			safeValue= max > safeValue?safeValue : max;
			int money = 0;
			if(safeValue-min<=0){
				money = min;
			}else{
				money = random.nextInt(safeValue-min)+min;
			}
			total = total-money;
			System.err.println(MessageFormat.format("当前第{0}个红包金额：{1}，剩余金额{2}", i,money,total));
		}
		return 0;
	}
	public static void main(String[] args) {
		//红包数
        int number = 10;
        //红包总额
        float total = 100;
        float money;
        //最小红包
        double min = 1;
        double max;
        int i = 1;
        List<Float> math = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("###.##");
        while (i < number) {
            //保证即使一个红包是最大的了,后面剩下的红包,每个红包也不会小于最小值
            max = total - min * (number - i);
            int k = (int)(number - i) / 2;
            //保证最后两个人拿的红包不超出剩余红包
            if (number - i <= 2) {
                k = number - i;
            }
            //最大的红包限定的平均线上下
            max = max / k;
            //保证每个红包大于最小值,又不会大于最大值
            money = (int) (min * 100 + Math.random() * (max * 100 - min * 100 + 1));
            money = (float)money / 100;
            //保留两位小数
            money = Float.parseFloat(df.format(money));
            total=(int)(total*100 - money*100);
            total = total/100;
            math.add(money);
            System.out.println("第" + i + "个人拿到" + money + "剩下" + total);
            i++;
            //最后一个人拿走剩下的红包
            if (i == number) {
                math.add(total);
                System.out.println("第" + i + "个人拿到" + total + "剩下0");
            }
        }
//取数组中最大的一个值的索引
//        System.out.println("本轮发红包中第" + (math.indexOf(Collections.max(math)) + 1) + "个人手气最佳");
	}
}
