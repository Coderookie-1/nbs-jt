package com.jt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

import redis.clients.jedis.Jedis;

//
public class TestRedis {
	//String类型操作方式 配置文件3处 防火墙
	//IP：端口号
	
	@Test
	public void testString() {
		Jedis jedis = new Jedis("192.168.206.129", 6379);
		jedis.set("1902", "1902班");
		jedis.expire("1902", 100);
		System.out.println(jedis.get("1902"));
	}
	//设定数据超时的方法 2种
	//分布式锁！！！
	@Test
	public void testTimaOut() throws Exception {
		Jedis jedis = new Jedis("192.168.206.129", 6379);
		jedis.setex("a", 2, "aa");
		System.out.println(jedis.get("a"));
		Thread.sleep(3000);
		//当key不存在时操作正常，key存在时操作失败。
		Long result = jedis.setnx("a", "bb");
		System.out.println("获取输出数据："+result+":"+jedis.get("a"));
	}
	//1.实现对象转换为JSON
	@Test
	public void objectToJSON() throws IOException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1000L).setItemDesc("测试数据");
		ObjectMapper mapper = new ObjectMapper();
		//转化JSON时必须get/set方法
		String json = mapper.writeValueAsString(itemDesc);
		System.out.println(json);
		//将json串转化为对象
		ItemDesc desc2 = mapper.readValue(json, ItemDesc.class);
		System.out.println("测试的对象"+desc2);
	}
	//实现List集合与JSON转化
	@Test
	public void listToJson() throws IOException {
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(1000L).setItemDesc("测试数据");
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(1000L).setItemDesc("测试数据");
		List<ItemDesc> list  = new ArrayList<>();
		list.add(itemDesc1);
		list.add(itemDesc2);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("mapper:"+mapper);
		String json = mapper.writeValueAsString(list);
		System.out.println("集合转化为JSON："+json);
		//将数据保存到Redis中
		Jedis jedis = new Jedis("192.168.206.129", 6379);
		jedis.set("itemDescList", json);
		//从redis中获取数据
		//有问题！！！！！！
		  String result = jedis.get("itemDescList"); 
		  List<ItemDesc> descList = mapper.readValue("result", list.getClass());
		  System.out.println("JSON转化为对象"+descList);
		 
	}
	/* 3.利用Redis存储业务数据 数据库 
	 * 数据库数据：对象Object
	 * String类型要求只能存储字符串类型
	 * item  -----> JSON -----> 字符串
	 * */
	@Test
	public void testSetObject() throws Exception {
		Item item = new Item();
		item.setId(1000L).setTitle("测试数据");
		Jedis jedis = new Jedis("192.168.206.129", 6379);
		//将item对象转换为JSON
		//转换JSON时必须要有get/set方法
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(item);
		System.out.println(json);
	}
	
}
