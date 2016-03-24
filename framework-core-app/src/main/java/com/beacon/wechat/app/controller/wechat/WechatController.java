package com.beacon.wechat.app.controller.wechat;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.beacon.wechat.api.model.WechatTemp;
import com.beacon.wechat.app.biz.DispatcherBiz;
import com.beacon.wechat.app.biz.RedpacketsBiz;
import com.beacon.wechat.app.biz.WeixinBiz;
import com.beacon.wechat.app.comm.Constant;
import com.beacon.wechat.app.comm.cache.GuavaCache;
import com.beacon.wechat.app.comm.utils.AppUtils;
import com.beacon.wechat.app.vo.Dispatcher;

import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
@Controller 
@RequestMapping("/wechat")
public class WechatController {
	private Logger log = Logger.getLogger(WechatController.class);
	@Autowired
	private WeixinBiz weixinBiz;
	@Autowired
	private DispatcherBiz dispatcherBiz;
	@Autowired
	private GuavaCache userCache;
	@Autowired
	private RedpacketsBiz redpacketsBiz;
	/**
	 * 静默授权,授权成功并把openid保存到cookie
	 * @param code
	 * @return WxMpUser
	 */
	@RequestMapping(value="/base",method=RequestMethod.GET)
	private String baseAuth(@RequestParam(name="code",defaultValue="")String code,
			@RequestParam(name="aid",required=true)String aid,
			@RequestParam(name="callback",defaultValue="")String callback) { 
		if(StringUtils.isBlank(code)){
			//跳转授权
			return "redirect:" + weixinBiz.baseAuth(AppUtils.url());
		}
		WxMpOAuth2AccessToken token = weixinBiz.oauth2getAccessToken(code);
		AppUtils.setCookie(Constant.COOKIE_OPENID, token.getOpenId(), 10*60*60);
		String now = String.valueOf(System.currentTimeMillis());
		Map<String, String> signMap = new HashMap<String, String>();
        signMap.put("aid",aid);
        signMap.put("timetemp",now);
		return MessageFormat.format("redirect:auth?aid={0}&callback={1}&openid={2}&timetemp={3}&sign={4}", 
				aid,callback,token.getOpenId(),now,weixinBiz.md5sign(signMap));
	}
	
	/**
	 * 用户授权，并存储微信用户信息，并且把用户缓存到缓存当中
	 * @param code
	 * @return
	 */
	@RequestMapping(value="/user",method=RequestMethod.GET)
	public String userAuth(@RequestParam(name="code",defaultValue="")String code,
			@RequestParam(name="aid",required=true)String aid,
			@RequestParam(name="callback",defaultValue="")String callback) { 
		if(StringUtils.isBlank(code)){
			//跳转用户授权
			return "redirect:" + weixinBiz.userInfoAuth(AppUtils.url());
		}
		
		WxMpUser user = null;
		try {
			WxMpOAuth2AccessToken token = weixinBiz.oauth2getAccessToken(code);
			user = weixinBiz.oauth2getUserInfo(token);
			//注册新用户或者绑定用户
			log.info(AppUtils.toJson(user));
			weixinBiz.saveTemp(user);
		} catch (Exception e) {
			log.error("==保存wechattemp信息时数据库操纵失败==");
			log.error(e);
			return "redirect:other";
		}
		
		String now = String.valueOf(System.currentTimeMillis());
		Map<String, String> signMap = new HashMap<String, String>();
		signMap.put("aid",aid);  
        signMap.put("timetemp",now);
		return MessageFormat.format("redirect:auth?aid={0}&callback={1}&openid={2}&timetemp={3}&sign={4}", 
				aid,callback,user.getOpenId(),now,weixinBiz.md5sign(signMap));
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value="/index.php",method=RequestMethod.GET) 
	public String index(String code) { 
		return null;
	}
	
	/**
	 * 授权总路
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/auth",method=RequestMethod.GET)
	public String auth(@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid,
			@RequestParam(name="callback",defaultValue="")String callback,
			@RequestParam(name="timetemp",defaultValue="")String timetemp,
			@RequestParam(name="sign",defaultValue="")String sign) { 
		//签名不通过
		Map<String, String> signMap = new HashMap<String, String>();
        signMap.put("aid",aid);
        signMap.put("timetemp",timetemp);
		if(!weixinBiz.checkSign(signMap, sign)){
			return "redirect:other";
		}
		//判断参数中的openid，若为空则取cookie的openid
		if(StringUtils.isBlank(openid)){
			openid = AppUtils.openId();
		}
		//判断cookie是否有openid，若为空则静默授权
		if(StringUtils.isBlank(openid)){
			return MessageFormat.format("redirect:base?aid={0}&callback={1}", 
					aid,callback);
		}
		//过滤，拦截控制
		if(!weixinBiz.filter(openid, AppUtils.request().getRequestURI())){
			return "redirect:other";
		}
		//数据库或缓存中不存在微信用户，则用户授权注册新用户或者绑定旧账户
		WechatTemp wechatTemp = weixinBiz.getWechatTemp(openid);
		if(wechatTemp==null){
			return MessageFormat.format("redirect:user?aid={0}&callback={1}", 
					aid,callback);
		}
		if(StringUtils.isNotBlank(callback)){
			return MessageFormat.format("redirect:{0}?aid={1}&openid={2}",
					callback,aid,openid);
		}
		return "redirect:other";
	}
	/**
	 * 拦截后跳转地址
	 * @return
	 */
	@RequestMapping(value="/other",method=RequestMethod.GET) 
	public ModelAndView other() { 
		System.err.println(AppUtils.request().getParameter("type"));
		return new ModelAndView("wechat/other");
	}
	/**
	 * 分发器
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/dispatcher.shake",method=RequestMethod.GET) 
	public ModelAndView dispatcher(
			@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
//		Dispatcher page = dispatcherBiz.shakePage(openid,storeid,deviceid);
		Dispatcher page = new Dispatcher();
		String url = MessageFormat.format("forward:{0}?type={1}&pagestore={2}",page.page(),page.getType(),page.getPagestore());
		log.info("分发路径："+url);
		return new ModelAndView(url);
	}
	/**
	 * 流量
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/ad.shake",method=RequestMethod.GET) 
	public ModelAndView ad(@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
		
		return new ModelAndView("wechat/ad");
	}
	/**
	 * 流量
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/shakeredpackets.shake",method=RequestMethod.GET) 
	public ModelAndView shakeredpackets(@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
		
		return new ModelAndView("wechat/shakeredpackets");
	}
	/**
	 * 流量
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/flows.shake",method=RequestMethod.GET) 
	public ModelAndView flows(@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
		
		return new ModelAndView("wechat/flows");
	}
	/**
	 * 金币
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/gold.shake",method=RequestMethod.GET) 
	public ModelAndView gold(@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
		
		return new ModelAndView("wechat/gold");
	}
	/**
	 * 签到
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/sign.shake",method=RequestMethod.GET) 
	public ModelAndView sign(
			@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
		ModelAndView model = new ModelAndView("wechat/sign");
		model.addObject("openid", openid);
//		model.addObject("storeid", storeid);
//		model.addObject("deviceid", deviceid);
//		model.addObject("type", type);
		return model;
	}
	/**
	 * 红包
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/redpackets.shake",method=RequestMethod.GET) 
	public ModelAndView redpackets(@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
		//获取当前红包信息
//		Redpack redpack = dispatcherBiz.getRedpacket(openid,storeid, deviceid, pagestore, type);
//		if(redpack==null){
//			return new ModelAndView("forward:other");
//		}
//		Result<Object> result = redpacketsBiz.filter(openid,String.valueOf(redpack.getId()),redpack.getSerialNumber());
//		if(result.getCode()==5){
//			return new ModelAndView("forward:other");
//		}
		ModelAndView view = new ModelAndView("wechat/redpackets");
//		view.addObject("redpack", redpack);
//		view.addObject("result", result);
		return view;
	}
	/**
	 * 祝福墙
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/wishwall.shake",method=RequestMethod.GET) 
	public ModelAndView wishwall(@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
		ModelAndView model = new ModelAndView("wechat/wishwall");
		model.addObject("openid", openid);
		return model;
	}
	/**
	 * 支付
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/payment.shake",method=RequestMethod.GET) 
	public ModelAndView payment(@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
		
		
		return new ModelAndView("wechat/payment");
	}
	
	/**
	 * 优惠券
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/coupon.shake",method=RequestMethod.GET) 
	public ModelAndView coupon(@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
//		Coupon coupon = dispatcherBiz.getCoupon(openid,storeid,deviceid,type,pagestore);
//		if(coupon==null){
//			return new ModelAndView("forward:other");
//		}
		
		//生成签名
		Map<String, String> signMap = new HashMap<String, String>();
//		signMap.put("storeid",storeid);  
//        signMap.put("deviceid",deviceid);
//        signMap.put("pagestore",pagestore);
        signMap.put("openid",openid);
//        signMap.put("couponid",String.valueOf(coupon.getCouponId()));
        signMap.put("timetemp",String.valueOf(System.currentTimeMillis()));
        String sign = weixinBiz.md5sign(signMap);
        
        //需判断该优惠券是否是活动型优惠券
        
        
		ModelAndView model = new ModelAndView("wechat/coupon");
//		model.addObject("coupon", coupon);
		model.addObject("openid", openid);
//		model.addObject("storeid", storeid);
//		model.addObject("deviceid", deviceid);
//		model.addObject("pagestore", pagestore);
//		model.addObject("type", type);
		model.addObject("sign", sign);
		return model;
	}
	
	/**
	 * 投票
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/vote.shake",method=RequestMethod.GET) 
	public ModelAndView vote(@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
		
		
		return new ModelAndView("wechat/vote");
	}
	
	/**
	 * 疯狂摇
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/crazyshake.shake",method=RequestMethod.GET) 
	public ModelAndView crazyShake(@RequestParam(name="openid",defaultValue="")String openid,
			@RequestParam(name="aid",required=true)String aid) { 
		
		return new ModelAndView("wechat/crazyshake");
	}
	public GuavaCache getUserCache() {
		return userCache;
	}
	public void setUserCache(GuavaCache userCache) {
		this.userCache = userCache;
	}

	/**
	 * @return the dispatcherBiz
	 */
	public DispatcherBiz getDispatcherBiz() {
		return dispatcherBiz;
	}

	/**
	 * @param dispatcherBiz the dispatcherBiz to set
	 */
	public void setDispatcherBiz(DispatcherBiz dispatcherBiz) {
		this.dispatcherBiz = dispatcherBiz;
	}
}
