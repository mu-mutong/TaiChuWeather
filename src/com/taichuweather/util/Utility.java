package com.taichuweather.util;

import android.text.TextUtils;

import com.taichuweather.db.TaiChuWeatherDB;
import com.taichuweather.model.City;
import com.taichuweather.model.Country;
import com.taichuweather.model.Province;

public class Utility {

	/*
	 * �����ʹ�����������ص�ʡ������
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
					//�ѽ���������ݴ������
					taiChuWeatherDB.saveProvince(province);
					
				}
			return true;
			}
		}
		return false;
	}
	//�����ʹ�����������ص��м�����
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
					//�ѽ���������ݴ������
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
					//�ѽ���������ݴ������
					taiChuWeatherDB.saveCountry(country);
					
				}
			return true;
			}
		}
		return false;
	}
}
