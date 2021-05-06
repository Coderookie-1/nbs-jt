package com.jt;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.util.ObjectMapperUtil;

import lombok.Data;
//研究转换关系
@Data
//当程序转换对象时如果遇到位置属性则忽略
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestJson {
	private Integer id;
	private String name;
	private Integer age;
	private String sex;
	private Integer getIds() {
		return id;
	}

	/*@JsonIgnoreProperties(ignoreUnknown = true)
	 * 该注解相当于set方法
	 * 	private void setIds() { this.id = id; }
	 */
	@Test
	public void userToJSON() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper);
		TestJson user = new TestJson();
		user.setId(1000);
		user.setName("测试数据");
		user.setAge(18);
		user.setSex("男");
		String json = mapper.writeValueAsString(user);
		System.out.println(json);
	}
	/**
	 * 1.获取userJson串
	 * 2.通过jsn串获取json中的key
	 * 2.根据class类型的反射机制实例化对象
	 * 3.根据key调用setkey方法为属性赋值
	 * 4.最终生成对象
	 * 5.可以利用
	 * 		@JsonIgnoreProperties(ignoreUnknown = true)
	 * 		注解忽略位置属性
	 * @throws IOException
	 */
	@Test
	public void jsonToUser() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		TestJson user = new TestJson();
		user.setId(1000);
		user.setName("测试数据");
		user.setAge(18);
		user.setSex("男");
		String json = mapper.writeValueAsString(user);
		System.out.println(json);
		//以下方法实现了数据的转化
		TestJson user2 = ObjectMapperUtil.toObject(json, user.getClass());
		System.out.println(user2);
		
		
	}
}