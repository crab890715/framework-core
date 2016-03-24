package com.framework.core.service.dao;

import org.apache.ibatis.annotations.Param;

import com.framework.core.api.base.BaseDao;
import com.framework.core.api.model.WechatTemp;

@MybatisRepository
public interface IWechatTempDao extends BaseDao<WechatTemp,Long>{
	WechatTemp findByOpenId(@Param("openid") String openid);
}
