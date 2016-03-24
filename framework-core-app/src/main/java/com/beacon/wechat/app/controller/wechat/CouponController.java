package com.beacon.wechat.app.controller.wechat;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beacon.wechat.app.biz.CouponBiz;
import com.beacon.wechat.app.biz.WeixinBiz;
import com.beacon.wechat.app.comm.Result;
import com.beacon.wechat.app.comm.annotations.RequireAuthed;
import com.beacon.wechat.app.comm.utils.AppUtils;
import com.beacon.wechat.app.exception.DatabaseExecuteFailedException;
import com.beacon.wechat.app.exception.UserExecuteFailedException;
import com.beacon.wechat.app.vo.OrderVO;

@RestController 
@RequestMapping("/coupon")
public class CouponController {
	@Autowired
	private CouponBiz couponBiz;
	@Autowired
	private WeixinBiz weixinBiz;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public Result<Map<String,Object>> create(OrderVO order){
		return new Result<>();
	}
	
	/**
	 * 摇一摇领取优惠券
	 * @return
	 */
	@RequireAuthed
	@RequestMapping(value="/get",method=RequestMethod.POST)
	public Result<OrderVO> get(@RequestParam(name="mobile",defaultValue="")String mobile,
			@RequestParam(name="openid",required=true)String openid,
			@RequestParam(name="storeid",required=true)String storeid,
			@RequestParam(name="pagestore",required=true)String pagestore,
			@RequestParam(name="deviceid",required=true)String deviceid,
			@RequestParam(name="couponid",required=true)String couponid,
			@RequestParam(name="timetemp",defaultValue="")String timetemp,
			@RequestParam(name="sign",defaultValue="")String sign) {
		
		//签名不通过
		Map<String, String> signMap = new HashMap<String, String>();
		signMap.put("openid",openid);  
		signMap.put("storeid",storeid);  
		signMap.put("pagestore",pagestore);  
        signMap.put("deviceid",deviceid);
        signMap.put("couponid",couponid);
        signMap.put("timetemp",timetemp);
		if(!weixinBiz.checkSign(signMap, sign)){
			return new Result<>(Result.CODE_FAIL,"签名错误");
		}
		
		//判断参数中的openid，若为空则取cookie的openid
		if(StringUtils.isBlank(openid)){
			openid = AppUtils.openId();
		}
		
		OrderVO orderVO = null;
		try {
//			//根据openid查找用户信息
//			Member member = weixinBiz.getMember(openid);
//			if(member == null) {
//				member = weixinBiz.bindMember(storeid, openid, mobile);
//			}
//		
//			//关注商铺
//		
//			//发放优惠券
//			orderVO = couponBiz.sendCoupon(couponid, member, "");
		} catch(UserExecuteFailedException userException) { 
			return new Result<>(userException.getCode(),userException.getMessage());
		} catch (Exception e) {
			log.error(MessageFormat.format("==用户（{0}）摇一摇领取优惠券（{1}）时出现异常",openid,couponid));
			return new Result<>(Result.CODE_FAIL,"系统繁忙，稍后重试");
		}
		return new Result<OrderVO>(Result.CODE_SUCCUSS,"OK",orderVO);
	}
	
	
	/**
	 * 抽奖发放优惠券接口
	 * @param open_id
	 * @param coupon_id
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/send",method=RequestMethod.POST)
	public Result<OrderVO> send(@RequestParam(name="open_id",required=true)String open_id,
			@RequestParam(name="coupon_id",required=true)String coupon_id,
			@RequestParam(name="type",defaultValue="")String type) {
		
		if(StringUtils.isBlank(coupon_id) || StringUtils.isBlank(open_id)) {
			return new Result<>(Result.CODE_FAIL,"参数异常");
		}
		
		OrderVO orderVO = null;
		try {
//			Member member = weixinBiz.findByOpenid(open_id);
//			orderVO = couponBiz.sendCoupon(coupon_id, member, type);
		} catch(UserExecuteFailedException userException) { 
			return new Result<>(userException.getCode(),userException.getMessage());
		} catch (Exception e) {
			log.error(MessageFormat.format("==通过接口给用户（{0}）发放优惠券（{1}）时出现异常",open_id,coupon_id));
			return new Result<>(Result.CODE_FAIL,"系统繁忙，稍后重试");
		}
		return new Result<OrderVO>(Result.CODE_SUCCUSS,"OK",orderVO);
	}
}
