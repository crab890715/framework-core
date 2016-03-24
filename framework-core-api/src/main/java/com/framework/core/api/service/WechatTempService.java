/**  
 * @Title: IUserService.java
 * @Prject: GlobalDef
 * @Package: io.springcat.service
 * @Description: TODO
 * @author: adampeng  
 * @date: 2015年2月26日 上午11:22:11
 * @version: V1.0  
 */
package com.framework.core.api.service;

import com.framework.core.api.model.WechatTemp;


/**
 * @ClassName: WechatTempService
 * @Description: TODO
 * @author: adampeng
 * @date: 2015年2月26日 上午11:22:11
 */
public interface WechatTempService {
	
	/**
	 * 
	* @Title: findUser 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param username
	* @param @return    设定文件 
	* @return UserView    返回类型 
	* @throws
	 */
	WechatTemp findByOpenId(String openid);

	WechatTemp insert(WechatTemp temp);
}
