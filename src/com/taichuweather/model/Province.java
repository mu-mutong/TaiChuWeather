package com.taichuweather.model;

import android.R.integer;
import android.R.string;

public class Province {
	private int id;
	private String provinceName;
	private String provinceCode;
	
	public int geidId(){
		return id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public String getProvinceName(){
		return provinceName;
	}
	
	public void setProvinceName(String provinceName){
		this.provinceName=provinceName;
	}
	
	public String getProvinceCode(){
		return provinceName;
	}

	public void setProvinceCode(String provinceCode){
		this.provinceCode=provinceCode;
	}
}
