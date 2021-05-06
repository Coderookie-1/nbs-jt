package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
/**
 * 操作哨兵的API案例
 * @author xxb
 *
 */
public class TestSentinel {
	//测试哨兵的get/set操作
	@Test
	public void test01() {
		//masterName 代表主机变量名称
		//sentinels  表示哨兵  Set<String> IP:端口
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.206.129:26379");
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = sentinelPool.getResource();
		jedis.set("cc", "考研快结束了");
		System.out.println(jedis.get("cc"));
		jedis.close();//关闭链接
	}
}
