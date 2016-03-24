package com.beacon.wechat.app.comm.cache;

import redis.clients.jedis.JedisPubSub;

public class WeixinJedisPubSub extends JedisPubSub{

	@Override
	public void onMessage(String channel, String message) {
		System.err.println(message);
	}

}
