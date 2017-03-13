package com.hznge.mweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hznge on 2017/3/12.
 */

public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature mTemperature;

    @SerializedName("cond")
    public More mMore;

    public class Temperature {

        public String max;

        public String min;

    }

    public class More {

        @SerializedName("txt_d")
        public String info;
    }

}
