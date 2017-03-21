package com.coolweather.android.gson;

/**
 * Created by XHYX on 2017/3/20.
 * 天气中空气质量
 */

public class AQI {
    public CityAqi city;
    public class CityAqi {
        public String aqi;
        public String pm25;
        public String qlty;
    }

}
