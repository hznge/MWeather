package com.hznge.mweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hznge on 2017/3/12.
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
