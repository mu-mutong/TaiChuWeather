package com.taichuweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.taichuweather.app.R;
import com.taichuweather.db.TaiChuWeatherDB;
import com.taichuweather.model.City;
import com.taichuweather.model.Country;
import com.taichuweather.model.Province;
import com.taichuweather.util.HttpCallbackListener;
import com.taichuweather.util.HttpUtil;
import com.taichuweather.util.Utility;

import android.text.TextUtils;
//import android.R;
import android.view.View;
import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTRY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleView;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private TaiChuWeatherDB taiChuWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	
	/*
	 * province list
	 * */
	private List<Province> provinceList;
	/*
	 * city list
	 */
	private List<City> cityList;
	/*
	 *country list
	 * */
	private List<Country> countryList;
	/*
	 * the select province
	 * */
	private Province selectedProvince;
	//the select city
	private City selectedCity;
	//the select Level
	private int currentLevel;
	
	
	protected void onCreate (Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleView = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		taiChuWeatherDB = TaiChuWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?>arg0,View view,int index,long arg3){
			if(currentLevel == LEVEL_PROVINCE){
				selectedProvince = provinceList.get(index);
				queryCities();
			}else if(currentLevel == LEVEL_CITY){
				selectedCity = cityList.get(index);
				queryCounties();
			}
			}
		});
		queryProvinces();
	}
	 /*
	  * 查询全国省份，优先从数据库查询，如果没有查询到，到服务器查询
	  * */
	
	private void queryProvinces(){
		provinceList = taiChuWeatherDB.loadProvinces();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province province : provinceList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleView.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		}else{
			queryFromServer(null,"province");
		}
	}
	/*
	 * 查询所有省内所有市，优先从数据库查询，如果没有去服务器查询
	 * */
	private void queryCities(){
		cityList = taiChuWeatherDB.loadCities(selectedProvince.getId());
		if(cityList.size()>0){
			dataList.clear();
			for(City city : cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleView.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
			
		}else{
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}
	/*
	 * 查询所有市内所有村，优先从数据库查询，如果没有去服务器查询
	 * */
	private void queryCounties(){
		countryList = taiChuWeatherDB.loadCountries(selectedCity.getId());
		if(countryList.size()>0){
			dataList.clear();
			for(Country country : countryList){
				dataList.add(country.getCountryName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleView.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTRY;
			
		}else{
			queryFromServer(selectedCity.getCityCode(), "country");
		}
	}
	private void queryFromServer(final String code,final String type){
		String address;
		if(!TextUtils.isEmpty(code)){
			address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
		}else{
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				if("province".equals(type)){
					result = Utility.handleProvincesResponse(taiChuWeatherDB, response);
					
				}else if("city".equals(type)){
					result = Utility.handleCitiesResponse(taiChuWeatherDB, response, selectedProvince.getId());
				}else if("country".equals(type)){
					result= Utility.handleCountriesResponse(taiChuWeatherDB, response, selectedCity.getId());
				}
				if(result){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if ("city".equals(type)){
								queryCities();
							}else if("country".equals(type)){
								queryCounties();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		
	}
	/*
	 * 显示进度对话框
	 * */
	private void showProgressDialog() {
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	/*
	 * 关闭对话进度框
	 * */
	private void  closeProgressDialog() {
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	public void onBackPressed() {
		if(currentLevel == LEVEL_COUNTRY){
			queryCities();
		}else if(currentLevel == LEVEL_CITY){
			queryProvinces();
		}else{
			finish();
		}
		
	}
}
