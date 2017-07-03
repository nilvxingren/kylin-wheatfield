package com.rkylin.wheatfield.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import com.google.common.collect.Maps;
import com.rkylin.wheatfield.domain.M000003OpenEntityAccountBean;

public class BeanUtil {


	public static <T> Object  maptobean(Class<? extends Object> cls,Map<String,String[]> m ){
		Collection<String>  c = new ArrayList<String>();
		Field[] declaredFields = cls.getDeclaredFields();
		for (Field field : declaredFields) {
			c.add(field.getName().toLowerCase());
		}
		Map<String,Object> _m = Maps.newHashMap();
		for (Entry<String, String[]> entry : m.entrySet()) {
			   if(c.contains(entry.getKey())){
				   String[] value = entry.getValue();
				   if(value!=null && value.length!=0){
					   String v = value[0];
					   _m.put(entry.getKey(), v);
				   }
			   }
		}
		Object convertMap2Bean = convertMap2Bean(cls, _m);
		
		
		return convertMap2Bean;
	}
	
	public static final <T> T convertMap2Bean(Class<T> clazz, Map<String, Object> map){
		T t = null;
		try {
			t = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
			e.printStackTrace();
			return null;
		}
		
		PropertyDescriptor[] targetPds = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if(writeMethod != null && map.containsKey(targetPd.getName().toLowerCase())){
				try {
					if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
						writeMethod.setAccessible(true);
					}
					writeMethod.invoke(t, map.get(targetPd.getName().toLowerCase()));
				} catch (Throwable ex) {
					throw new FatalBeanException("Could not write property '" + targetPd.getName() + "' from bean", ex);
				}
			}
		}
		
		return t;
	}
	
	/**
	 * 内省 遍历原bean 取出非null的
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T beanDelNullAndEmpty(T t){
		Object ret = null;
		try {
			ret =  t.getClass().newInstance();
			 BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
			PropertyDescriptor[] targetPds = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor targetPd : targetPds) {
				Method writeMethod = targetPd.getWriteMethod();
				Method readMethod = targetPd.getReadMethod();
				if(writeMethod != null && readMethod!=null){
					
					try {
						if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
							writeMethod.setAccessible(true);
						}
						Object invoke = readMethod.invoke(t, null);
						if(invoke!=null){
							writeMethod.invoke(ret, invoke);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not write property '" + targetPd.getName() + "' from bean", ex);
					}
				}
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (T)ret;
	}
	
	public static void main(String[] args) {
		Map<String,String[]> m = Maps.newHashMap();
		maptobean(M000003OpenEntityAccountBean.class, m);
	}
}
