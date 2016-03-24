package com.framework.core.app.comm.interceptor;

import java.io.Writer;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.framework.core.api.model.WechatTemp;
import com.framework.core.app.biz.WeixinBiz;
import com.framework.core.app.comm.Result;
import com.framework.core.app.comm.annotations.RequireAuthed;
import com.framework.core.app.comm.utils.AppUtils;

public class AuthInterceptor implements HandlerInterceptor{
	private Logger log = Logger.getLogger(AuthInterceptor.class);
	@Autowired
	private WeixinBiz weixinBiz;
	private final String key = "time20160317";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long time = System.currentTimeMillis();
		request.setAttribute(key, time);
		String openid = request.getParameter("openid");
		//判断参数中的openid，若为空则取cookie的openid
		if(StringUtils.isBlank(openid)){
			openid = AppUtils.openId();
		}
		if(handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			RequireAuthed require = handlerMethod.getBeanType().getAnnotation(RequireAuthed.class);
			if(require==null){
				Method method = handlerMethod.getMethod();
				require = method.getAnnotation(RequireAuthed.class);
			}
			//这里区别是ajax请求还是页面请求，ajax请求返回json，如果是页面请求返回错误页面
			if(require!=null){
				if(StringUtils.isBlank(openid)){
					Writer out = response.getWriter();
					response.setCharacterEncoding("UTF-8");  
				    response.setContentType("application/json; charset=utf-8"); 
					Result<String> obj = new Result<>();
					obj.setCode(401);
					obj.setMsg("openid失效，请重新授权登录");
					out.write(AppUtils.toJson(obj));
					out.close();
					return false;
				}
				WechatTemp wechatTemp = weixinBiz.getWechatTemp(openid);
				if(wechatTemp==null){
					Writer out = response.getWriter();
					response.setCharacterEncoding("UTF-8");  
				    response.setContentType("application/json; charset=utf-8"); 
					Result<String> obj = new Result<>();
					obj.setCode(402);
					obj.setMsg("你还未进行微信授权，请进行微信授权登录");
					out.write(AppUtils.toJson(obj));
					out.close();
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		long startTime = (long)request.getAttribute(key);
		log.info(MessageFormat.format("接口{0}:执行时长：{1}ms.", request.getRequestURI(),System.currentTimeMillis()-startTime));
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
	}
}
