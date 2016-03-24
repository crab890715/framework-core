
-- ----------------------------
-- Table structure for `wechat_temp`
-- ----------------------------
DROP TABLE IF EXISTS `wechat_temp`;
CREATE TABLE `wechat_temp` (
  `temp_id` int(11) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(100) NOT NULL COMMENT '微信唯一标识',
  `nickname` varchar(500) DEFAULT NULL COMMENT '微信用户昵称',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `headimgurl` text COMMENT '会员微信头像',
  `sex` tinyint(1) DEFAULT '2' COMMENT '微信用户性别 1时是男性，值为2时是女性，值为0时是未知 ',
  `time` int(11) DEFAULT '0' COMMENT '添加时间',
  `lon` varchar(40) DEFAULT NULL COMMENT '用户所在经度',
  `lat` varchar(40) DEFAULT NULL COMMENT '用户所在纬度',
  PRIMARY KEY (`temp_id`),
  KEY `open_id` (`open_id`) USING BTREE,
  KEY `open_id_2` (`open_id`) USING BTREE,
  KEY `open_id_3` (`open_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=113386 DEFAULT CHARSET=utf8 COMMENT='用户微信信息临时表';
