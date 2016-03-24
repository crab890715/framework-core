/**  
 * @Title: UserService.java
 * @Prject: InternalService
 * @Package: io.springcat.internal.service.impl
 * @Description: TODO
 * @author: adampeng  
 * @date: 2015年2月26日 下午2:35:32
 * @version: V1.0  
 */
package com.framework.core.service.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.framework.core.api.base.AbstractService;
import com.framework.core.api.base.BaseDao;
import com.framework.core.api.model.WechatTemp;
import com.framework.core.api.service.WechatTempService;
import com.framework.core.service.dao.IWechatTempDao;

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
