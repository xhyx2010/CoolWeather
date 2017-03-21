package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XHYX on 2017/3/20.
 * 天气中基本信息类
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    @SerializedName("update")
    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
