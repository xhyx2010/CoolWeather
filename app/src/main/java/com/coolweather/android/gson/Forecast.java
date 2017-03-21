package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XHYX on 2017/3/20.
 * 天气中未来天气信息,定义一天的数据信息，后面使用数组List类型存储
 */

public class Forecast {
    @SerializedName("date")
    public String dateTime;
    @SerializedName("tmp")
    public Temperature temperature;
    @SerializedName("cond")
    public More more;

    public class Temperature {
        public String max;
        public String min;
    }

    public class More {
        @SerializedName("txt_d")
        public String info;
    }


}
