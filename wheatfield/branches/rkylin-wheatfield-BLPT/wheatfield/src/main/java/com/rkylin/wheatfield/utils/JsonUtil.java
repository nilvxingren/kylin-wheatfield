package com.rkylin.wheatfield.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author zhouqingda
 * 
 */
public class JsonUtil {
	public static String bean2Json(Object bean) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(bean);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 将Json对象转换成Map
	 * 
	 * @param jsonObject
	 *            json对象
	 * @return Map对象
	 * @throws JSONException
	 */
	public static Map<String, String> json2Map(String jsonString) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			Iterator<?> iterator = jsonObject.keys();
			String key = null;
			String value = null;
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				value = jsonObject.getString(key);
				result.put(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
}
