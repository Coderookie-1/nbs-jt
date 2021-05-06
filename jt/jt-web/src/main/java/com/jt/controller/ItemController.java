package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	/**
	 * 根据商品id查讯后台服务器数据
	 * 业务步骤
	 * 1.在前台service中实现httpClient调用
	 * 2.后台根据itemId查询数据库返回对象的json串
	 * 3.前台将json转化为item对象
	 * 4.将item对象保存到request域中
	 * 5.返回页面逻辑名称
	 */
	@RequestMapping("items/{itemId}")
	public String findItemById(@PathVariable Long itemId,Model model) {
		//根据商品id查询商品基本信息（item.jsp中item对象的属性）
		Item item = itemService.findItemById(itemId);
		//根据商品id查询商品详情信息（item.jsp中itemDesc的属性）
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		//将item对象存入request域中
		model.addAttribute("item",item);
		//将itemDesc对象存入request域中
		model.addAttribute("itemDesc",itemDesc);
		//返回item对象到页面
		return "item";
	}
}
