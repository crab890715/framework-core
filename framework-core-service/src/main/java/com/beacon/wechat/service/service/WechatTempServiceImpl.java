/**  
 * @Title: UserService.java
 * @Prject: InternalService
 * @Package: io.springcat.internal.service.impl
 * @Description: TODO
 * @author: adampeng  
 * @date: 2015年2月26日 下午2:35:32
 * @version: V1.0  
 */
package com.beacon.wechat.service.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.beacon.wechat.api.base.AbstractService;
import com.beacon.wechat.api.base.BaseDao;
import com.beacon.wechat.api.model.WechatTemp;
import com.beacon.wechat.api.service.WechatTempService;
import com.beacon.wechat.service.dao.IWechatTempDao;

/**
 * @ClassName: UserService
 * @Description: TODO
 * @author: adampeng
 * @date: 2015年2月26日 下午2:35:32
 */
public class WechatTempServiceImpl extends AbstractService<WechatTemp, Long> implements WechatTempService {
	
	@Autowired
	IWechatTempDao wechatTempDao;
	@Override
	public BaseDao<WechatTemp, Long> getBaseDao() {
		return wechatTempDao;
	}
	@Override
	public WechatTemp findByOpenId(String openid) {
		return wechatTempDao.findByOpenId(openid);
	}

	@Override
	public WechatTemp insert(WechatTemp wechatTemp) {
		wechatTempDao.insert(wechatTemp);	
		return wechatTemp;
	}
}
