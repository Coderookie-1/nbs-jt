package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVO;

@Service
//加载image.properties文件的内容
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService{
	//定义本地磁盘路径
	@Value("${image.localDirPath}")//利用配置文件动态的为属性赋值
	private String localDirPath;
	//定义虚拟路径名称
	@Value("${image.urlPath}")
	private String urlPath;
	/**
	 * 文件上传思路：
	 * 1.获取图片名称
	 * 2.校验是否为图片类型 jpg|png|gif
	 * 3.校验是否为恶意程序
	 * 4.准备文件夹   分文件保存   按照时间存储 yyyy/MM/dd
	 * 5.防止文件重名 UUID来实现 32位16进制数+毫秒数 重复的几率低，约等于不重复
	 */
	/**
	 * 1.^  标识..开始
	 * 2.$  标识..结束
	 * 3.点.  任意单个字符
	 * 4.*   任意0-无穷个
	 * 5.+   表示1-无穷个
	 * 6.\.  标示特殊字符 点
	 * 7.()  代表分组 满足其中一个条件即可
	 */
	@Override
	public ImageVO updateFile(MultipartFile uploadFile) {
		ImageVO imageVO = new ImageVO();
		//1.获取图片名称
		String fileName = uploadFile.getOriginalFilename();
		//将字符同意转化为小写
		fileName = fileName.toLowerCase();
		//2.校验是否为图片类型 jpg|png|gif
		//2.1使用正则表达式判断字符串
		if(!fileName.matches("^.+\\.(jpg|png|gif)$")) {
			imageVO.setError(1);//表示上传有误
			return imageVO;
		}
		//3.校验是否为恶意程序  图片模板
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			if(width==0 || height == 0) {
				imageVO.setError(1);
				return imageVO;
			}
			//4.  按照时间存储 yyyy/MM/dd
			//4.1时间转换为字符串
			String dateDir = new SimpleDateFormat("yyyy/MM//dd").format(new Date());
			//5.准备文件夹   分文件保存 
			String localDir = localDirPath + dateDir;
			File dirFile = new File(localDir);
			if(!dirFile.exists()) {
				//如果文件不存在，则创建文件夹
				dirFile.mkdirs();
			}
			//6.防止文件重名 UUID来实现 32位16进制数+毫秒数 重复的几率低，约等于不重复
			//6.1使用UUID定义文件的名称
			String uuid = UUID.randomUUID().toString().replace("-","");
			//图片的类型   动态获取
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			//拼接新的文件的名称
			String realLocalPath = localDir + "/" + uuid + fileType;
			//7.完成文件的上传
			uploadFile.transferTo(new File(realLocalPath));
			//拼接url路径
			String realUrlPath = urlPath + dateDir + "/" + uuid + fileType;
			//将文件信息回传给页面
			imageVO.setError(0).setHeight(height).setWidth(width).setUrl(realUrlPath);
			//url地址暂时写死
		} catch (Exception e) {
			e.printStackTrace();
			imageVO.setError(1);
			return imageVO;
		}
		return imageVO;
	}

}
