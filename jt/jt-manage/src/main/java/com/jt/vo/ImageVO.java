package com.jt.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data//该注解相当于get和set方法
@Accessors(chain = true)//链式加载
@NoArgsConstructor//无参构造
@AllArgsConstructor//有参构造
public class ImageVO implements Serializable{
	private Integer error;//0表示成功，1表示失败
	private String url;
	private Integer width;
	private Integer height;
}
