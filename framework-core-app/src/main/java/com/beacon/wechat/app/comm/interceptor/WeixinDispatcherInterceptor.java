 package com.beacon.wechat.app.comm.interceptor;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.beacon.wechat.api.model.WechatTemp;
import com.beacon.wechat.app.biz.DispatcherBiz;
import com.beacon.wechat.app.biz.WeixinBiz;
import com.beacon.wechat.app.comm.utils.AppUtils;

public class WeixinDispatcherInterceptor implements HandlerInterceptor{
	@Autowired
	private WeixinBiz weixinBiz;
//	@Autowired
//	private DeviceService deviceService;
//	@Autowired
//	private StoreService storeService;
	@Autowired
	private DispatcherBiz  dispatcherBiz;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String openid = request.getParameter("openid");
		String aid = request.getParameter("aid");
		String timetemp = String.valueOf(System.currentTimeMillis());
		String baseurl = AppUtils.baseurl();
		Map<String, String> signMap = new HashMap<String, String>();
		signMap.put("aid",aid);  
        signMap.put("timetemp",timetemp);
		if(StringUtils.isBlank(openid)) {
			log.info(MessageFormat.format("==分发前，用户的openid：{0}为空，重新进行微信静默授权", openid));
			response.sendRedirect(MessageFormat.format("{0}/wechat/auth?aid={1}&callback={2}&timetemp={3}&sign={4}",
					baseurl,aid,request.getRequestURL().toString(),
					timetemp,weixinBiz.md5sign(signMap)));
			 return false;
		}
		//数据库或缓存中不存在微信用户，则用户授权注册新用户或者绑定旧账户
		WechatTemp wechatTemp = weixinBiz.getWechatTemp(openid);
		if(wechatTemp==null) {
			log.info(MessageFormat.format("==分发前，用户{0}的微信信息为空，重新进行微信用户授权", openid));
			response.sendRedirect(MessageFormat.format("{0}/wechat/auth?aid={1}&callback={2}&timetemp={3}&sign={4}",
					baseurl,aid,request.getRequestURL().toString(),
					timetemp,weixinBiz.md5sign(signMap)));
			return false;
		}
		
//		Store store = storeService.getById(Integer.valueOf(storeid));
//		if(store == null){
//			//返回什么也没摇到的页面
//			log.info(MessageFormat.format("==根据商铺ID：{0}未找到对应商铺", storeid));
//			response.sendRedirect(MessageFormat.format("{0}/wechat/other?storeid={1}&deviceid={2}&type={3}",
//					baseurl,storeid,deviceid,type));
//			return false;
//		}
//		
//		if(store.getStoreState() != 2) {
//			log.info(MessageFormat.format("==商铺id为({0})出现异常，该商铺尚未开启，请检查该商铺的状态", storeid));
//			response.sendRedirect(MessageFormat.format("{0}/wechat/other?storeid={1}&deviceid={2}&type={3}",
//					baseurl,storeid,deviceid,type));
//			return false;
//		}
		
//		Device device = deviceService.findByDeviceid(Integer.valueOf(deviceid));
//		if(device==null || device.getIsActive() != 1) {
//			//未激活 已禁用
//			response.sendRedirect(MessageFormat.format("{0}/wechat/other?storeid={1}&deviceid={2}&type={3}",
//					baseurl,storeid,deviceid,type));
//			return false;
//		}
		
		//普通摇一摇流程
		if(request.getRequestURI().indexOf("dispatcher.shake") < 0) {
			String action = new File(request.getRequestURI()).getName().split("\\.")[0];
			
//			if(("1".equals(type) || "4".equals(type))){
//				//只有优惠券，金币，流量，现金，广告图片几个模板会判断是否扣除自己钻石
//				int shakeType = Dispatcher.template(action);
//				int[] typeArr = {1,2,3,4,12};
//				Arrays.sort(typeArr); 
//				if(Arrays.binarySearch(typeArr,shakeType) >= 0) {
//					if(store.getIsDecDiamond()==1 && store.getDiamond()>0) {
//						//返回什么也没摇到的页面
//						response.sendRedirect(MessageFormat.format("{0}/wechat/other?storeid={1}&deviceid={2}&type={3}",
//								baseurl,storeid,deviceid,type));
//						return false;
//					}
//				}
//				
//				//记录当天当前用户摇一摇次数
//				dispatcherBiz.updateShakeCount(storeid, deviceid, openid);
//				int proSum = 1000;
//		    	int prizeCount = (int)Double.parseDouble(device.getPrizePercent())*10;//中奖率
//				int shakeNone = proSum - prizeCount;
//				Map<Integer,Integer> proArr = new HashMap<Integer,Integer>();
//				proArr.put(0, prizeCount);
//				proArr.put(1, shakeNone);
//				int result = AppUtils.getRand(proArr,proSum);
//				if(result == 1) {
//					//返回什么也没摇到的页面
//					response.sendRedirect(MessageFormat.format("{0}/wechat/other?storeid={1}&deviceid={2}&type={3}",
//							baseurl,storeid,deviceid,type));
//					return false;
//				}
//			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//保存摇一摇信息
		saveShakeInfo(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
	}
	
	
	/**
	 * 保存摇一摇信息，并扣除相应钻石
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 */
	private void saveShakeInfo(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String openid = request.getParameter("openid");
		String aid = request.getParameter("aid");
//		String pagestore = request.getParameter("pagestore");
//		String deviceid = request.getParameter("deviceid");
//		String type = request.getParameter("type");
		
		if(request.getRequestURI().indexOf("dispatcher.shake") < 0){
//			if(StringUtils.isBlank(type)){
//				type = "1";
//			}
//			if(StringUtils.isBlank(pagestore)){
//				pagestore = storeid;
//			}
			
			//缓存摇到的历史模板
			String action = new File(request.getRequestURI()).getName().split("\\.")[0];
//			dispatcherBiz.cacheHistory(storeid, deviceid, openid, action,Integer.valueOf(type),pagestore);
//			
//			WechatTemp wechatTemp = weixinBiz.getWechatTemp(openid);
//			Device device = deviceService.findByDeviceid(Integer.valueOf(deviceid));
//			Store store = storeService.getById(Integer.valueOf(pagestore));
//			int shakeType = Dispatcher.template(action);
//			
//			//广告分发
//			if(("3".equals(type))) {
//				//更新分发商铺的剩余投放次数
//				dispatcherBiz.updateStoreAdvertNum(pagestore, store.getAdvertNumResidue());
//			}
			
//			DeviceShakeInfo deviceShakeInfo = new DeviceShakeInfo();
//			deviceShakeInfo.setChannel(Integer.valueOf(type));
//			deviceShakeInfo.setOpenId(openid);
//			deviceShakeInfo.setShakeStoreId(store.getStoreId());
//			deviceShakeInfo.setShakeStoreName(store.getStoreName());
//			deviceShakeInfo.setShakeStoreDiamond(store.getDiamond());
//			deviceShakeInfo.setShakeType(shakeType); 
//			deviceShakeInfo.setNickName(wechatTemp.getNickname());
			
			Map<String,Object> map = modelAndView.getModel();
			Object obj1 = map.get("shakeUrl");
			if(obj1!=null && obj1 instanceof String){
//				deviceShakeInfo.setShakeUrl((String)obj1);
			}
			Object obj2 = map.get("coupon");
//			if(obj2!=null && obj2 instanceof Coupon){
//				Coupon coupon = (Coupon)obj2;
//				deviceShakeInfo.setCouponName(coupon.getCouponName());
//				deviceShakeInfo.setShakeCoupon(coupon.getCouponId());
//			}
			Object obj3 = map.get("shakeFlow");
			if(obj3!=null && obj3 instanceof String){
//				deviceShakeInfo.setShakeFlow((String)obj3);
			}
//			deviceShakeInfo.setDeviceId(device.getId());
//			if(device.getType()==1){
//				deviceShakeInfo.setDeviceStoreName(device.getBelongName());
//				deviceShakeInfo.setDeviceStoreId(device.getBelongId());
//			}
//			deviceShakeInfo.setShakeTime(DateUtils.phptime());
//			try {
//				dispatcherBiz.saveShakeInfo(deviceShakeInfo);
//			} catch (DatabaseExecuteFailedException e) {
//				log.error("==保存摇一摇信息失败==");
//				log.error(e);
//			}
			
//			checkMember(modelAndView,openid,pagestore,type);
		}
	}
	
	/**
	 * 是否需要显示手机号码以及商铺通知
	 * @param modelAndView
	 * @param openid
	 * @param pageStore
	 */
	private void checkMember(ModelAndView modelAndView,String openid,String pageStore,String type) {
//		Member member = weixinBiz.getMember(openid);
//		if(member == null) {
//			modelAndView.addObject("needMobile", 1);//需要显示手机号码
//		} else {
//			modelAndView.addObject("needMobile", 0);
//			
//			//通知商铺
//			
//			//预制名单检查
//		}
	}
}
