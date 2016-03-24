package com.beacon.wechat.service.dao;

import org.apache.ibatis.annotations.Param;

import com.beacon.wechat.api.base.BaseDao;
import com.beacon.wechat.api.model.WechatTemp;

@MybatisRepository
public interface IWechatTempDao extends BaseDao<WechatTemp,Long>{
	WechatTemp findByOpenId(@Param("openid") String openid);
}
