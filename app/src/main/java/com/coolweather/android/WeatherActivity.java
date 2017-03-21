package com.coolweather.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by XHYX on 2017/3/20.
 * 天气活动类引用activity_weather.xml
 */

public class WeatherActivity extends AppCompatActivity {
    //声明activity_weather.xml中各控件
    private TextView titleCity;
    private TextView title_update_time;
    private TextView degree_text;
    private TextView weather_info_text;
    private LinearLayout forecast_layout;
    private TextView aqi_txt;
    private TextView pm25_txt;
    private TextView qlty_txt;
    private TextView comfort_txt;
    private TextView carWash_txt;
    private TextView drsg_txt;
    private TextView sport_txt;
    private ScrollView weather_layout;
    private ImageView bing_pic;
    public SwipeRefreshLayout swipe_refresh;
    public DrawerLayout drawer_layout;
    private Button nav_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始化各控件
        weather_layout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        title_update_time = (TextView) findViewById(R.id.title_update_time);
        degree_text = (TextView) findViewById(R.id.degree_text);
        weather_info_text = (TextView) findViewById(R.id.weather_info_text);
        forecast_layout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqi_txt = (TextView) findViewById(R.id.aqi_txt);
        pm25_txt = (TextView) findViewById(R.id.pm25_txt);
        qlty_txt=(TextView) findViewById(R.id.qlty_txt);
        comfort_txt = (TextView) findViewById(R.id.comfort_txt);
        carWash_txt = (TextView) findViewById(R.id.carWash_txt);
        drsg_txt = (TextView) findViewById(R.id.drsg_txt);
        sport_txt = (TextView) findViewById(R.id.sport_txt);
        bing_pic=(ImageView) findViewById(R.id.bing_pic);
        swipe_refresh=(SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        drawer_layout=(DrawerLayout) findViewById(R.id.drawer_layout);
        nav_button=(Button) findViewById(R.id.nav_botton);

        //设置监听城市切换按钮
        nav_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });
        //设置手动下拉刷新时进度样式为系统colorPrimary
        swipe_refresh.setColorSchemeResources(R.color.colorPrimary);

        //获取缓存
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather", null);
        final String weatherId;
        //检查缓存中是不是有bing每日一图
        String bingPic=preferences.getString("bing_pic",null);
        if(bingPic!=null){
            //有直接插入图片控件中
            Glide.with(this).load(bingPic).into(bing_pic);
        }else {
            //没有到服务器上获取
            loadBingPic();
        }

        //检查是否有缓存天气信息

        if (weatherString != null) {
            //有缓存天气信息直接解析天气
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId=weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            //无缓存时去服务器查询天气
            weatherId = getIntent().getStringExtra("weather_id");
            weather_layout.setVisibility(View.INVISIBLE);
            //Log.d("cool",weatherId);
            requestWeather(weatherId);
        }
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
    }
    //到服务器上获取bing每日一图
    private void loadBingPic(){
        final String  requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bing_pic);
                    }
                });
            }
        });
    }

    //根据天气ID到服务器查询天气
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=848aa04ab5484b81909b497011cccd5e";
        Log.d("cool",weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WeatherActivity.this, "获取天气信息失败1", Toast.LENGTH_SHORT).show();
                                swipe_refresh.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        final String responseText=response.body().string();
                        final Weather weather=Utility.handleWeatherResponse(responseText);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(weather!=null&&"ok".equals(weather.status)){
                                    SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                                    editor.putString("weather",responseText);
                                    editor.apply();
                                    showWeatherInfo(weather);
                                }else {
                                    Toast.makeText(WeatherActivity.this,"获取天气信息失败2", Toast.LENGTH_SHORT).show();
                                }
                                swipe_refresh.setRefreshing(false);
                            }
                        });
                    }
                }
        );
        loadBingPic();
    }
    //处理展示天气实体类Weather中的数据
    public void showWeatherInfo(Weather weather){
        String cityName=weather.basic.cityName;
        String update=weather.basic.update.updateTime.split(" ")[1];
        String temperature=weather.now.temperature+"℃";
        String more=weather.now.more.info;

        titleCity.setText(cityName);
        title_update_time.setText(update);
        degree_text.setText(temperature);
        weather_info_text.setText(more);

        forecast_layout.removeAllViews();
        for (Forecast forecast:weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecast_layout,false);
            TextView dateText=(TextView)view.findViewById(R.id.date_text);
            TextView infoText=(TextView)view.findViewById(R.id.info_text);
            TextView max_text=(TextView)view.findViewById(R.id.max_text);
            TextView min_text=(TextView)view.findViewById(R.id.min_text);
            dateText.setText(forecast.dateTime);
            infoText.setText(forecast.more.info);
            max_text.setText(forecast.temperature.max);
            min_text.setText(forecast.temperature.min);
            forecast_layout.addView(view);
        }
        if (weather.aqi!=null){
            String aqiText=weather.aqi.city.aqi;
            String pm25Text=weather.aqi.city.pm25;
            String qltyText=weather.aqi.city.qlty;
            aqi_txt.setText(aqiText);
            pm25_txt.setText(pm25Text);
            qlty_txt.setText(qltyText);
        }else {
            aqi_txt.setText("0");
            pm25_txt.setText("0");
            qlty_txt.setText("未知");
        }


        String comfor="舒适度："+weather.suggestion.comfort.info;
        String carWash="洗车指数："+weather.suggestion.carWash.info;
        String drsg="穿衣指数："+weather.suggestion.drsg.info;
        String sport="运动指数："+weather.suggestion.sport.info;
        comfort_txt.setText(comfor);
        carWash_txt.setText(carWash);
        drsg_txt.setText(drsg);
        sport_txt.setText(sport);

        weather_layout.setVisibility(View.VISIBLE);
    }

}
