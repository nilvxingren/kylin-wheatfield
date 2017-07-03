package com.rkylin.wheatfield.model;

import java.util.List;

import com.rkylin.wheatfield.pojo.Dictionary;

public class DictionaryResponse  extends CommonResponse{

	private List<Dictionary> dicList;

	public List<Dictionary> getDicList() {
		return dicList;
	}

	public void setDicList(List<Dictionary> dicList) {
		this.dicList = dicList;
	}

}
