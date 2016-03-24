 package com.framework.core.app.comm.interceptor;

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

import com.framework.core.api.model.WechatTemp;
import com.framework.core.app.biz.DispatcherBiz;
import com.framework.core.app.biz.WeixinBiz;
import com.framework.core.app.comm.utils.AppUtils;

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
		
		//普通摇一摇流程
		if(request.getRequestURI().indexOf("dispatcher.shake") < 0) {
			String action = new File(request.getRequestURI()).getName().split("\\.")[0];
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
		if(request.getRequestURI().indexOf("dispatcher.shake") < 0){
			//缓存摇到的历史模板
			String action = new File(request.getRequestURI()).getName().split("\\.")[0];
			Map<String,Object> map = modelAndView.getModel();
			Object obj1 = map.get("shakeUrl");
			if(obj1!=null && obj1 instanceof String){
//				deviceShakeInfo.setShakeUrl((String)obj1);
			}
			Object obj2 = map.get("coupon");
			Object obj3 = map.get("shakeFlow");
			if(obj3!=null && obj3 instanceof String){
//				deviceShakeInfo.setShakeFlow((String)obj3);
			}
		}
	}
}
