package com.jt;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestRedis2 {
	//测试hash/list/事务控制
	@Test
	public void testHash() {
		Jedis jedis = new Jedis("192.168.206.129",6379);
		jedis.hset("user1", "id", "200");
		jedis.hset("user1", "name", "tomcat服务器");
		String result = jedis.hget("user1", "name");
		System.out.println(result);
		System.out.println(jedis.hgetAll("user1"));
	}
	//编辑list集合
	@Test
	public void list() {
		Jedis jedis = new Jedis("192.168.206.129",6379);
		jedis.lpush("list", "1","2","3","4","5");
		System.out.println(jedis.rpop("list"));
	}
	//redis事务控制
	@Test
	public void testTx() {
		Jedis jedis = new Jedis("192.168.206.129",6379);
		//开启事务
		Transaction transaction = jedis.multi();
		try {
			transaction.set("ww", "wwww");
			transaction.set("dd", null);
			//事务提交
			transaction.exec();
		} catch (Exception e) {
			//事务回滚
			transaction.discard();
		}
	}
	
	
	
	
}
