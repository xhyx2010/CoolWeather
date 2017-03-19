package com.coolweather.android.util;

import android.text.TextUtils;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by XHYX on 2017/3/19.
 * 处理JSON格式数据
 */

public class Utility {
    /*
    解析处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String string) {
        if (!TextUtils.isEmpty(string)) {
            try {
                JSONArray allProvinces = new JSONArray(string);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    解析处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String string, int provinceId) {
        if (!TextUtils.isEmpty(string)) {
            try {
                JSONArray allCityes = new JSONArray(string);
                for (int i = 0; i < allCityes.length(); i++) {
                    JSONObject cityObject = allCityes.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();

                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    解析处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String string, int cityId) {
        if (!TextUtils.isEmpty(string)) {
            try {
                JSONArray allCounty = new JSONArray(string);
                for (int i = 0; i < allCounty.length(); i++) {
                    JSONObject countyObject = allCounty.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
