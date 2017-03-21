package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XHYX on 2017/3/20.
 * 天气中空气质量
 */

public class AQI {
    public class CityAqi {
        @SerializedName("aqi")
        public String aqi;
        @SerializedName("pm25")
        public String pm25;
        @SerializedName("qlty")
        public String qlty;
    }
    public CityAqi cityAqi;
}
