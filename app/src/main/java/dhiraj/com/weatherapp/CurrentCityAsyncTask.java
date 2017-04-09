package dhiraj.com.weatherapp;

import android.os.AsyncTask;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dhira on 05-04-2017.
 */

public class CurrentCityAsyncTask extends AsyncTask<String, Void, KeyDetails> {
    IData activity;

    public CurrentCityAsyncTask(IData activity) {
        this.activity = activity;
    }

    @Override
    protected KeyDetails doInBackground(String... params) {
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
                return CurrentCityKeyUtil.CurrentCityKeyJSONParser.parseCurrentCityKey(stringBuilder.toString());
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
    protected void onPostExecute(KeyDetails keyDetails) {
        super.onPostExecute(keyDetails);
        activity.setupData(keyDetails);
    }

    static public interface IData{
        public void setupData(KeyDetails keyDetails);
    }
}
