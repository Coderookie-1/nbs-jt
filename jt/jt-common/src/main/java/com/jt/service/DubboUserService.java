package com.jt.service;

import com.jt.pojo.User;

//定义中立的接口
public interface DubboUserService {
	//Dubbo接口实现用户新增
	void saveUser(User user);

}
