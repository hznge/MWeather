package com.hznge.mweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hznge.mweather.gson.Forecast;
import com.hznge.mweather.gson.Weather;
import com.hznge.mweather.util.HttpUtil;
import com.hznge.mweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        findCompoment();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String weatherStr = prefs.getString("weather", null);
        if (weatherStr != null) {
            // if cached then parse it
            Weather weather = Utility.handleWeatherResponse(weatherStr);
            showWeatherInfo(weather);
        } else {
            // If no cached then parse from server
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }


    // Get Weather info by id
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=d5c59fa9882e422b8a55f111c7e033de";

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "Load Weather Info Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "Load Weather Info Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.mBasic.cityName;
        String updateTime = weather.mBasic.mUpdate.updateTime.split(" ")[1];
        String degree = weather.mNow.temperature + "°";
        String weatherInfo = weather.mNow.more.info;
        
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        
        forecastLayout.removeAllViews();

        for (Forecast forecast :
                weather.mForecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) findViewById(R.id.date_text);
            TextView infoText = (TextView) findViewById(R.id.info_text);
            TextView maxText = (TextView) findViewById(R.id.max_text);
            TextView minText = (TextView) findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.mMore.info);
            maxText.setText(forecast.mTemperature.max);
            minText.setText(forecast.mTemperature.min);

            forecastLayout.addView(view);
        }

        if (weather.mAQI != null) {
            aqiText.setText(weather.mAQI.mCity.api);
            pm25Text.setText(weather.mAQI.mCity.pm25);
        }

        String comfort = "舒适度： " + weather.mSuggestion.mComfort.info;
        String carWash = "洗车指数：" + weather.mSuggestion.mCarWash.info;
        String sport = "运动建议：" + weather.mSuggestion.mSport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);
    }
    
    private void findCompoment() {
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
    }
}
