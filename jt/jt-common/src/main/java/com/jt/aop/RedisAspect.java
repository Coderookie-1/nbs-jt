package com.jt.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;



//redis的切面
@Component//将对象交给spring容器管理
@Aspect//标识切面
public class RedisAspect {
	@Autowired(required = false)
	private JedisCluster jedis;
	//注入redis哨兵工具API
	//@Autowired(required = false)//调用时才调用
	//private RedisService jedis;
	//引入哨兵机制  1000个链接最优
	/*
	 * @Autowired(required = false) private JedisSentinelPool pool;
	 */
	
	//容器初始化时不需要实例化对象，只有用户使用时才初始化
	//一般工具类中添加该注解
	//@Autowired(required = false)
	//private Jedis jedis;
	//private ShardedJedis jedis;
	//拦截注解
	//execution(返回值类型 包名.类名.方法名(int,string))
	//使用该方法可以直接获取注解的对象
	/**
	 * key:value
	 * @param cache_find
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Around("@annotation(cache_find)")
	public Object around(ProceedingJoinPoint joinPoint,Cache_Find cache_find) {
		//1.获取key的值
		String key = getkey(joinPoint,cache_find);
		//2.根据key查询缓存
		String result = jedis.get(key);
		Object data = null;
		try {
			if(StringUtils.isEmpty(result)) {
				//如果结果为空（null），表示缓存中没有数据
				//查询数据库
				data = joinPoint.proceed();//表示业务方法执行
				//将数据转化为json串
				String json = ObjectMapperUtil.toJSON(data);
				//将json串存入缓存
				//判断用户是否设定超时时间
				if(cache_find.secondes()==0) 
					//表示不要超时
					jedis.set(key, json);
				else
					//表示超时
					jedis.setex(key, cache_find.secondes(), json);
				System.out.println("第一次查询数据库");
			}else {
				Class targetClass = getClass(joinPoint);
				//如果缓存中有数据则将json串转换为对象直接返回
				data = ObjectMapperUtil.toObject(result,targetClass);
				System.out.println("aop（环绕通知）查询缓存");
			}	
		} catch (Throwable e) {
			e.printStackTrace();
			//将异常转化为运行时异常
			throw new RuntimeException();
		}
		return data;
	}
	//获取返回值的类型
	private Class getClass(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getReturnType();
	}
	/**
	 * 1.判断用户key的类型  auto   empty
	 * 2.
	 * @param joinPoint
	 * @param cache_find
	 * @return
	 */
	private String getkey(ProceedingJoinPoint joinPoint, Cache_Find cache_find) {
		//1.获取key的类型
		KEY_ENUM key_ENUM = cache_find.keyType();
		//2.判断key的类型
		if(key_ENUM.equals(key_ENUM.EMPTY)) {
			//标示使用用户自己的key
			return cache_find.key();
		}
		//标示用户的key需要拼接 key+"_"+第一个参数
		//String.valueOf()将输出的东西转化为字符串
		String strArgs = String.valueOf(joinPoint.getArgs()[0]);
		String key = cache_find.key()+"_"+strArgs;
		return key;
	}
}
