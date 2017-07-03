package com.rkylin.wheatfield.response;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public abstract class Response implements Serializable{
	private static final long serialVersionUID = 1L;

	@XStreamOmitField
	private boolean callResult = true;

	public boolean getCallResult() {
		return callResult;
	}

	public void setCallResult(boolean callResult) {
		this.callResult = callResult;
	}

}
