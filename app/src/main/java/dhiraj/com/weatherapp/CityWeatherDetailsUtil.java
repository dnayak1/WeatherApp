package dhiraj.com.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dhira on 07-04-2017.
 */

public class CityWeatherDetailsUtil {
    static public class CityWeatherDetailsSONParser{
        static String headline;
        static ArrayList<CityWeatherDetails> parseCityWeatherDetails(String in) throws JSONException {
            ArrayList<CityWeatherDetails> cityWeatherDetailsArrayList=new ArrayList<>();
            JSONObject outerRoot=new JSONObject(in);
            JSONObject root=outerRoot.getJSONObject("Headline");
            headline=root.getString("Text");
            JSONArray jsonArrayForecasts=outerRoot.getJSONArray("DailyForecasts");
            for(int i=0;i<jsonArrayForecasts.length();i++){
                JSONObject jsonObjectCityWeatherDetail=jsonArrayForecasts.getJSONObject(i);
                CityWeatherDetails cityWeatherDetails=new CityWeatherDetails();
                cityWeatherDetails.setHeadline(headline);
                cityWeatherDetails.setForecastDate(jsonObjectCityWeatherDetail.getString("Date"));
                JSONObject jsonObjectTemperature=jsonObjectCityWeatherDetail.getJSONObject("Temperature");
                JSONObject jsonObjectMinimumTemperature=jsonObjectTemperature.getJSONObject("Minimum");
                cityWeatherDetails.setTemperatureMin(jsonObjectMinimumTemperature.getString("Value"));
                JSONObject jsonObjectMaximumTemperature=jsonObjectTemperature.getJSONObject("Maximum");
                cityWeatherDetails.setTemperatureMax(jsonObjectMaximumTemperature.getString("Value"));
                JSONObject jsonObjectDay=jsonObjectCityWeatherDetail.getJSONObject("Day");
                cityWeatherDetails.setDayImage(jsonObjectDay.getString("Icon"));
                cityWeatherDetails.setDayText(jsonObjectDay.getString("IconPhrase"));
                JSONObject jsonObjectNight=jsonObjectCityWeatherDetail.getJSONObject("Night");
                cityWeatherDetails.setNightImage(jsonObjectNight.getString("Icon"));
                cityWeatherDetails.setNightText(jsonObjectNight.getString("IconPhrase"));
                cityWeatherDetails.setMoreDetails(jsonObjectCityWeatherDetail.getString("MobileLink"));
                cityWeatherDetails.setExtendedForeCast(root.getString("MobileLink"));
                cityWeatherDetailsArrayList.add(cityWeatherDetails);
            }
            return cityWeatherDetailsArrayList;
        }
    }
}
