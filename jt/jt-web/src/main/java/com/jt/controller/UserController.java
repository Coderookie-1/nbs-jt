package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

//实现用户登陆
@Controller
@RequestMapping("/user")
public class UserController {
	/*
	 * @Autowired private UserService userService;
	 */
	//导入dubbo的用户接口
	@Reference(timeout = 3000,check = false)
	private DubboUserService userService;
	
	@RequestMapping("/{ModelName}")
	public String index(@PathVariable String ModelName) {
		
		return ModelName;
	}
	@RequestMapping("/doRegister")
	@ResponseBody //
	public SysResult saveUser(User user) {
		try {
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
}
