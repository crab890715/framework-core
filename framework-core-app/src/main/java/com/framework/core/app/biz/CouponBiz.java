package com.framework.core.app.biz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.framework.core.app.comm.utils.RandomUtils;

@Service("couponBiz")
public class CouponBiz {
//	@Autowired
//	private CouponService couponService;
//	@Autowired
//	private StoreService storeService;
//	@Autowired
//	private OrderService orderService;
//	
	private Logger log = Logger.getLogger(this.getClass());
//	
//	/**
//	 * 发放优惠券通用接口
//	 * @param couponId
//	 * @param openId
//	 * @param memberId
//	 * @param type
//	 * @return
//	 * @throws Exception 
//	 */
//	public OrderVO sendCoupon(String couponId,Member member,String type) throws Exception {
//		//优惠券参数异常
//		if(StringUtils.isBlank(couponId)) {
//			//优惠券已经被抢光啦，请继续关注，亲，还有别的惊喜哦~
//			log.info("==方法：" + Thread.currentThread().getStackTrace()[1].getMethodName() + "调用出错。错误原因：couponId为空");
//			throw new UserExecuteFailedException(Result.CODE_FAIL,"参数异常");
//		}
//		
//		//用户数据为空
//		if(member == null) {
//			//优惠券已经被抢光啦，请继续关注，亲，还有别的惊喜哦~
//			log.info("==方法：" + Thread.currentThread().getStackTrace()[1].getMethodName() + "调用出错。错误原因：member为空");
//			throw new UserExecuteFailedException(Result.CODE_FAIL,"参数异常");
//		}
//		
//		//根据couponId查询优惠券
//		Long today = DateUtils.getStartTime(0l);
//		Coupon coupon = couponService.selectActiveCoupon(couponId,today);
//		
//		//验证优惠券发放时间数量
//		Map<String, Object> data = checkCoupon(coupon);
//		//没有找到对应优惠券或优惠券数量为0,或发放数量数据不符
//		if(data == null) {
//			//优惠券已经被抢光啦，请继续关注，亲，还有别的惊喜哦~
//			log.info(MessageFormat.format("==优惠券id为({0})出现异常，请检查该优惠券是否打开，且分发时间和分发数量是否符合。", couponId));
//			throw new UserExecuteFailedException(Result.CODE_FAIL,"优惠券已经被抢光啦，请继续关注，亲，还有别的惊喜哦~");
//		}
//		boolean flag1 = (boolean)data.get("flag1");
//		boolean flag2 = (boolean)data.get("flag2");
//		
//		//该用户已领优惠券数量
//		int memberCount = orderService.findOrderCountByMember(member.getMemberId(), Integer.valueOf(couponId));
//		if(memberCount >= coupon.getCouponExchangeLimit()) {
//			//您已经领取过该优惠券啦！后面再试试吧，亲，还有别的惊喜哦
//			log.info(MessageFormat.format("==用户->{0}<-已领取优惠券({1},{2})的数量超过该优惠券的每人最大兑换量->{3}", 
//						member.getMemberId(),coupon.getCouponId(),coupon.getCouponName(),coupon.getCouponExchangeLimit()));
//			throw new UserExecuteFailedException(Result.CODE_FAIL,"优惠券已经被抢光啦，请继续关注，亲，还有别的惊喜哦~");
//		}
//		
//		//查询对应商铺信息
//		Store store = storeService.getById(coupon.getStoreId());
//		if(store == null) {
//			log.info(MessageFormat.format("==优惠券id为({0})出现异常，根据该优惠券未找到对应的商铺", couponId));
//			throw new UserExecuteFailedException(Result.CODE_FAIL,"优惠券已经被抢光啦，请继续关注，亲，还有别的惊喜哦~");
//		}
//		
//		if(store.getStoreState() != 2) {
//			log.info(MessageFormat.format("==商铺id为({0})出现异常，该商铺尚未开启，请检查该商铺的状态", coupon.getStoreId()));
//			throw new UserExecuteFailedException(Result.CODE_FAIL,"优惠券已经被抢光啦，请继续关注，亲，还有别的惊喜哦~");
//		}
//		
//		try {
//			Order order = new Order();
//			order.setMemberId(member.getMemberId());
//			order.setMemberName(member.getMemberName());
//			order.setMobile(member.getMobile());
//			order.setStoreId(store.getStoreId());
//			order.setStoreName(store.getStoreName());
//			order.setAddTime(DateUtils.phptime());
//			order.setItemId(coupon.getCouponId());
//			order.setItemName(coupon.getCouponName());
//			order.setOrderType(Order.TYPE.WECHAT.getValue());
//			order.setState(Order.STATE.EXCHANGE.getValue());
//			order.setPrice(coupon.getCouponOriginalPrice());
//			order.setType(type);
//			order.setOrderOut(createOrderOut());
//			order.setOrderSn(createOrderSn());
//			order.setNumber(1);
//			
//			//生成订单
//			createOrder(order,coupon,flag1,flag2);
//			
//			//发送短信
//			int[] testMembers = {12184,6631};//测试人员id
//			Arrays.sort(testMembers); 
//			if(member.getIsSendmsg() == 1 && Arrays.binarySearch(testMembers, member.getMemberId()) < 0) {
//				sendCouponMsg(coupon,member,store,order);
//			}
//			
//			//返回核销密码
//			OrderVO orderVO = new OrderVO();
//			orderVO.setOpenId(member.getOpenId());
//			orderVO.setStoreId(String.valueOf(store.getStoreId()));
//			orderVO.setOrderOut(order.getOrderOut());
//			orderVO.setCouponName(coupon.getCouponName());
//			return orderVO;
//		} catch(DatabaseExecuteFailedException databaseException) {
//			log.error(MessageFormat.format("==用户（{0}）领取优惠券id为（{1}）时出现异常，数据库生成订单信息失败", member.getMemberId(),coupon.getCouponId()));
//			throw databaseException;
//		} catch (Exception e) {
//			log.error(MessageFormat.format("==用户（{0}）领取优惠券id为（{1}）时出现异常，生成订单对象失败", member.getMemberId(),coupon.getCouponId()));
//			throw new UserExecuteFailedException(Result.CODE_FAIL,"系统繁忙，稍后重试~");
//		}
//	}
//	
//	@Transactional(rollbackFor=Exception.class)
//	public void createOrder(Order order,Coupon coupon,boolean flag1,boolean flag2) throws DatabaseExecuteFailedException {
//		//生成订单
//		order = orderService.save(order);
//		
//		//更新优惠券信息
//		Long oldUpdateTime = coupon.getUpdateTime();//上次更新时间戳
//		coupon.setCouponCount(coupon.getCouponCount() - 1);
//		coupon.setCouponExchangeNum(coupon.getCouponExchangeNum() + 1);
//		coupon.setUpdateTime(DateUtils.phptime());
//		if(flag1) {
//			coupon.setOutCountOne(coupon.getOutCountOne() - 1);
//		} else if(flag2){
//			coupon.setOutCountTwo(coupon.getOutCountTwo() - 1);
//		}
//		couponService.updateSafeById(coupon, oldUpdateTime);
//	}
//	
//	
//	/**
//	 * 发送优惠券短信接口
//	 * @param coupon
//	 * @param member
//	 * @param store
//	 * @return
//	 */
//	public boolean sendCouponMsg(Coupon coupon,Member member,Store store,Order order) {
//		String content = MessageFormat.format("您的{0}的兑换码是：{1}，使用时请将该兑换码出示给店员。",coupon.getCouponName(),order.getOrderOut());
//		switch (coupon.getCouponId()) {
//			case 1360:
//			case 1361:
//			case 1362:
//				content = MessageFormat.format("恭喜您获得{0}（兑换码{1}）一张！请您在12月20日晚21:30前移步至大疆欢乐海岸旗舰店门口活动兑换处领取代金券，过期失效。",
//						coupon.getCouponName(),order.getOrderOut());
//				break;
//			case 1363:
//				content = MessageFormat.format("恭喜您获得{0}（兑换码{1}）一台！请您现在上台领奖，逾期失效。",
//						coupon.getCouponName(),order.getOrderOut());
//				break;
//			case 1880:
//				content = "亲爱的嘉宾，恭喜您成功领取了【大宋官窑】提供的价值18880元的现金礼券，可在关注公众号“瓷骋(cheng)天下”后，在“我的红包”里查看。 ";
//				break;			
//		}
//		
//		String result = WeixinUtils.sendMsg(member.getMobile(), content);
//		log.info(MessageFormat.format("==手机号码{0} 领取优惠券({1})发送短信,返回{2}", member.getMobile(),coupon.getCouponName(),result));
//		return true;
//	}
//	
//	public Map<String,Object>checkCoupon(Coupon coupon) {
//		if(coupon == null) {
//			return null;
//		}
//		
//		//分发时间判断
//		boolean flag1 = false;
//		boolean flag2 = false;
//		if(coupon.getOutStarttimeOne() != 0 && coupon.getOutEndtimeOne() != 0) {
//			flag1 = DateUtils.judgeShakeTime(coupon.getOutStarttimeOne(), coupon.getOutEndtimeOne());
//		}
//		if(coupon.getOutTwoUsed() == 1) {
//			flag2 = DateUtils.judgeShakeTime(coupon.getOutStarttimeTwo(),coupon.getOutEndtimeTwo());
//		}
//		
//		if(!flag1 && !flag2) {
//			return null;
//		}
//		
//		if(flag1 && coupon.getOutCountOne() < 1) {
//			return null;
//		}
//		
//		if(flag2 && coupon.getOutCountTwo() < 1) {
//			return null;
//		}
//		
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("coupon", coupon);
//		map.put("flag1", flag1);
//		map.put("flag2", flag2);
//		return map;
//	}
//	
	/**
	 * 生成16位纯数字订单编号
	 * @return
	 */
	private String createOrderSn() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date) + RandomUtils.generateNumber();
	}
	
	/**
	 * 生成12位纯数字核销密码
	 * @return
	 */
	private String createOrderOut() {
		StringBuilder builder = new StringBuilder();
		builder.append(RandomUtils.generateNumber().substring(3));
		builder.append(RandomUtils.getRandomByNanoTime());
		builder.append(String.format("%02d", new Random().nextInt(99)));
		return builder.toString();
	}
	
}
