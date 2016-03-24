package com.beacon.wechat.app.biz;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.beacon.wechat.app.comm.utils.DateUtils;
import com.beacon.wechat.app.vo.Dispatcher;

@Service("dispatcherBiz")
public class DispatcherBiz {
	
//	@Autowired
//	private DeviceShakeInfoService deviceShakeInfoService;
//	@Autowired
//	private DeviceService deviceService;
//	@Autowired
//	private DevicePageService devicePageService;
//	@Autowired
//	private CouponService couponService;
//	@Autowired
//	private StoreAdvertService storeAdvertService;
//	@Autowired
//	private StoreService storeService;
//	@Autowired
//	private DeviceShakeCountService deviceShakeCountService;
//	@Autowired
//	private RedpackService redpackService;
	@Autowired
	private RedisOperations<String,String> shakeHistoryRedisTemplate;
	
	/**
	 * 摇一摇历史
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @return
	 */
	private String getShakeKey(String openid, String storeid,String deviceid){
		return MessageFormat.format("shake_{0}_{1}_{2}", openid,storeid,deviceid);
	}
	
	/**
	 * 普通摇一摇次数
	 * @param openid
	 * @param storeid
	 * @param deviceid
	 * @return
	 */
	private String getShakeCountKey(String openid, String storeid, String deviceid) {
		return MessageFormat.format("shake_count_{0}_{1}_{2}", openid,storeid,deviceid);
	}
	
	/**
	 * 用户当日在分发商铺的摇到的广告次数
	 * @param sid
	 * @param targetSid
	 * @return
	 */
	public String getAdvertStoreTimeKey(String targetSid,String openid){
		return MessageFormat.format("advert_store_time_{0}_{1}", targetSid, openid);
	}
	
	/**
	 * 用户当日在该设备摇到广告分发次数
	 * @param did
	 * @param openid
	 * @return
	 */
	public String getAdvertDeviceTimeKey(String did,String openid){
		return MessageFormat.format("advert_device_time_{0}_{1}", did,openid);
	}
	
//	/**
//	 * 拆分device_page为普通page和交叉分发page，以及判断优惠券数量和分发时间，不满足则剔除该优惠券
//	 * @param pages
//	 * @param storeid
//	 * @return
//	 */
//	private Map<String,List<DevicePage>> parseDevicePage(List<DevicePage> pages,Integer storeid) {
//		DevicePage page = null;
//		List<DevicePage> shop_pages = new ArrayList<DevicePage>();	
//		List<DevicePage> admin_pages = new ArrayList<DevicePage>();
//		Map<String, List<DevicePage>> result = new HashMap<String,List<DevicePage>>();
//		for(int i=0;i<pages.size();i++) {
//			page = pages.get(i);
//			if(page.getPageStore() == 0) {
//				page.setPageStore(storeid);
//            }
//			
//			//判断优惠券
//			if(page.getPageType() == 2 && page.getPageTemplate() == 1) {
//				String[] couponidArr = page.getPageCoupon().split("/");
//				List<Coupon> coupons = checkCoupon(couponidArr);
//				if(coupons == null || coupons.isEmpty()) {
//					pages.remove(i);
//					continue;
//				} else {
//					String couponids = "";
//					int count = coupons.size();
//					for(int j=0;j<count;j++) {
//						couponids += coupons.get(j).getCouponId() + "/";
//					}
//					couponids = couponids.substring(0,couponids.length()-1);
//					page.setPageCoupon(couponids);
//				}
//			}
//			
//			if(page.getPageIsAdmin() != 1) {
//				shop_pages.add(page);
//			} else {
//				admin_pages.add(page);
//			}
//		}
//		
//		result.put("shop_pages", shop_pages);
//		result.put("admin_pages", admin_pages);
//		return result;
//	}
//	
//	public List<Coupon> checkCoupon(String[] couponIds) {
//		List<Coupon> coupons = couponService.findOpenCoupon(couponIds, DateUtils.getStartTime(0l));
//		return CouponUtils.checkCoupon(coupons);
//	}
//	
//	public DeviceShakeInfo findShakeInfo(Integer deviceId,String openid) {
//		Long today = DateUtils.getStartTime(0l);
//		return deviceShakeInfoService.findShakeInfo(deviceId, openid, today);
//	}
//	
//	/**
//	 * 广告分发业务逻辑
//	 * @param openid
//	 * @param storeid
//	 * @param list2 
//	 * @return
//	 */
//	public Dispatcher shakeAdvert(String openid, String storeid, List<String> historys,Device device) {
//		//判断当前用户的他家摇次数（即用户当日在该设备摇到广告分发次数）是否超过该设备的设置
//		ValueOperations<String, String> valueOperations = shakeHistoryRedisTemplate.opsForValue();
//		String dkey = getAdvertDeviceTimeKey(String.valueOf(device.getId()),openid);
//		int dtimes = valueOperations.get(dkey) == null ? 0 : Integer.valueOf(valueOperations.get(dkey));
//		if(device.getAdvertLimit() < dtimes){
//			//返回什么也没摇到的页面
//			return new Dispatcher(1,Dispatcher.TYPE.ERR.getValue(),Integer.parseInt(storeid));
//		}
//		
//		Map<String,List<StoreAdvert>> map = getAdvertStores(storeid,openid);
//		List<StoreAdvert> allStoreAdverts = map.get("allStoreAdverts");
//		List<StoreAdvert> allClassAdverts = map.get("allClassAdverts");
//		for(StoreAdvert page : allStoreAdverts){
//			String val = Dispatcher.historyTemp(Dispatcher.TYPE.ADS.getValue(), page.getAdvType()) + "_" + page.getStoreId();
//			if(!historys.contains(val)){
//				return new Dispatcher(page.getAdvType(),Dispatcher.TYPE.ADS.getValue(),page.getStoreId());
//			}
//		}
//		
//		for(StoreAdvert page : allClassAdverts){
//			String val = Dispatcher.historyTemp(Dispatcher.TYPE.ADS.getValue(), page.getAdvType()) + "_" + page.getStoreId();
//			if(!historys.contains(val)){
//				return new Dispatcher(page.getAdvType(),Dispatcher.TYPE.ADS.getValue(),page.getStoreId());
//			}
//		}
//		allStoreAdverts.addAll(allClassAdverts);
//		if(allStoreAdverts==null || allStoreAdverts.isEmpty()){
//			//返回什么也没摇到的页面
//			return new Dispatcher(1,Dispatcher.TYPE.ERR.getValue(),Integer.parseInt(storeid));
//		}
//		Random random = new Random();
//		int n = random.nextInt(allStoreAdverts.size());
//		StoreAdvert page = allStoreAdverts.get(n);
//		return new Dispatcher(page.getAdvType(),Dispatcher.TYPE.ADS.getValue(),page.getStoreId());
//	}
//	
//	/**
//	 * 用户当前摇一摇准备分发的页面
//	 * @param openid
//	 * @param storeid
//	 * @param deviceid
//	 * @return
//	 */
//	public Dispatcher shakePage(String openid, String storeid, String deviceid) {
//		Device device = deviceService.findByDeviceid(Integer.valueOf(deviceid));
//		if(device==null || device.getIsActive() != 1) {
//			//设备为空 或未被激活，返回什么都没摇到
//			return new Dispatcher(1,Dispatcher.TYPE.ERR.getValue(),Integer.parseInt(storeid));
//		}
//		
//		Integer startTime = device.getShakeTimeStart();
//		Integer endTime = device.getShakeTimeEnd();
//		startTime = startTime == null ? 0 : startTime;
//		endTime = endTime == null ? 0 : endTime;
//		
//		//获取当前用户在当前门店缓存的摇一摇历史
//		String key = this.getShakeKey(openid, storeid,deviceid);
//		ListOperations<String, String> listOperations =  shakeHistoryRedisTemplate.opsForList();
//		List<String> list = listOperations.range(key, 0, listOperations.size(key));
//		if(!DateUtils.judgeShakeTime(startTime.longValue(), endTime.longValue())){
//			//不在摇一摇活动时间范围内，跳转广告分发 
//			return shakeAdvert(openid, storeid,list,device);
//		}
//		
//		List<DevicePage> pages = devicePageService.getActivePageByDeviceId(Integer.valueOf(deviceid));
//		//判断普通摇一摇次数是不是使用完
//		//当天已经摇了的次数
//		ValueOperations<String, String> valueOperations = shakeHistoryRedisTemplate.opsForValue();
//		String timeKey = getShakeCountKey(openid, storeid, deviceid);
//		long times = valueOperations.get(timeKey) == null ? 0l : Long.valueOf(valueOperations.get(timeKey));
//		Map<String,List<DevicePage>> data = parseDevicePage(pages, Integer.parseInt(storeid));
//		List<DevicePage> shopPages = data.get("shop_pages");
//		List<DevicePage> adminPages = data.get("admin_pages");
//		Random random = new Random();
//		int pagesid = Integer.valueOf(storeid);
//		if(device.getShakeLimit() <= times || shopPages == null || shopPages.isEmpty()){
//			//走交叉分发流程
//			if(adminPages==null||adminPages.isEmpty()){
//				//没有启用的交叉分发内容，走广告分发
//				return shakeAdvert(openid, storeid,list,device);
//			}
//			for(DevicePage page : adminPages){
//				if(!list.contains(Dispatcher.historyTemp(Dispatcher.TYPE.ACCOSS.getValue(), page.getPageTemplate()))){
//					if(page.getPageStore()==null||page.getPageStore()<=0){
//						page.setPageStore(pagesid);
//					}
//					//分发当前DevicePage
//					if(page.getPageType()==1){
//						return new Dispatcher(page.getPageTemplate(),Dispatcher.TYPE.CUSTOM_ACCOSS.getValue(),page.getPageStore());
//					}else{
//						return new Dispatcher(page.getPageTemplate(),Dispatcher.TYPE.ACCOSS.getValue(),page.getPageStore());
//					}
//				}
//			}
//			//如果交叉分发发完就走广告投放
//			return shakeAdvert(openid, storeid,list,device);
//		}
//		
//		for(DevicePage page : shopPages){
//			if(!list.contains(Dispatcher.historyTemp(Dispatcher.TYPE.NORMAL.getValue(), page.getPageTemplate()))) {
//				//分发当前DevicePage
//				if(page.getPageType()==1) { 
//					return new Dispatcher(page.getPageTemplate(),Dispatcher.TYPE.CUSTOM_NORMAL.getValue(),pagesid);
//				} else {
//					return new Dispatcher(page.getPageTemplate(),Dispatcher.TYPE.NORMAL.getValue(),pagesid);
//				}
//			}
//		}
//		int n = random.nextInt(shopPages.size());
//		DevicePage page = shopPages.get(n);
//		if(page.getPageType()==1) {
//			return new Dispatcher(page.getPageTemplate(),Dispatcher.TYPE.CUSTOM_NORMAL.getValue(),pagesid);
//		} else {
//			return new Dispatcher(page.getPageTemplate(),Dispatcher.TYPE.NORMAL.getValue(),pagesid);
//		}
//	}
//	
//	/**
//	 * @return the deviceService
//	 */
//	public DeviceService getDeviceService() {
//		return deviceService;
//	}
//	
//	/**
//	 * @param deviceService the deviceService to set
//	 */
//	public void setDeviceService(DeviceService deviceService) {
//		this.deviceService = deviceService;
//	}
//	
//	/**
//	 * 根据当前商铺查出可以分发的商铺ID
//	 * @param storeid
//	 * @return
//	 */
//	public Map<String,List<StoreAdvert>> getAdvertStores(String storeid, String openid) {
//		ValueOperations<String, String> valueOperations = shakeHistoryRedisTemplate.opsForValue();
//		List<Store> stores = storeService.queryAdvertStores(storeid);
//		List<String> sids = new ArrayList<String>();
//		List<String> storeAdvs = new ArrayList<String>();
//		List<String> classAdvs = new ArrayList<String>();
//		if(stores!=null){
//			for(Store store : stores){
//				String targetSid = String.valueOf(store.getStoreId());
//				sids.add(targetSid);
//				long startTime = 0l,endTime=0l;
//				if(store.getAdvertTimeStart()!=null){
//					startTime = store.getAdvertTimeStart().longValue();
//				}
//				if(store.getAdvertTimeEnd()!=null){
//					endTime = store.getAdvertTimeEnd().longValue();
//				}
//				boolean flag = DateUtils.judgeShakeTime(startTime, endTime);
//				if(!flag){
//					continue;
//				}
//				
//				String skey = getAdvertStoreTimeKey(storeid,targetSid);
//				int stimes = valueOperations.get(skey)==null?0:Integer.valueOf(valueOperations.get(skey));
//				//记得加缓存，缓存商铺每日摇一摇分发次数
//				if(store.getAdvertLimit()!=null&&store.getAdvertLimit()<stimes){
//					continue;
//				}
//				if(store.getAdvertType()==1){
//					storeAdvs.add(targetSid);
//				}else{
//					classAdvs.add(targetSid);
//				}
//			}
//		}
//		
//		List<StoreAdvert> allStoreAdverts = new ArrayList<>();
//		List<StoreAdvert> allClassAdverts = new ArrayList<>();
//		if(sids!=null&&!sids.isEmpty()){
//			List<StoreAdvert> storeAdverts = storeAdvertService.getAdvertsBySids(sids);
//			if(storeAdverts!=null){
//				for(StoreAdvert storeAdvert:storeAdverts){
//					if(storeAdvert.getAdvType()==1){
//						if(storeAdvert.getCouponIds()!=null){
//							List<Coupon> coupons = checkCoupon(storeAdvert.getCouponIds().split("/"));
//							if(coupons!=null&&!coupons.isEmpty()){
//								String ids = ArrayUtils.join(coupons, ",", new Callback<Coupon>(){
//									@Override
//									public String load(Coupon t) {
//										return String.valueOf(t.getCouponId());
//									}
//									});
//								storeAdvert.setCouponIds(ids);
//							}else{
//								continue;
//							}
//						}else{
//							continue;
//						}
//					}
//					if(storeAdvs.contains(String.valueOf(storeAdvert.getStoreId()))){
//						allStoreAdverts.add(storeAdvert);
//					}
//					if(classAdvs.contains(String.valueOf(storeAdvert.getStoreId()))){
//						allClassAdverts.add(storeAdvert);
//					}
//				}
//			}
//		}
//		Map<String,List<StoreAdvert>> map = new HashMap<>();
//		map.put("allStoreAdverts", allStoreAdverts);
//		map.put("allClassAdverts", allClassAdverts);
//		return map;
//	}
//	
//	/**
//	 * 更新用户摇自家次数
//	 * @param sid
//	 * @param did
//	 * @param openid
//	 */
//	public void updateShakeCount(String sid,String did,String openid) throws DatabaseExecuteFailedException {
//		DeviceShakeCount d = new DeviceShakeCount();
//		d.setDeviceId(Integer.valueOf(did));
//		d.setDeviceStore(Integer.valueOf(sid));
//		d.setOpenId(openid);
//		d.setShakeTime((int)DateUtils.phpdate());
//		DeviceShakeCount deviceShakeCount = deviceShakeCountService.selectByOpenIdAndTime(d);
//		if(deviceShakeCount==null) {
//			d.setShakeCount(1);
//			deviceShakeCountService.insert(d);
//		} else {
//			deviceShakeCountService.updateCount(deviceShakeCount.getCountId());
//		}
//	}
//	
//	/**
//	 * 更新商铺剩余分发次数
//	 * @param sid 商铺ID
//	 * @param residueNum 商铺原剩余分发次数
//	 */
//	public void updateStoreAdvertNum(String sid,int residueNum) throws DatabaseExecuteFailedException {
//		Store store = new Store();
//		store.setStoreId(Integer.valueOf(sid));
//		store.setAdvertNumResidue(residueNum - 1);
//		storeService.updateByStoreId(store);
//	}
	
	/**
	 * 缓存摇一摇模板历史记录
	 * @param sid
	 * @param did
	 * @param openid
	 * @param action
	 * @param type
	 */
	public void cacheHistory(String sid,String did,String openid,String action, int type,String targetSid){
		String key = this.getShakeKey(openid, sid,did);
		String value = Dispatcher.historyTemp(type, action);
		ListOperations<String, String> listOperations =  shakeHistoryRedisTemplate.opsForList();
		ValueOperations<String, String> valueOperations = shakeHistoryRedisTemplate.opsForValue();
		List<String> list = listOperations.range(key, 0, listOperations.size(key));
		if(!list.contains(value)){
			listOperations.rightPush(key, value);
		}
		long time = DateUtils.distanceNextDay(TimeUnit.MINUTES);
		shakeHistoryRedisTemplate.expire(key,time , TimeUnit.MINUTES);
		//普通摇一摇缓存摇到的次数
		if(type==1){
			String timeKey = getShakeCountKey(openid, sid, did);
			valueOperations.increment(timeKey, 1l);
			shakeHistoryRedisTemplate.expire(timeKey,time , TimeUnit.MINUTES);
		}
		
		//广告投放,用户在此设备的摇一摇次数和用户在广告分发商铺摇一摇的次数
		if(type == 3) {
			String dtimekey = getAdvertDeviceTimeKey(did, openid);
			String stimekey = getAdvertStoreTimeKey(targetSid,openid);
			valueOperations.increment(stimekey, 1);
			valueOperations.increment(dtimekey, 1);
			shakeHistoryRedisTemplate.expire(dtimekey,time , TimeUnit.MINUTES);
			shakeHistoryRedisTemplate.expire(stimekey,time , TimeUnit.MINUTES);
		}
		
	}
	
//	public void saveShakeInfo(DeviceShakeInfo deviceShakeInfo) throws DatabaseExecuteFailedException{
//		deviceShakeInfoService.insertSelective(deviceShakeInfo);
//	}
//	/**
//	 * 获取优惠券
//	 * @param openid
//	 * @param storeid
//	 * @param deviceid
//	 * @param type
//	 * @param pagestore
//	 * @return
//	 */
//	public Coupon getCoupon(String openid,String storeid, String deviceid, String type,String pagestore) {
//		String[] cids = null;
//		ValueOperations<String, String> valueOperations = shakeHistoryRedisTemplate.opsForValue();
//		
//		if("3".equals(type)){
//			//广告投放
//			StoreAdvert storeAdvert = storeAdvertService.getPageBySidAndTemp(pagestore,1);
//			if(storeAdvert!=null&&StringUtils.isNotBlank(storeAdvert.getCouponIds())){
//				cids = storeAdvert.getCouponIds().split("/");
//			}
//		} else if("2".equals(type)) {
//			//交叉分发
//			int pageIsAdmin = 1;
//			DevicePage devicePage = devicePageService.getPageByDidAndTemp(deviceid,1,pageIsAdmin);
//			if(devicePage!=null&&StringUtils.isNotBlank(devicePage.getPageCoupon())){
//				cids = devicePage.getPageCoupon().split("/");
//			}
//		} else {
//			//普通摇一摇
//			int pageIsAdmin = 2;
//			DevicePage devicePage = devicePageService.getPageByDidAndTemp(deviceid,1,pageIsAdmin);
//			if(devicePage!=null&&StringUtils.isNotBlank(devicePage.getPageCoupon())){
//				cids = devicePage.getPageCoupon().split("/");
//			}
//		}
//		if(cids!=null) {
//			String key = "coupon_history_"+openid;
//			String couponId =  valueOperations.get(key);
//			long time = DateUtils.phptime();
//			
//			if(StringUtils.isBlank(couponId)) {
//				couponId = "0";
//			}
//			
//			//查询非历史记录中的那一张优惠券
//			Coupon coupon = couponService.selectActiveCoupon(couponId, cids, time);
//			if(coupon==null) {
//				//查询历史记录中的那一张优惠券
//				coupon = couponService.selectActiveCoupon(couponId, time);
//			}
//			if(coupon!=null) {
//				valueOperations.set(key, String.valueOf(coupon.getCouponId()),
//						DateUtils.distanceNextDay(TimeUnit.MINUTES), TimeUnit.MINUTES);
//			}
//			return coupon;
//		}
//		return null;
//	}
//	/**
//	 * 获取红包
//	 * @param openid
//	 * @param storeid
//	 * @param deviceid
//	 * @param pagestore
//	 * @param type
//	 * @return
//	 */
//	public Redpack getRedpacket(String openid, String storeid, String deviceid, String pagestore, String type) {
//		String[] cids = null;
//		ValueOperations<String, String> valueOperations = shakeHistoryRedisTemplate.opsForValue();
//		
//		if("3".equals(type)){
//			//广告投放
//			StoreAdvert storeAdvert = storeAdvertService.getPageBySidAndTemp(pagestore,4);
//			if(storeAdvert!=null&&StringUtils.isNotBlank(storeAdvert.getRedpackId())){
//				cids = storeAdvert.getRedpackId().split("/");
//			}
//		} else if("2".equals(type)) {
//			//交叉分发
//			int pageIsAdmin = 1;
//			DevicePage devicePage = devicePageService.getPageByDidAndTemp(deviceid,4,pageIsAdmin);
//			if(devicePage!=null&&StringUtils.isNotBlank(devicePage.getPageRedpack())){
//				cids = devicePage.getPageRedpack().split("/");
//			}
//		} else {
//			//普通摇一摇
//			int pageIsAdmin = 2;
//			DevicePage devicePage = devicePageService.getPageByDidAndTemp(deviceid,4,pageIsAdmin);
//			if(devicePage!=null&&StringUtils.isNotBlank(devicePage.getPageRedpack())){
//				cids = devicePage.getPageRedpack().split("/");
//			}
//		}
//		if(cids!=null) {
//			String key = "redpackage_history_"+openid;
//			String redid =  valueOperations.get(key);
//			long time = DateUtils.phptime();
//			
//			if(StringUtils.isBlank(redid)) {
//				redid = "0";
//			}
//			
////			查询非历史记录中的那一张优惠券
//			
//			Redpack redpack = redpackService.selectActiveRedpack(redid, cids, time);
//			if(redpack==null) {
//				//查询历史记录中的那一张优惠券
//				redpack = redpackService.selectActiveRedpack(redid, time);
//			}
//			if(redpack!=null) {
//				valueOperations.set(key, String.valueOf(redpack.getId()),
//						DateUtils.distanceNextDay(TimeUnit.MINUTES), TimeUnit.MINUTES);
//			}
//			return redpack;
//		}
//		return null;
//	}
	
}
