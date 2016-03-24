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
