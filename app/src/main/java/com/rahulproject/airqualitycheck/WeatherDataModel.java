package com.rahulproject.airqualitycheck;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataModel {

    private String mTemperature;
    private String mCity;
    private String mIconName;
    private int mCondition;

    public static WeatherDataModel fromJson(JSONObject jsonObject) {
        try {
            WeatherDataModel weatherData = new WeatherDataModel();

            weatherData.mCity = jsonObject.getJSONObject("data").getJSONObject("city").getString("name");
            weatherData.mCondition =jsonObject.getJSONObject("data").getInt("aqi");
            weatherData.mIconName = updateWeatherIcon(weatherData.mCondition);
            int roundedValue = jsonObject.getJSONObject("data").getInt("aqi");

            weatherData.mTemperature = Integer.toString(roundedValue);

            return weatherData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 51) {
            return "green";
        } else if (condition >= 51 && condition < 100) {
            return "Yellow";
        } else if (condition >= 101 && condition < 200) {
            return "orange";
        } else if (condition >= 201 && condition <= 300) {
            return "voilet";
        } else if (condition >= 301 && condition <= 400) {
            return "purple";
        } else if (condition >= 401 && condition < 800) {
            return "Red";
        }

        return "dunno";
    }

    public String getTemperature() {
        return mTemperature;
    }

    public String getCity() {
        return mCity;
    }

    public String getIconName() {
        return mIconName;
    }
}
