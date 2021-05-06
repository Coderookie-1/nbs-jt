package com.jt.config;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;


//表示Redis的配置类
@Configuration//XML文件
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	@Value("${redis.nodes}")
	private String redisNodes;
	@Bean
	public JedisCluster jedisCluster() {
		//1.按照，号拆分 ：号拆分 获取IP和端口
		Set<HostAndPort> nodes = new HashSet<>();
		//1.1按照，号拆分为多个node
		String[] strnode = redisNodes.split(",");
		for (String node : strnode) {
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			HostAndPort hostAndPort = new HostAndPort(host, port);
			nodes.add(hostAndPort);
		}
		//将集合存进来
		return new JedisCluster(nodes);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * @Value("${redis.sentinels}") private String jedisSentinelNodes;
	 * 
	 * @Value("${redis.sentinel.masterName}") private String masterName;
	 * //通过哨兵机制实现redis操作
	 * 
	 * @Bean public JedisSentinelPool jedisSentinelPoll() { Set<String> sentinels =
	 * new HashSet<>(); sentinels.add(jedisSentinelNodes); return new
	 * JedisSentinelPool(masterName, sentinels); }
	 */
	
	
	/*
	 * //表示从spring容器中可以自动取值
	 * 
	 * @Value("${jedis.host}") private String host;
	 * 
	 * @Value("${jedis.port}") private Integer port; //交给spring容器管理
	 * 
	 * @Bean public Jedis jedis() { return new Jedis(host,port); }
	 */
	
	/*
	 * @Value("${redis.nodes}")
	 * private String redisNodes;
	 * @Bean() public ShardedJedis shardedJedis() { List<JedisShardInfo> shards =
	 * new ArrayList<>(); String[] nodes = redisNodes.split(","); for (String node :
	 * nodes) { String host = node.split(":")[0]; int port =
	 * Integer.parseInt(node.split(":")[1]); JedisShardInfo info = new
	 * JedisShardInfo(host,port); shards.add(info); } return new
	 * ShardedJedis(shards); }
	 */
}
