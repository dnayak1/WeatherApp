package dhiraj.com.weatherapp;

/**
 * Created by dhira on 06-04-2017.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherUtil {
    static public class WeatherJSONParser{
        static Weather parseWeather(String in) throws JSONException {
            JSONArray jsonArrayRoot=new JSONArray(in);
            JSONObject jsonObjectRoot=jsonArrayRoot.getJSONObject(0);
            Weather weather=new Weather();
            weather.setLocalObservationDateTime(jsonObjectRoot.getString("LocalObservationDateTime"));
            weather.setWeatherText(jsonObjectRoot.getString("WeatherText"));
            weather.setWeatherIcon(jsonObjectRoot.getString("WeatherIcon"));
            JSONObject jsonObjectTemperature=jsonObjectRoot.getJSONObject("Temperature");
            JSONObject jsonObjectMetric=jsonObjectTemperature.getJSONObject("Metric");
            weather.setMetricCelsius(jsonObjectMetric.getString("Value"));
            JSONObject jsonObjectImperial=jsonObjectTemperature.getJSONObject("Imperial");
            weather.setMetricFahrenheit(jsonObjectImperial.getString("Value"));
            return weather;
        }
    }
}
