package com.taichuweather.activity;

import javax.security.auth.PrivateCredentialPermission;

import com.taichuweather.app.R;
import com.taichuweather.util.HttpCallbackListener;
import com.taichuweather.util.HttpUtil;
import com.taichuweather.util.Utility;

import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity {

	private LinearLayout weatherInfoLayout;
		/*
		 *用于显示城市名字
		 */
		
	private TextView cityNameText;
	/*
	 *用于显示发布时间
	 */
	private TextView publisText;
	/*
	 *用于显示天气概述信息
	 */
	
	private TextView weahtherDespText;
	/*
	 *用于显示气温1
	 */
	private TextView temp1Text;
	/*
	 *用于显示气温2
	 */
	private TextView temp2Text;
	/*
	 *用于显示分布日期
	 */
	private TextView currentDateText;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publisText = (TextView) findViewById(R.id.publish_text);
		weahtherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		String countryCode = getIntent().getStringExtra("country_code");
		if(!TextUtils.isEmpty(countryCode)){
			publisText.setText("同步中");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countryCode);
			
		}else{
			
			showWeather();
		}
	}
	private	void queryWeatherCode(String countryCode){
		String address = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";
		queryFromServer(address,"countryCode");
	}
	private	void queryWeatherInfo(String weatherCode){
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode+ ".html";
		queryFromServer(address,"weatherCode");
	}
	
	private void queryFromServer(final String address,final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				// TODO Auto-generated method stub
				if("countryCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					
					Utility.handleWeatherResonse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
					
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						publisText.setText("同步失败");
					}
				});
			}
		});
	}
	private void showWeather() {
	
		SharedPreferences prefs = PreferenceManager.
getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name",""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weahtherDespText.setText(prefs.getString("weather_desp", ""));
		publisText.setText("今天"+prefs.getString("publish_time", "")+"发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
}
