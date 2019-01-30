package com.taichuweather.model;

import java.util.ArrayList;
import java.util.List;

import com.taichuweather.db.TaiChuWeatherOpenHelper;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaiChuWeatherDB {
	public static final String DB_NAME = "Taichu";
	
	public static final int VERSION = 1;
	
	private static TaiChuWeatherDB taiChuWeatherDB;
	private SQLiteDatabase db;
	
	private TaiChuWeatherDB(Context context){
		TaiChuWeatherOpenHelper dbHelper = new TaiChuWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	/*
	 * ��ȡTaiChu���ݿ�ʵ��
	 * */
	public synchronized static TaiChuWeatherDB getInstance(Context context){
		if(taiChuWeatherDB == null){
			taiChuWeatherDB = new TaiChuWeatherDB(context);
		}
		return taiChuWeatherDB;
	}
	
	
	/*
	 * ��provinceʵ�����洢�����ݿ�
	 * */
	
	public void saveProvince(Province province) {
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name: ", province.getProvinceName());
			values.put("province_code: ", province.getProvinceCode());
			db.insert("Province", null, values);
			
		}
	}
	/*
	 * �����ݿ��ȡʡ������
	 * */
	
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null,null,null,null);
		if(cursor.moveToFirst()){
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("provinice_code")));
				list.add(province);
			}while (cursor.moveToNext());
		}
		return list;
	}
	/*
	 * ��cityʵ�����洢�����ݿ�
	 * */
	
	public void saveCity(City city) {
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name: ", city.getCityName());
			values.put("city_code: ", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
			
		}
	}
	/*
	 * �����ݿ��ȡ��������
	 * */
	
	public List<City> loadCities() {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, null, null,null,null,null);
		if(cursor.moveToFirst()){
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(cursor.getString(cursor.getColumnIndex("province_id")));
				list.add(city);
			}while (cursor.moveToNext());
		}
		return list;
	}
	/*
	 * ��countryʵ�����洢�����ݿ�
	 * */
	
	public void saveountry(Country country) {
		if(country != null){
			ContentValues values = new ContentValues();
			values.put("country_name: ", country.getCountryName());
			values.put("country_code: ", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert("Country", null, values);
			
		}
	}
	/*
	 * �����ݿ��ȡ��������
	 * */
	
	public List<Country> loadCountries() {
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query("Country", null, null, null,null,null,null);
		if(cursor.moveToFirst()){
			do {
				Country country = new Country();
				country.setId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCityId(cursor.getString(cursor.getColumnIndex("city_id")));
				list.add(country);
			}while (cursor.moveToNext());
		}
		return list;
	}
}
