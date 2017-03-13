package com.hznge.mweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hznge on 2017/3/12.
 */

public class Weather {

    public String status;

    public Basic mBasic;

    public AQI mAQI;

    public Now mNow;

    public Suggestion mSuggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> mForecastList;

}
