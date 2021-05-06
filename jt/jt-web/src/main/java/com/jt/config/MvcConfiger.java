package com.jt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiger implements WebMvcConfigurer{
	//开启匹配后缀配置
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		//开启后缀类型匹配
		configurer.setUseSuffixPatternMatch(true);
	}
}
