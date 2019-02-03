package com.taichuweather.util;

import android.text.TextUtils;

import com.taichuweather.db.TaiChuWeatherDB;
import com.taichuweather.model.City;
import com.taichuweather.model.Country;
import com.taichuweather.model.Province;

public class Utility {

	/*
	 * 解析和处理服务器传回的省级数据
	 * */
	public synchronized static boolean handleProvincesResponse (TaiChuWeatherDB taiChuWeatherDB,String response) {
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length>0){
				for (String pString : allProvinces) {
					String[] array = pString.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//把解析后的数据存入表中
					taiChuWeatherDB.saveProvince(province);
					
				}
			return true;
			}
		}
		return false;
	}
	//解析和处理服务器返回的市级数据
	public synchronized static boolean handleCitiesResponse (TaiChuWeatherDB taiChuWeatherDB,String response,int provinceId) {
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length>0){
				for (String pString : allCities) {
					String[] array = pString.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId); 
					//把解析后的数据存入表中
					taiChuWeatherDB.saveCity(city);
					
				}
			return true;
			}
		}
		return false;
	}
	public synchronized static boolean handleCountriesResponse (TaiChuWeatherDB taiChuWeatherDB,String response,int cityId) {
		if(!TextUtils.isEmpty(response)){
			String[] allCountries = response.split(",");
			if(allCountries != null && allCountries.length>0){
				for (String pString : allCountries) {
					String[] array = pString.split("\\|");
					Country country = new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId); 
					//把解析后的数据存入表中
					taiChuWeatherDB.saveCountry(country);
					
				}
			return true;
			}
		}
		return false;
	}
}
