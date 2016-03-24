package com.framework.core.app.biz;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.framework.core.api.model.WechatTemp;
import com.framework.core.api.service.WechatTempService;
import com.framework.core.app.comm.cache.GuavaCache;
import com.framework.core.app.comm.utils.AppUtils;
import com.framework.core.app.comm.utils.BeanUtils;
import com.framework.core.app.comm.utils.ParamSignUtils;
import com.framework.core.app.comm.utils.WeixinUtils;
import com.framework.core.app.exception.DatabaseExecuteFailedException;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

@Service("weixinBiz")
@Lazy(false)
public class WeixinBiz {
	@Autowired
	protected WxMpConfigStorage wxMpConfigStorage;
	@Autowired
	protected WxMpService wxMpService;
	@Autowired
	protected WxMpMessageRouter wxMpMessageRouter;
	@Autowired
	private GuavaCache weixinCache;
	@Autowired
	private WechatTempService wechatTempService;
	/*@Autowired
	private MemberService memberService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private AreaService areaService;*/
	@Autowired
	private RedisOperations<String,Object> weixinStringRedisTemplate;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	//expiresIn
	private final static long SIGN_EXPIRESIN = 1000*60*10;
	
	/**
	 * 微信请求过滤
	 * @param openid
	 * @param uri
	 * @return
	 */
	public boolean filter(String openid,String uri){
		return true;
	}
	
	/**
	 * 用户授权url
	 * @param url
	 * @return
	 */
	public String userInfoAuth(String url){
		return wxMpService.oauth2buildAuthorizationUrl(url,WxConsts.OAUTH2_SCOPE_USER_INFO, null);
	}
	
	/**
	 * 静默授权url
	 * @param url
	 * @return
	 */
	public String baseAuth(String url){
		return wxMpService.oauth2buildAuthorizationUrl(url,WxConsts.OAUTH2_SCOPE_USER_INFO, null);
	}
	
	/**
	 * 根据code获取token
	 * @param code
	 * @return
	 */
	public WxMpOAuth2AccessToken oauth2getAccessToken(String code){
		try {
			return wxMpService.oauth2getAccessToken(code);
		} catch (WxErrorException e) {
			log.error("==根据code获取token出错==");
			log.error(e);
		}
		return null;
	}
	
	/**
	 * 根据token获取微信用户信息
	 * @param wxMpOAuth2AccessToken
	 * @return
	 */
	public WxMpUser oauth2getUserInfo(WxMpOAuth2AccessToken wxMpOAuth2AccessToken){
		try {
			return wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
		} catch (WxErrorException e) {
			log.error("==根据token获取微信用户信息出错==");
			log.error(e);
		}
		return null;
	}
	
//	/**
//	 * 根据ID查找设备
//	 * @param deviceId
//	 * @return
//	 */
//	public Device findByDeviceid(Integer deviceId) {
//		return deviceService.findByDeviceid(deviceId);
//	}
//	
//	public Member findByOpenid(String openid) {
//		return memberService.findByOpenid(openid);
//	}
	
	/**
	 * 保存或更新member用户信息
	 * @param user
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @throws Exception 
	 *//*
	public Member bindMember(String storeid, String openid, String mobile) throws Exception {
		//验证手机号码
		if(!WeixinUtils.valideMobile(mobile)) {
			log.info("==方法：" + Thread.currentThread().getStackTrace()[1].getMethodName() + "调用出错。错误原因：手机号码:"+mobile+" 格式不正确");
			throw new UserExecuteFailedException(Result.CODE_FAIL,"手机号码格式不正确");
		}
		
		WechatTemp temp = getWechatTemp(openid);
		//wechat_temp为空，需重新授权
		if(temp == null) {
			log.info("==方法：" + Thread.currentThread().getStackTrace()[1].getMethodName() + "调用出错。错误原因：根据openid:"+openid+" 未找到对应wechatTemp信息");
			throw new UserExecuteFailedException(Result.CODE_FAIL,"请重新授权");
		}
		
		//判断此手机号码是否已经注册过且openID是不是相同
		Member member = memberService.findByMobile(mobile);
		if(member != null) {
			//该手机号码已绑定其他微信帐号
			if(!StringUtils.isBlank(member.getOpenId()) && !openid.equals(member.getOpenId())) {
				log.info("==方法：" + Thread.currentThread().getStackTrace()[1].getMethodName() 
							+ "调用出错。错误原因：手机号码:"+mobile+" 已与微信："+member.getOpenId()+" 绑定，无法再与微信："+openid+" 绑定");
				throw new UserExecuteFailedException(Result.CODE_FAIL,"该手机号码已绑定其他微信号,请更换其他手机号试试");
			}
			
			//更新原member信息
			member.setNickname(temp.getNickname());
			member.setAvatar(temp.getHeadimgurl());
			saveOrUpdateMember(member,"update");
		} else {
			//注册新用户信息
			member = new Member();
			member.setMobile(mobile);
			member.setMemberName(mobile);
			member.setBirthday(2);//默认80后
			member.setLoginNum(1);
			member.setIsSendmsg(1);//默认有发送短信的权限
			member.setOpenId(temp.getOpenId());
			member.setNickname(temp.getNickname());
			member.setGender(temp.getSex() == 1 ? 1 : 2);
			member.setAvatar(temp.getHeadimgurl());
			member.setRegisterTime((int)DateUtils.phptime());
			member.setLoginTime((int)DateUtils.phptime());
			member.setUsercity(getCityIdByMobile(mobile));
			member = saveOrUpdateMember(member,"save");
		}
		
		String key = "member_" + temp.getOpenId();
		ValueOperations<String, Object> redisCache = weixinStringRedisTemplate.opsForValue();
		redisCache.set(key, member, 10, TimeUnit.MINUTES);
		
		//门店通知
		
		return member;
	}*/
	
//	public Member getMember(String openid) {
//		Member member = null;
//		String key = "member_" + openid;
//		ValueOperations<String, Object> redisCache = weixinStringRedisTemplate.opsForValue();
//		Object obj = redisCache.get(key);
//		//缓存中不存在授权数据则从数据库中查询
//		if(obj == null) {
//			member = memberService.findByOpenid(openid);
//			//数据库中不存在用户，则用户授权注册新用户或者绑定旧账户
//			if(member != null) {
//				redisCache.set(key, member, 10, TimeUnit.MINUTES);
//			}
//		} else {
//			member = (Member)obj;
//		}
//		return member;
//	}
	
	
//	/**
//	 * 更新或新增用户信息
//	 * @throws DatabaseExecuteFailedException 
//	 */
//	public Member saveOrUpdateMember(Member member,String type) throws DatabaseExecuteFailedException {
//		try {
//			if("update".equals(type)) {
//				memberService.updateByMemberId(member);
//			} else {
//				member = memberService.save(member);
//			}
//			return member;
//		} catch(Exception e) {
//			log.error("==更新或新增用户信息时操作数据库失败==");
//			log.error(e);
//			throw new DatabaseExecuteFailedException("系统繁忙，稍后重试");
//		}
//	}
	
	public void saveTemp(WxMpUser user) throws Exception {
		WechatTemp temp = wechatTempService.findByOpenId(user.getOpenId());
		if(temp==null) {
			try {
				temp = BeanUtils.convertBean(user, WechatTemp.class);
				temp.setSex(user.getSexId());
				temp.setHeadimgurl(user.getHeadImgUrl());
				temp.setTime((int)(System.currentTimeMillis()/1000));
				temp = wechatTempService.insert(temp);
			} catch(Exception e) {
				log.error("==保存wechattemp信息时数据库操纵失败==");
				log.error(e);
				throw new DatabaseExecuteFailedException("系统繁忙，稍后重试");
			}
		}
		String key = "weixin_" + temp.getOpenId();
		ValueOperations<String, Object> redisCache = weixinStringRedisTemplate.opsForValue();
		redisCache.set(key, temp, 10, TimeUnit.MINUTES);
	}
	
	public WechatTemp getWechatTemp(String openid){
		WechatTemp wechatTemp = null;
		String key = "weixin_" + openid;
		ValueOperations<String, Object> redisCache = weixinStringRedisTemplate.opsForValue();
		Object obj = redisCache.get(key);
		//缓存中不存在授权数据则从数据库中查询
		if(obj==null){
			wechatTemp = wechatTempService.findByOpenId(openid);
			//数据库中不存在用户，则用户授权注册新用户或者绑定旧账户
			if(wechatTemp!=null){
				redisCache.set(key, wechatTemp, 10, TimeUnit.MINUTES);
			}
		}else{
			wechatTemp = (WechatTemp)obj;
		}
		return wechatTemp;
	}
	public Map<String,String> jssdk(){
		Map<String,String> map = new HashMap<String, String>();
		try {
			String jssdk = wxMpService.getJsapiTicket();
			String noncestr = DigestUtils.md2Hex(String.valueOf(System.currentTimeMillis()));
	        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
	        String[] list = new String[]{
	        		"jsapi_ticket="+jssdk,
	        		"noncestr="+noncestr,
	        		"timestamp="+timestamp,
	        		"url="+AppUtils.url()
	        		};
			map.put("ticket", jssdk);
			map.put("signature", WeixinUtils.sha1(StringUtils.join(list,"&")));
	        map.put("noncestr", noncestr);
	        map.put("timestamp", timestamp);
	        map.put("appid", wxMpConfigStorage.getAppId());
		} catch (WxErrorException e) {
			log.error(e);
		}
		return map;
	}
	public boolean checkSign(Map<String, String> signMap,String sign){
		if(StringUtils.isBlank(sign)){
			return false;
		}
		if(Math.abs(Long.valueOf(signMap.get("timetemp"))-System.currentTimeMillis())>SIGN_EXPIRESIN){
			return false;
		}
		return sign.toUpperCase().equals(md5sign(signMap));
	}
	
	public String md5sign(Map<String, String> signMap){
		/*Map<String, String> signMap = new HashMap<String, String>();  
        signMap.put("storeid",storeid);  
        signMap.put("deviceid",deviceid);
        signMap.put("timetemp",timetemp);*/
		return ParamSignUtils.sign(signMap).get("sign").toUpperCase();
	}
	
//	/**
//	 * 根据手机号码获取城市ID
//	 * @param areaName
//	 * @return
//	 */
//	public int getCityIdByMobile(String mobile) {
//		int areaId = 68;//默认深圳
//		if(WeixinUtils.valideMobile(mobile)) {
//			String cityName = WeixinUtils.getCityByMobile(mobile).getCity();
//			if(!StringUtils.isBlank(cityName)) {
//				Area area = areaService.findByAreaName(cityName);
//				if(area != null) {
//					areaId = area.getAreaId();
//				}
//			}
//		}
//		return areaId;
//	}
	
	/**
	 * @return the wechatTempService
	 */
	public WechatTempService getWechatTempService() {
		return wechatTempService;
	}
	/**
	 * @param wechatTempService the wechatTempService to set
	 */
	public void setWechatTempService(WechatTempService wechatTempService) {
		this.wechatTempService = wechatTempService;
	}
	
	/**
	 * @return the weixinCache
	 */
	public GuavaCache getWeixinCache() {
		return weixinCache;
	}
	/**
	 * @param weixinCache the weixinCache to set
	 */
	public void setWeixinCache(GuavaCache weixinCache) {
		this.weixinCache = weixinCache;
	}
}
