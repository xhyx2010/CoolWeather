package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XHYX on 2017/3/20.
 * 天气中当前天气信息
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    public class More {
        @SerializedName("txt")
        public String info;
    }
}
