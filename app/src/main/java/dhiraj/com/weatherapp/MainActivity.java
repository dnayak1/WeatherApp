package dhiraj.com.weatherapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements CurrentCityAsyncTask.IData,WeatherAsyncTask.IWeatherData {
    Button buttonSetCurrentCity;
    EditText editTextSetCurrentCityName;
    EditText editTextSetCurrentCountryName;
    String currentCity,currentCountry,currentTemperature,currentKey;
    ProgressBar progressBarCurrentCity;
    RelativeLayout linearLayoutCurrent;
    TextView textViewCurrentCityCountry;
    TextView textViewWeatherText;
    TextView textViewTemperature;
    TextView textViewUpdatedTime;
    ImageView imageViewWeather;
    TextView textViewNotSetLabel;
    TextView textViewNoCity;
    TextView textViewNoSavedCities;
    TextView textViewNoSavedCities2;
    String imageUrl,savedKey;
    Date outputDate;
    EditText editTextSearchCity;
    EditText editTextSearchCountry;
    Button buttonSearch;
    SharedPreferences currentLocationPreferences;
    public static final int REQ_KEY=100;
    public static final String VAL_KEY="value";
    public static final String COUNTRY_KEY="value1";
    public static final String CITY_KEY="value2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        getSupportActionBar().setTitle("  Weather App");
        getSupportActionBar().setIcon(R.drawable.summer);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        buttonSetCurrentCity= (Button) findViewById(R.id.buttonSetCurrentCity);
        linearLayoutCurrent= (RelativeLayout) findViewById(R.id.linearLayoutWeather);
        textViewCurrentCityCountry= (TextView) findViewById(R.id.textViewCurrentCityCountry);
        textViewWeatherText= (TextView) findViewById(R.id.textViewCurrentWeatherText);
        textViewTemperature= (TextView) findViewById(R.id.textViewCurrentTemperature);
        textViewUpdatedTime= (TextView) findViewById(R.id.textViewCurrentUpdatedTime);
        textViewNotSetLabel= (TextView) findViewById(R.id.textViewNotSetLabel);
        textViewNoCity= (TextView) findViewById(R.id.textViewNoCity);
        imageViewWeather= (ImageView) findViewById(R.id.imageViewCurrentImage);
        progressBarCurrentCity= (ProgressBar) findViewById(R.id.progressBarCurrentCity);
        progressBarCurrentCity.setVisibility(View.INVISIBLE);
        editTextSearchCity= (EditText) findViewById(R.id.editTextCityName);
        editTextSearchCountry= (EditText) findViewById(R.id.editTextCountryName);
        buttonSearch= (Button) findViewById(R.id.buttonSearchCity);
        textViewNoSavedCities= (TextView) findViewById(R.id.textViewNoSavedCitiesToDisplay);
        textViewNoSavedCities2= (TextView) findViewById(R.id.textViewNoSavedCitiesToDisplay2);
        textViewNoSavedCities.setText("There are no cities to display.");
        textViewNoSavedCities2.setText("Search the city from search the box and save.");
        currentLocationPreferences=getSharedPreferences("locationPreferences", Context.MODE_PRIVATE);
        savedKey=currentLocationPreferences.getString("key","").trim();
        if(!savedKey.isEmpty()){
            buttonSetCurrentCity.setVisibility(View.INVISIBLE);
            textViewNotSetLabel.setVisibility(View.INVISIBLE);
            linearLayoutCurrent.setVisibility(View.VISIBLE);
            currentCountry=currentLocationPreferences.getString("country","");
            currentCity=currentLocationPreferences.getString("city","");
            currentTemperature=currentLocationPreferences.getString("temperature","");
            new WeatherAsyncTask(MainActivity.this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+savedKey+"?apikey=GGGvhnax8EYhgq1ICf6Qo5x2bMLjTBGh");
        }
        buttonSetCurrentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater=MainActivity.this.getLayoutInflater();
                final View layout=inflater.inflate(R.layout.alert_layout,null);
                builder.setView(layout);
                builder.setTitle("Enter City Details");
                builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editTextSetCurrentCityName= (EditText) layout.findViewById(R.id.editTextAlertCurrentCityName);
                        editTextSetCurrentCountryName= (EditText) layout.findViewById(R.id.editTextAlertCurrentCountryName);
                        currentCity=editTextSetCurrentCityName.getText().toString().trim();
                        currentCountry=editTextSetCurrentCountryName.getText().toString().trim();
                        getCityKey(currentCity,currentCountry);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextSearchCity.getText().toString().trim()!=null && !editTextSearchCity.getText().toString().trim().isEmpty()
                        && editTextSearchCountry.getText().toString().trim()!=null && !editTextSearchCountry.getText().toString().trim().isEmpty()){
                    Intent intent=new Intent(MainActivity.this,CityWeatherActivity.class);
                    intent.putExtra(MainActivity.COUNTRY_KEY,editTextSearchCountry.getText().toString().trim());
                    intent.putExtra(MainActivity.CITY_KEY,editTextSearchCity.getText().toString().trim());
                    startActivityForResult(intent,REQ_KEY);
                }
                else{
                    Toast.makeText(MainActivity.this, "City Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void getCityKey(String currentCity, String currentCountry){
        new CurrentCityAsyncTask(MainActivity.this).execute("http://dataservice.accuweather.com/locations/v1/"+currentCountry+"/search?apikey=GGGvhnax8EYhgq1ICf6Qo5x2bMLjTBGh&q="+currentCity);
    }

    @Override public void setupData(KeyDetails keyDetails) {
        if(keyDetails==null)
            Toast.makeText(this, "City Not Found", Toast.LENGTH_SHORT).show();
        else{
            currentKey=keyDetails.getKey();
            currentCountry=keyDetails.getCountry();
            currentCity=keyDetails.getCity();
            if(savedKey.isEmpty())
                Toast.makeText(this, "Current City Details Saved", Toast.LENGTH_SHORT).show();
            progressBarCurrentCity.setVisibility(View.VISIBLE);
            currentLocationPreferences=getSharedPreferences("locationPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=currentLocationPreferences.edit();
            editor.putString("city",currentCity);
            editor.putString("country",currentCountry);
            editor.putString("key",currentKey);
            editor.apply();
            new WeatherAsyncTask(MainActivity.this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+currentKey+"?apikey=GGGvhnax8EYhgq1ICf6Qo5x2bMLjTBGh");
        }

    }

    @Override
    public void setupWeatherData(Weather weather) {
        progressBarCurrentCity.setVisibility(View.INVISIBLE);
        buttonSetCurrentCity.setVisibility(View.INVISIBLE);
        textViewNotSetLabel.setVisibility(View.INVISIBLE);
        linearLayoutCurrent.setVisibility(View.VISIBLE);
        textViewCurrentCityCountry.setText(currentCity+","+currentCountry);
        textViewWeatherText.setText(weather.getWeatherText());
        if(Integer.parseInt(weather.getWeatherIcon())<10){
            imageUrl="0"+weather.getWeatherIcon();
        }
        else{
            imageUrl=weather.getWeatherIcon();
        }
        String weatherIconUrl="http://developer.accuweather.com/sites/default/files/"+imageUrl+"-s.png";
        Picasso.with(this).load(weatherIconUrl).into(imageViewWeather);
        if(currentTemperature!=null && !currentTemperature.isEmpty() && currentTemperature.equalsIgnoreCase("Fahrenheit")){
            textViewTemperature.setText("Temperature: "+weather.getMetricFahrenheit()+"°F");
        }
        else{
            textViewTemperature.setText("Temperature: "+weather.getMetricCelsius()+"°C");
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat simpleDateFormatNewFormat=new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String dateString=weather.getLocalObservationDateTime();
        try {
            Date inputDate=simpleDateFormat.parse(dateString);
            String outputDateString=simpleDateFormatNewFormat.format(inputDate);
            outputDate=simpleDateFormatNewFormat.parse(outputDateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime=new PrettyTime();
        String stringPrettyTime=prettyTime.format(outputDate);
        textViewUpdatedTime.setText("Updated "+stringPrettyTime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.prefSetting:
                startActivityForResult(new Intent(MainActivity.this,SettingsActivity.class),REQ_KEY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currentLocationPreferences=getSharedPreferences("locationPreferences", Context.MODE_PRIVATE);
        currentCountry=currentLocationPreferences.getString("country","");
        currentCity=currentLocationPreferences.getString("city","");
        savedKey=currentLocationPreferences.getString("key","");
        currentTemperature=currentLocationPreferences.getString("temperature","");
        getCityKey(currentCity,currentCountry);
    }

}
