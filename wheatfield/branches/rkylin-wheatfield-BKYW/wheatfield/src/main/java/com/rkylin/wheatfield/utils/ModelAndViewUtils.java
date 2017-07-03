package com.rkylin.wheatfield.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;








import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SpringBeanConstants;
import com.rkylin.wheatfield.utils.xStreamConverter.XStreamCollectionConverter;
import com.rkylin.wheatfield.utils.xStreamConverter.XStreamDateConverter;
import com.rkylin.wheatfield.utils.xStreamConverter.XStreamDoubleConverter;
import com.rkylin.wheatfield.utils.xStreamConverter.XStreamStringConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;

public class ModelAndViewUtils {

	public static ModelAndView getModelAndView(String method, String format,
			Object bean) {
		ModelAndView mav = new ModelAndView();

		// 设定marshallingView
		mav.setView(getMarshallingView(method, format, bean.getClass()));

		// 添加需映射的对象
		mav.addObject(BindingResult.MODEL_KEY_PREFIX, bean);

		return mav;
	}

	/**
	 * 取得marshallingView
	 * 
	 * @param <T>
	 * 
	 * @param method
	 * @param format
	 * @param obj
	 * @return
	 */
	private static <T> MarshallingView getMarshallingView(String method,
			String format, Class<T> type) {

		MarshallingView marshallingView = (MarshallingView) SpringBeanUtils
				.getBean(SpringBeanConstants.MARSHALLING_VIEW);

		// 设定marshaller
		marshallingView
				.setMarshaller(getXStreamMarshaller(method, format, type));

		return marshallingView;
	}

	/**
	 * 取得marshaller
	 * 
	 * @param <T>
	 * 
	 * @param method
	 * @param format
	 * @param obj
	 * @return
	 */
	private static <T> XStreamMarshaller getXStreamMarshaller(String method,
			String format, Class<T> type) {

		XStreamMarshaller xStreamMarshaller = (XStreamMarshaller) SpringBeanUtils
				.getBean(SpringBeanConstants.XSTREAM_MARSHALLER);

		// 设定驱动
		xStreamMarshaller.setStreamDriver(getStreamDriver(format));

		// 如果设定了类别名，则使用设定的类别名
		if (method != null && !"".equals(method)) {
			xStreamMarshaller.getXStream().alias(makeRootName(method), type);
		}

		// 去除class属性
		xStreamMarshaller.getXStream().aliasSystemAttribute(null, "class");

		// 设定日期格式
		XStreamDateConverter dateFormater = (XStreamDateConverter) SpringBeanUtils
				.getBean(SpringBeanConstants.XSTREAM_DATE_CONVERTER);
		xStreamMarshaller.getXStream().registerConverter(dateFormater);

		// 设定double格式（保留2位小数）
		XStreamDoubleConverter doubleFormater = (XStreamDoubleConverter) SpringBeanUtils
				.getBean(SpringBeanConstants.XSTREAM_DOUBLE_CONVERTER);
		xStreamMarshaller.getXStream().registerConverter(doubleFormater);

		// 设定String格式（过滤特殊字符 &#x[0-8B-CE-F];）
		XStreamStringConverter stringFormater = (XStreamStringConverter) SpringBeanUtils
				.getBean(SpringBeanConstants.XSTREAM_STRING_CONVERTER);
		xStreamMarshaller.getXStream().registerConverter(stringFormater);

		// 如果节点对象类型为集合（List、Map、数组等），则为该节点增加属性：list="true"
		xStreamMarshaller.getXStream().registerConverter(
				new XStreamCollectionConverter(xStreamMarshaller.getXStream()
						.getMapper()));

		return xStreamMarshaller;

	}

	/**
	 * 根据数据格式取得相应的映射驱动
	 * 
	 * @param format
	 * @return
	 */
	private static HierarchicalStreamDriver getStreamDriver(String format) {

		HierarchicalStreamDriver streamDriver = null;

		// jsonDriver
		if (Constants.DATA_PROTOCOL_TYPE_JSON.equals(format)) {
			streamDriver = (HierarchicalStreamDriver) SpringBeanUtils
					.getBean(SpringBeanConstants.XSTREAM_JSON_DRIVER);
		}
		// xmlDriver(默认)
		else {
			streamDriver = (HierarchicalStreamDriver) SpringBeanUtils
					.getBean(SpringBeanConstants.XSTREAM_XML_DRIVER);
		}

		return streamDriver;
	}

	/**
	 * 根据调用方法名生成根节点名
	 * 
	 * @param methodName
	 *            方法名
	 * @return
	 */
	public static String makeRootName(String methodName) {

		StringBuffer makerBuffer = new StringBuffer();

		// 去除开头的"ruixue."，并将分隔符"."替换为"_"
		makerBuffer.append(methodName.replaceFirst("ruixue.", "").replace('.',
				'_'));
		// 添加"_response"后缀
		makerBuffer.append("_response");

		return makerBuffer.toString();
	}

	public static List<String> getCollectionFields(String path, Object obj)
			throws Exception {
		if (null == obj) {
			return new ArrayList<String>();
		}
		Class<?> clazz = obj.getClass();
		if (isPrimitive(clazz)) {
			return new ArrayList<String>();
		}
		List<String> propertyPathList = new ArrayList<String>();
		if (null != path) {
			propertyPathList.add(path);
		}
		List<String> collectionFields = new ArrayList<String>();
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			Class<?> type = field.getType();
			field.setAccessible(true);
			Object value = field.get(obj);
			if (isPrimitive(type)) {
				continue;
			} else if (Collection.class.isAssignableFrom(type)) {
				if (Map.class.isAssignableFrom(type)) {
					// 不支持
				} else {
					String fieldName = getFidldName(field);
					propertyPathList.add(fieldName);
					collectionFields.add(org.apache.commons.lang.StringUtils
							.join(propertyPathList.iterator(), "."));
					propertyPathList.clear();
					if (null != value) {
						Collection<?> list = (Collection<?>) value;
						if (list.size() > 0) {
							collectionFields
									.addAll(getCollectionFields(
											null == path ? fieldName : path
													+ "." + fieldName, list
													.iterator().next()));
						}
					}
				}
			} else if (type.isArray()) {
				// 不支持
			} else {
				String fieldName = getFidldName(field);
				collectionFields.addAll(getCollectionFields(
						null == path ? fieldName : path + "." + fieldName,
						value));
			}
		}
		return collectionFields;
	}

	private static boolean isPrimitive(Class<?> type) {
		if (type.isPrimitive() || String.class.isAssignableFrom(type)
				|| Boolean.class.isAssignableFrom(type)
				|| Character.class.isAssignableFrom(type)
				|| Byte.class.isAssignableFrom(type)
				|| Short.class.isAssignableFrom(type)
				|| Integer.class.isAssignableFrom(type)
				|| Long.class.isAssignableFrom(type)
				|| Float.class.isAssignableFrom(type)
				|| Double.class.isAssignableFrom(type)
				|| Number.class.isAssignableFrom(type)
				|| Date.class.isAssignableFrom(type)
				|| Calendar.class.isAssignableFrom(type)) {
			return true;
		} else {
			return false;
		}
	}

	private static String getFidldName(Field field) {
		String fieldName = null;
		XStreamAlias fieldAnnotation = field.getAnnotation(XStreamAlias.class);
		if (null == fieldAnnotation) {
			fieldName = field.getName();
		} else {
			fieldName = fieldAnnotation.value();
			if (org.apache.commons.lang.StringUtils.isBlank(fieldName)) {
				fieldName = field.getName();
			}
		}
		return fieldName;
	}
}