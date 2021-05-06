package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//指定作用范围（只对方法有效）
@Retention(RetentionPolicy.RUNTIME)//运行时有效
public @interface Cache_Update {
	String key() default "";
}
