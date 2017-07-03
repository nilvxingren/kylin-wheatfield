package com.rkylin.wheatfield.utils.xStreamConverter;

import com.thoughtworks.xstream.converters.basic.StringConverter;

public class XStreamStringConverter extends StringConverter{

	@Override
	public String toString(Object obj){
		String result = super.toString(obj);
		return result == null ? null : result.replaceAll("&#x[0-8B-CE-F];", "");
	}
}
