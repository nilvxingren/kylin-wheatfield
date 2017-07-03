/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.rkylin.wheatfield.utils;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.oxm.Marshaller;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.response.ErrorResponse;



public class MarshallingView extends AbstractView {
	public static final String DEFAULT_CONTENT_TYPE = "application/xml";
	private Marshaller marshaller;
	private String modelKey;

	public MarshallingView() {
		setContentType("application/xml");
		setExposePathVariables(false);
	}

	public MarshallingView(Marshaller marshaller) {
		Assert.notNull(marshaller, "'marshaller' must not be null");
		setContentType("application/xml");
		this.marshaller = marshaller;
		setExposePathVariables(false);
	}

	public void setMarshaller(Marshaller marshaller) {
		Assert.notNull(marshaller, "'marshaller' must not be null");
		this.marshaller = marshaller;
	}

	public void setModelKey(String modelKey) {
		this.modelKey = modelKey;
	}

	protected void initApplicationContext() throws BeansException {
		Assert.notNull(this.marshaller, "Property 'marshaller' is required");
	}

	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Object toBeMarshalled = locateToBeMarshalled(model);
		if (toBeMarshalled == null) {
			throw new ServletException("Unable to locate object to be marshalled in model: " + model);
		}
		String format = request.getParameter(Constants.SYS_PARAM_FORMAT);
		String method = request.getParameter(Constants.SYS_PARAM_METHOD);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
		marshaller.marshal(toBeMarshalled, new StreamResult(bos));
		String result = "";
		if (Constants.DATA_PROTOCOL_TYPE_JSON.equals(format) && !ErrorResponse.class.isInstance(toBeMarshalled)) {
			if (StringUtils.isBlank(method)) {
				return;
			}
			String rootName = ModelAndViewUtils.makeRootName(method);
			JSONObject jsonObject = new JSONObject(bos.toString(Constants.CHARSET_UTF8));
			List<String> collectionFields = ModelAndViewUtils.getCollectionFields(null, toBeMarshalled);
			if (!collectionFields.isEmpty()) {
				Collections.reverse(collectionFields);
			}
			for (String collectionField : collectionFields) {
				String[] propertyPath = collectionField.split("\\.");
				String property = propertyPath[propertyPath.length - 1];
				JSONObject rootObj = jsonObject.getJSONObject(rootName);
				Map<JSONObject, JSONArray> findReplaceElements = findReplaceElements(rootObj, collectionField);
				Set<Entry<JSONObject, JSONArray>> entrySet = findReplaceElements.entrySet();
				for (Entry<JSONObject, JSONArray> entry : entrySet) {
					replace(entry.getKey(), property, entry.getValue());
				}
			}
			result = jsonObject.toString();
		} else {
			result = bos.toString(Constants.CHARSET_UTF8);
		}
		byte[] resultBytes = result.getBytes(Constants.CHARSET_UTF8);
		setResponseContentType(request, response);
		response.setContentLength(resultBytes.length);
		response.setCharacterEncoding(Constants.CHARSET_UTF8);
		FileCopyUtils.copy(resultBytes, response.getOutputStream());
	}

	private Map<JSONObject, JSONArray> findReplaceElements(JSONObject parent, String propertyPath) throws JSONException {
		Map<JSONObject, JSONArray> result = new HashMap();
		String property = null;
		JSONArray jsonArray = null;
		String[] paths = propertyPath.split("\\.");
		int propertyDepth = paths.length;
		property = paths[0];
		if (propertyDepth == 1) {
			if (parent.isNull(property)) {
				return new HashMap();
			}
			jsonArray = parent.getJSONArray(property);
			result.put(parent, jsonArray);
		} else {
			if (parent.isNull(property)) {
				return new HashMap();
			}
			Object temp = parent.get(property);
			if (JSONObject.class.isInstance(temp)) {
				parent = (JSONObject) temp;
				result.putAll(findReplaceElements(parent, StringUtils.join(ArrayUtils.removeElement(paths, property), ".")));
			} else if (JSONArray.class.isInstance(temp)) {
				JSONArray parentArray = (JSONArray) temp;
				int length = parentArray.length();
				for (int j = 0; j < length; j++) {
					JSONObject tempParent = parentArray.getJSONObject(j);
					result.putAll(findReplaceElements(tempParent, StringUtils.join(ArrayUtils.removeElement(paths, property), ".")));
				}
			}
		}
		return result;
	}

	private void replace(JSONObject parent, String propertyName, JSONArray data) throws JSONException {
		parent.remove(propertyName);
		String mockProperty = propertyName.substring(0, propertyName.length() - 1);
		JSONObject mockObj = new JSONObject();
		mockObj.put(mockProperty, data);
		parent.put(propertyName, mockObj);
	}

	protected Object locateToBeMarshalled(Map<String, Object> model) throws ServletException {
		if (this.modelKey != null) {
			Object o = model.get(this.modelKey);
			if (o == null) {
				throw new ServletException("Model contains no object with key [" + this.modelKey + "]");
			}
			if (!(this.marshaller.supports(o.getClass()))) {
				throw new ServletException("Model object [" + o + "] retrieved via key [" + this.modelKey
						+ "] is not supported by the Marshaller");
			}

			return o;
		}
		for (Iterator i$ = model.values().iterator(); i$.hasNext();) {
			Object o = i$.next();
			if ((o != null) && (this.marshaller.supports(o.getClass()))) {
				return o;
			}
		}
		return null;
	}
}