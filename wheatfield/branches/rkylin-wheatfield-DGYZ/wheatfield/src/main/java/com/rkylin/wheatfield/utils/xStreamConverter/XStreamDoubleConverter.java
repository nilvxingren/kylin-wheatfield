package com.rkylin.wheatfield.utils.xStreamConverter;

import com.thoughtworks.xstream.converters.basic.DoubleConverter;

public class XStreamDoubleConverter extends DoubleConverter{

	@Override
	public String toString(Object obj){
		String result = String.format("%.2f", obj);
		return result;
	}

}
