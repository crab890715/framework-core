package com.framework.core.app.biz;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.core.app.comm.Result;

@Service("redpacketsBiz")
public class RedpacketsBiz {
	
	@Autowired
	private RedisOperations<String,String> shakeHistoryRedisTemplate;
	@Autowired
	private RedisOperations<String,Object> weixinStringRedisTemplate;
//	@Autowired
//	private RedpackService redpackService;
//	@Autowired
//	private StoreAccountLogsService storeAccountLogsService;
//	@Autowired
//	private StoreAccountService storeAccountService;
	/**
	 * 避免一个红包发多次，红包加入流水号
	 * @param redId
	 * @param number
	 * @return
	 */
	private String getAccessKey(String redId, String number){
		return MessageFormat.format("redpackets_{0}_{1}", redId,number);
	}
	private String getAccessValue(String openid, String redId){
		return MessageFormat.format("{0}_{1}", openid,redId);
	}
	private String getMemberKey(String redId, String number){
		return MessageFormat.format("redpack_member_{0}_{1}", redId,number);
	}
	private String getUnConsumerKey(String redId, String number){
		return MessageFormat.format("redpackets_un_consumer_{0}_{1}", redId,number);
	}
	private String getConsumerKey(String redId, String number){
		return MessageFormat.format("redpackets_consumer_{0}_{1}", redId,number);
	}
	/**
	 * 获取当前红包的状态
	 * @param redId
	 * @return
	 */
	public boolean currRedpacketState(String redId,String number){
		ListOperations<String, Object>  list = weixinStringRedisTemplate.opsForList();
		return list.size(getUnConsumerKey(redId,number))>0;
	}
	/**
	 * 获取发放成功的红包
	 * @param redId
	 * @param openid
	 * @param number 
	 * @return
	 */
	public Object getConsumerRedpacket(String redId,String openid, String number){
		HashOperations<String, Object, Object>  hash = weixinStringRedisTemplate.opsForHash();
		return hash.get(getConsumerKey(redId,number), openid) ;
	}
	/**
	 * 设置发放成功的红包
	 * @param redId
	 * @param openid
	 * @param value
	 */
	public void setConsumerRedpacket(String redId,String openid,Object value, String number){
		HashOperations<String, Object, Object>  hash = weixinStringRedisTemplate.opsForHash();
		hash.put(getConsumerKey(redId,number), openid, value);
	}
	
	public void addAccess(String openid, String redId,String number){
		String key = getAccessKey(redId,number);
		String value = getAccessValue(openid, redId);
		SetOperations<String, String> setOperations =  shakeHistoryRedisTemplate.opsForSet();
		setOperations.add(key, value);
	}
	/**
	 * 摇红包策略
	 * 拦截用户重复刷新页面的请求：
	 * 已经获取到红包结果的返回状态1，
	 * 已经进入缓存队列的返回状态2，
	 * 红包已经摇完了返回状态3，
	 * 没有在队列随机返回进入摇红包页面状态4,
	 * 否则返回没有摇到5
	 * @param openid
	 * @param storeid
	 * @param deviceid 
	 * @param pagestore
	 * @param type
	 * @return
	 */
	public Result<Object> filter(String openid, String redId,String number) {
		SetOperations<String, String> setOperations =  shakeHistoryRedisTemplate.opsForSet();
		String key = getAccessKey(redId,number);
		String value = getAccessValue(openid, redId);
		//已经获取的红包，返回结果页面
		Object object = getConsumerRedpacket(redId, openid,number);
		if(object!=null){
			return new Result<Object>(1, "成功抢到红包", object);
		}
		//如果是成员已经在队列了，则通知用户等待
		if(setOperations.isMember(key, value)){
			return new Result<>(2);
		}
		//红包已经摇完
		if(!currRedpacketState(redId,number)){
			return new Result<>(3);
		}
		//随机到抢红包页面
		if(Math.random()>0.7){
			return new Result<>(4);
		}
		return new Result<>(5);
	}
	@Transactional
	public void chargeback(int money,String openId,String redId){
		int a = 0;
		if(a==0){
			throw new RuntimeException("");
		}
	} 
	/**
	 * 根据redid和openid消费队列,需要事务处理
	 * @param redId
	 * @param openId
	 */
	
	public void consumRedpacket(String redId, String openId,String number) {
		//校验参数
		if(StringUtils.isBlank(number)||StringUtils.isBlank(openId)||StringUtils.isBlank(redId)){
			return;
		}
//		Redpack  redpack = redpackService.selectByPrimaryKey(Integer.valueOf(redId));
		//校验红包金额
//		if(redpack.getResidueAmount()<=0 || (redpack.getTotalNum()>0 && redpack.getResidueNum()<=0)||!number.equals(redpack.getSingleNum())){
//			return;
//		}
		String key = getMemberKey(redId, number); 
		SetOperations<String, String> setOperations =  shakeHistoryRedisTemplate.opsForSet();
		if(shakeHistoryRedisTemplate.hasKey(key)){
			//根据红包信息重新生成红包
			
			//移除信息
			setOperations.remove(getAccessKey(redId,number), getAccessValue(openId, redId));
			return;
		}   
		String money = setOperations.randomMember(key);
		this.chargeback(Integer.valueOf(money), openId, redId);
	}
	
}
