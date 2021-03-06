package com.taichuweather.db;

import java.util.ArrayList;
import java.util.List;

import com.taichuweather.model.City;
import com.taichuweather.model.Country;
import com.taichuweather.model.Province;

import android.R.integer;
import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaiChuWeatherDB {
	public static final String DB_NAME = "Taichu";
	
	public static final int VERSION = 2;
	
	private static TaiChuWeatherDB taiChuWeatherDB;
	private SQLiteDatabase db;
	
	private TaiChuWeatherDB(Context context){
		TaiChuWeatherOpenHelper dbHelper = new TaiChuWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	/*
	 * 获取TaiChu数据库实例
	 * */
	public synchronized static TaiChuWeatherDB getInstance(Context context){
		if(taiChuWeatherDB == null){
			taiChuWeatherDB = new TaiChuWeatherDB(context);
		}
		return taiChuWeatherDB;
	}
	
	
	/*
	 * 把province实例化存储到数据库
	 * */
	
	public void saveProvince(Province province) {
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
			
		}
	}
	/*
	 * 从数据库读取省份数据
	 * */
	
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null,null,null,null);
		if(cursor.moveToFirst()){
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while (cursor.moveToNext());
		}
		return list;
	}
	/*
	 * 把city实例化存储到数据库
	 * */
	
	public void saveCity(City city) {
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
			
		}
	}
	/*
	 * 从数据库读取城市数据
	 * */
	
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?", new String[] {String.valueOf(provinceId)},null,null,null);
		if(cursor.moveToFirst()){
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while (cursor.moveToNext());
		}
		return list;
	}
	/*
	 * 把country实例化存储到数据库
	 * */
	
	public void saveCountry(Country country) {
		if(country != null){
			ContentValues values = new ContentValues();
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert("Country", null, values);
			
		}
	}
	/*
	 * 从数据库读取country数据
	 * */
	
	public List<Country> loadCountries(int cityId) {
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query("Country", null, "city_id = ?", new String[]{String.valueOf(cityId)},null,null,null);
		if(cursor.moveToFirst()){
			do {
				Country country = new Country();
				country.setId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCityId(cityId);
				list.add(country);
			}while (cursor.moveToNext());
		}
		return list;
	}
}
