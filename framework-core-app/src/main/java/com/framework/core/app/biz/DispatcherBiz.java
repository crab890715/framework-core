package com.framework.core.app.biz;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.framework.core.app.comm.utils.DateUtils;
import com.framework.core.app.vo.Dispatcher;

@Service("dispatcherBiz")
public class DispatcherBiz {
	

	@Autowired
	private RedisOperations<String,String> shakeHistoryRedisTemplate;
	
	/**
	 * 摇一摇历史
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @return
	 */
	private String getShakeKey(String openid, String storeid,String deviceid){
		return MessageFormat.format("shake_{0}_{1}_{2}", openid,storeid,deviceid);
	}
	
	/**
	 * 普通摇一摇次数
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @return
	 */
	private String getShakeCountKey(String openid, String storeid, String deviceid) {
		return MessageFormat.format("shake_count_{0}_{1}_{2}", openid,storeid,deviceid);
	}
	
	/**
	 * 用户当日在分发商铺的摇到的广告次数
	 * @param sid
	 * @param targetSid
	 * @return
	 */
	public String getAdvertStoreTimeKey(String targetSid,String openid){
		return MessageFormat.format("advert_store_time_{0}_{1}", targetSid, openid);
	}
	
	/**
	 * 用户当日在该设备摇到广告分发次数
	 * @param did
	 * @param openid
	 * @return
	 */
	public String getAdvertDeviceTimeKey(String did,String openid){
		return MessageFormat.format("advert_device_time_{0}_{1}", did,openid);
	}
	/**
	 * 缓存摇一摇模板历史记录
	 * @param sid
	 * @param did
	 * @param openid
	 * @param action
	 * @param type
	 */
	public void cacheHistory(String sid,String did,String openid,String action, int type,String targetSid){
		String key = this.getShakeKey(openid, sid,did);
		String value = Dispatcher.historyTemp(type, action);
		ListOperations<String, String> listOperations =  shakeHistoryRedisTemplate.opsForList();
		ValueOperations<String, String> valueOperations = shakeHistoryRedisTemplate.opsForValue();
		List<String> list = listOperations.range(key, 0, listOperations.size(key));
		if(!list.contains(value)){
			listOperations.rightPush(key, value);
		}
		long time = DateUtils.distanceNextDay(TimeUnit.MINUTES);
		shakeHistoryRedisTemplate.expire(key,time , TimeUnit.MINUTES);
		//普通摇一摇缓存摇到的次数
		if(type==1){
			String timeKey = getShakeCountKey(openid, sid, did);
			valueOperations.increment(timeKey, 1l);
			shakeHistoryRedisTemplate.expire(timeKey,time , TimeUnit.MINUTES);
		}
		
		//广告投放,用户在此设备的摇一摇次数和用户在广告分发商铺摇一摇的次数
		if(type == 3) {
			String dtimekey = getAdvertDeviceTimeKey(did, openid);
			String stimekey = getAdvertStoreTimeKey(targetSid,openid);
			valueOperations.increment(stimekey, 1);
			valueOperations.increment(dtimekey, 1);
			shakeHistoryRedisTemplate.expire(dtimekey,time , TimeUnit.MINUTES);
			shakeHistoryRedisTemplate.expire(stimekey,time , TimeUnit.MINUTES);
		}
		
	}
}
