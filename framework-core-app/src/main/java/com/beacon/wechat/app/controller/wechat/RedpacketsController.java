package com.beacon.wechat.app.controller.wechat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beacon.wechat.app.biz.RedpacketsBiz;
import com.beacon.wechat.app.comm.Result;
import com.beacon.wechat.app.comm.annotations.RequireAuthed;
import com.beacon.wechat.app.comm.runnable.RedpacketsRunnable;
import com.beacon.wechat.app.comm.utils.AppUtils;
import com.beacon.wechat.app.comm.utils.ThreadPoolUtils;
@Controller 
@RequestMapping("/redpackets")
public class RedpacketsController {
	private Logger log = Logger.getLogger(RedpacketsController.class);
	@Autowired
	private RedpacketsBiz redpacketsBiz;
	/**
	 * 红包
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @param mobile
	 * @return
	 */
	@RequireAuthed
	@ResponseBody
	@RequestMapping(value="/update",method=RequestMethod.GET) 
	public Result<Object> redpackets(
			@RequestParam(name="number",required=true)String serialNumber,
			@RequestParam(name="redid",required=true)String redid) { 
		String openid = AppUtils.openId();
		Result<Object> result =  redpacketsBiz.filter(openid, redid,serialNumber);
		if(result.getCode()==5){
			//添加拦截
			redpacketsBiz.addAccess(openid, redid, serialNumber);
			//丢进队列
			ThreadPoolUtils.getExecutor().execute(new RedpacketsRunnable(openid, redid,serialNumber));
			
		}
		return result;
	} 
}
