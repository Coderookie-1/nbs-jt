package com.jt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;


//测试分片
public class TestRedisShards {
	/**
	 * 操作时需要将多台redis当作1台使用
	 */
	@Test
	public void testShards() {
		List<JedisShardInfo> shards = new ArrayList<>();
		JedisShardInfo info1 = new JedisShardInfo("192.168.206.129",6379);
		JedisShardInfo info2 = new JedisShardInfo("192.168.206.129",6380);
		JedisShardInfo info3 = new JedisShardInfo("192.168.206.129",6381);
		shards.add(info1);
		shards.add(info2);
		shards.add(info3);
		//操作分片redis对象工具类
		ShardedJedis shardedJedis = new ShardedJedis(shards);
		shardedJedis.set("wwww","2020年12月4号");
		System.out.println(shardedJedis.get("wwww"));
	}
}
