package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

//该类是Dubbo的实现类
@Service(timeout = 3000)
public class DubboUserServiceImpl implements DubboUserService{
	@Autowired
	private UserMapper userMapper;
	@Transactional//添加事物控制
	@Override
	public void saveUser(User user) {
		//1.将密码加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass)
			.setEmail(user.getPhone())
			.setCreated(new Date())
			.setUpdated(user.getCreated());
		//2.补齐入库数据 email用Phone代替
		userMapper.insert(user);
	}
}
