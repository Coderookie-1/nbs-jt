package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;

@Controller//因为要跳转页面故用Controller注解
//RestControlle注解不能实现页面的跳转
public class FileController {
	@Autowired
	private FileService fileService;
	/* 当用户上传完成时重定向到上传页面 
	 * 思路：
	 * 1.获取用户文件信息 包含文件名称
	 * 2.指定文件上传的路径 判断该文件是否存在
	 * 3.实现文件的上传
	 * */
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws IllegalStateException, IOException {
		//1.获取input标签中的name属性
		String inputName = fileImage.getName();
		System.out.println("1:"+inputName);
		//2.获取文件名称
		String filePath = fileImage.getOriginalFilename();
		String fileName = filePath.substring(8);//从第9位开始是文件的名称，前面都是文件的路径
		//3.定义文件夹路径
		File fileDir = new File("D:/1-jt/image");
		if(!fileDir.exists()) {
			//创建文件夹
			fileDir.mkdirs();
		}
		//4.实现文件上传
		fileImage.transferTo(new File("D:/1-jt/image/"+fileName));
		return "redirect:/file.jsp";
	}
	//实现文件上传
	@RequestMapping("/pic/upload")
	@ResponseBody//返回JSON串
	public ImageVO uploadFile(MultipartFile uploadFile) {
		
		return fileService.updateFile(uploadFile);
	}
}
