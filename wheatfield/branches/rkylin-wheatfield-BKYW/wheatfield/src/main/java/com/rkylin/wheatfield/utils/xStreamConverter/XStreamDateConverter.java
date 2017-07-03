package com.rkylin.wheatfield.utils.xStreamConverter;

import java.util.Date;

import com.thoughtworks.xstream.converters.basic.DateConverter;

public class XStreamDateConverter extends DateConverter  {

    public XStreamDateConverter(String dateFormat) {
    	super(dateFormat, new String[] {dateFormat});
    }

    @Override
    public boolean canConvert(Class type) {

    	boolean checkFlag = false;

    	if(super.canConvert(type) || Date.class.isAssignableFrom(type)){
    		checkFlag = true;
    	}

		return checkFlag;
	}
}
