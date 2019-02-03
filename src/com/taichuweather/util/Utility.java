package com.taichuweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
	public static void handleWeatherResonse(Context context,String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}
