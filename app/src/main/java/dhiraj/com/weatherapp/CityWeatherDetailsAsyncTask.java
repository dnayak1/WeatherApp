package dhiraj.com.weatherapp;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dhira on 07-04-2017.
 */

public class CityWeatherDetailsAsyncTask extends AsyncTask<String,Void,ArrayList<CityWeatherDetails>> {
    ICityWeatherDetailsData activity;

    public CityWeatherDetailsAsyncTask(ICityWeatherDetailsData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<CityWeatherDetails> doInBackground(String... params) {
        try {
            URL url=new URL(params[0]);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode=connection.getResponseCode();
            if(statusCode== HttpsURLConnection.HTTP_OK){
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder=new StringBuilder();
                String line=bufferedReader.readLine();
                while ((line!=null)){
                    stringBuilder.append(line);
                    line=bufferedReader.readLine();
                }
                return CityWeatherDetailsUtil.CityWeatherDetailsSONParser.parseCityWeatherDetails(stringBuilder.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<CityWeatherDetails> cityWeatherDetailses) {
        super.onPostExecute(cityWeatherDetailses);
        activity.setupCityWeatherDetailsData(cityWeatherDetailses);
    }

    static public interface ICityWeatherDetailsData{
        public void setupCityWeatherDetailsData(ArrayList<CityWeatherDetails> cityWeatherDetailses);
    }
}
