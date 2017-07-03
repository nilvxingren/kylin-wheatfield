package com.rkylin.wheatfield.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class SpringBeanUtils {
	private static ApplicationContext applicationContext = null;

	public static void setApplicationContext(ApplicationContext applicationContext) {
		SpringBeanUtils.applicationContext = applicationContext;
	}

	/**
	 * 获取对象
	 * 
	 * @param id 
	 * @return Object 一个以所给名字注册的bean的实例
	 * @throws BeansException
	 */
	public static Object getBean(String id) {
		return applicationContext.getBean(id);
	}
}
