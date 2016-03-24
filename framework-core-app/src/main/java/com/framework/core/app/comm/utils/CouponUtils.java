package com.framework.core.app.comm.utils;

public class CouponUtils {
//	/**
//	 * 判断优惠券发放时间，数量
//	 * @param coupons
//	 * @return
//	 */
//	public static List<Coupon> checkCoupon(List<Coupon> coupons) {
//		if(coupons != null && !coupons.isEmpty()) {
//			Coupon coupon = null;
//			for(int i=0;i<coupons.size();i++) {
//				//分发时间判断
//				boolean flag1 = false;
//				boolean flag2 = false;
//				coupon = coupons.get(i);
//				if(coupon.getOutStarttimeOne() != 0 && coupon.getOutEndtimeOne() != 0) {
//					flag1 = DateUtils.judgeShakeTime(coupon.getOutStarttimeOne(), coupon.getOutEndtimeOne());
//				}
//				if(coupon.getOutTwoUsed() == 1) {
//					flag2 = DateUtils.judgeShakeTime(coupon.getOutStarttimeTwo(),coupon.getOutEndtimeTwo());
//				}
//				
//				if(!flag1 && !flag2) {
//					coupons.remove(i);
//					continue;
//				}
//				
//				if(flag1 && coupon.getOutCountOne() < 1) {
//					coupons.remove(i);
//					continue;
//				}
//				
//				if(flag2 && coupon.getOutCountTwo() < 1) {
//					coupons.remove(i);
//					continue;
//				}
//			}
//		}
//		return coupons;
//	}
}
