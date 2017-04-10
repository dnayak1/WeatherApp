package dhiraj.com.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CityWeatherActivity extends AppCompatActivity implements CurrentCityAsyncTask.IData,CityWeatherDetailsAsyncTask.ICityWeatherDetailsData,GridRecyclerAdapter.IGridListener,WeatherAsyncTask.IWeatherData {
    TextView textViewCityCountryDetails;
    TextView textViewHeadline;
    TextView textViewForecastDate;
    TextView textViewTemperature;
    ImageView imageViewDay;
    ImageView imageViewNight;
    TextView textViewDayText;
    TextView textViewNightText;
    RecyclerView recyclerView;
    TextView textViewMoreDetails;
    TextView textViewExtendedForeCast;
    RelativeLayout relativeLayoutCityWeatherContainer;
    ProgressBar progressBarCityWeather;
    String country, city, dayImageUrl, nightImageUrl, outputDateString, cityKey;
    GridRecyclerAdapter gridRecyclerAdapter;
    LinearLayoutManager layoutManager;
    Date inputDate;
    String currentTemperature,storeTemperature,updatedTime;
    SharedPreferences currentLocationPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        getSupportActionBar().setTitle("  Weather App");
        getSupportActionBar().setIcon(R.drawable.summer);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        textViewCityCountryDetails= (TextView) findViewById(R.id.textViewCityWeatherDetails);
        textViewHeadline= (TextView) findViewById(R.id.textViewCityWeatherHeadline);
        textViewForecastDate= (TextView) findViewById(R.id.textViewCityWeatherForecastDate);
        textViewTemperature= (TextView) findViewById(R.id.textViewCityWeatherTemperature);
        imageViewDay= (ImageView) findViewById(R.id.imageViewCityWeatherDay);
        imageViewNight= (ImageView) findViewById(R.id.imageViewCityWeatherNight);
        textViewDayText= (TextView) findViewById(R.id.textViewCityWeatherDayText);
        textViewNightText= (TextView) findViewById(R.id.textViewCityWeatherNightText);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerViewHorizontal);
        textViewMoreDetails= (TextView) findViewById(R.id.textViewCityWeatherMoreDetails);
        textViewExtendedForeCast= (TextView) findViewById(R.id.textViewCityWeatherExtendedForecast);
        progressBarCityWeather= (ProgressBar) findViewById(R.id.progressBarCityWeather);
        relativeLayoutCityWeatherContainer= (RelativeLayout) findViewById(R.id.cityWeatherContainer);
        country=getIntent().getStringExtra(MainActivity.COUNTRY_KEY);
        city=getIntent().getStringExtra(MainActivity.CITY_KEY);
        progressBarCityWeather.setVisibility(View.VISIBLE);
        relativeLayoutCityWeatherContainer.setVisibility(View.INVISIBLE);
        new CurrentCityAsyncTask(this).execute("http://dataservice.accuweather.com/locations/v1/"+country+"/search?apikey=GGGvhnax8EYhgq1ICf6Qo5x2bMLjTBGh&q="+city);

    }

    @Override
    public void setupData(KeyDetails keyDetails) {
        if(keyDetails==null){
            Toast.makeText(this, "City Not Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            cityKey=keyDetails.getKey();
            country=keyDetails.getCountry();
            city=keyDetails.getCity();
            new CityWeatherDetailsAsyncTask(this).execute("http://dataservice.accuweather.com/forecasts/v1/daily/5day/"+keyDetails.getKey()+"?apikey=GGGvhnax8EYhgq1ICf6Qo5x2bMLjTBGh");
        }
    }

    @Override
    public void setupCityWeatherDetailsData(ArrayList<CityWeatherDetails> cityWeatherDetailses) {
        progressBarCityWeather.setVisibility(View.INVISIBLE);
        relativeLayoutCityWeatherContainer.setVisibility(View.VISIBLE);
        setSelectedData(cityWeatherDetailses.get(0));
        gridRecyclerAdapter=new GridRecyclerAdapter(CityWeatherActivity.this,cityWeatherDetailses,CityWeatherActivity.this);
        recyclerView.setAdapter(gridRecyclerAdapter);
        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        gridRecyclerAdapter.notifyDataSetChanged();

    }

    public void setSelectedData(final CityWeatherDetails selectedData){
        textViewCityCountryDetails.setText("Daily forecast for "+city+", "+country);
        textViewHeadline.setText(selectedData.getHeadline());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat simpleDateFormatNewFormat=new SimpleDateFormat("MMMM dd, yyyy");
        String dateString=selectedData.getForecastDate();
        try {
            inputDate=simpleDateFormat.parse(dateString);
            outputDateString=simpleDateFormatNewFormat.format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textViewForecastDate.setText("Forecast on "+outputDateString);
        textViewTemperature.setText("Temperature: "+selectedData.getTemperatureMax()+"°/"+selectedData.getTemperatureMin()+"°");
        if(Integer.parseInt(selectedData.getDayImage())<10){
            dayImageUrl="0"+selectedData.getDayImage();
        }
        else{
            dayImageUrl=selectedData.getDayImage();
        }
        if(Integer.parseInt(selectedData.getNightImage())<10){
            nightImageUrl="0"+selectedData.getNightImage();
        }
        else{
            nightImageUrl=selectedData.getNightImage();
        }

        String dayImageIconUrl="http://developer.accuweather.com/sites/default/files/"+dayImageUrl+"-s.png";
        String nightImageIconUrl="http://developer.accuweather.com/sites/default/files/"+nightImageUrl+"-s.png";
        Picasso.with(this).load(dayImageIconUrl).into(imageViewDay);
        Picasso.with(this).load(nightImageIconUrl).into(imageViewNight);
        textViewDayText.setText(selectedData.getDayText());
        textViewNightText.setText(selectedData.getNightText());
        textViewMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedData.getMoreDetails())));
            }
        });
        textViewExtendedForeCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedData.getExtendedForeCast())));
            }
        });


    }

    @Override
    public void showWeatherDetail(CityWeatherDetails cityWeatherDetails) {
        setSelectedData(cityWeatherDetails);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveCityMenuItem:
                saveData();
                return true;
            case R.id.setAsCurrentCityMenuItem:
                currentLocationPreferences=getSharedPreferences("locationPreferences", Context.MODE_PRIVATE);
                String savedKey=currentLocationPreferences.getString("key","").trim();
                if(!savedKey.isEmpty()){
                    SharedPreferences.Editor editor=currentLocationPreferences.edit();
                    editor.putString("city",city);
                    editor.putString("country",country);
                    editor.putString("key",cityKey);
                    editor.apply();
                    Toast.makeText(this, "Current City Updated", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else{
                    SharedPreferences.Editor editor=currentLocationPreferences.edit();
                    editor.putString("city",city);
                    editor.putString("country",country);
                    editor.putString("key",cityKey);
                    editor.apply();
                    Toast.makeText(this, "Current City Saved", Toast.LENGTH_SHORT).show();
                    return true;
                }
            case R.id.settingsMenuItem:
                startActivityForResult(new Intent(CityWeatherActivity.this,SettingsActivity.class),MainActivity.REQ_KEY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(MainActivity.RESULT_OK);
    }

    public void saveData(){
        new WeatherAsyncTask(CityWeatherActivity.this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+cityKey+"?apikey=GGGvhnax8EYhgq1ICf6Qo5x2bMLjTBGh");

    }

    @Override
    public void setupWeatherData(Weather weather) {
        currentLocationPreferences=getSharedPreferences("locationPreferences", Context.MODE_PRIVATE);
        currentTemperature=currentLocationPreferences.getString("temperature","");
        if(currentTemperature!=null && !currentTemperature.isEmpty() && currentTemperature.equalsIgnoreCase("Fahrenheit")){
            storeTemperature=weather.getMetricFahrenheit()+"°F";
        }
        else{
            storeTemperature=weather.getMetricCelsius()+"°C";
        }
        updatedTime=weather.getLocalObservationDateTime();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("cities").child(cityKey);
        DatabaseReference cityDatabaseReference= FirebaseDatabase.getInstance().getReference("cities");
        String cityId= databaseReference.getKey();
        CityData cityData=new CityData();
        cityData.setKeyCity(cityKey);
        cityData.setCountry(country);
        cityData.setCityName(city);
        cityData.setTemperature(storeTemperature);
        cityData.setUpdatedTime(updatedTime);
        cityData.setCityId(cityId);
        cityData.setFavorite(false);
        databaseReference.setValue(cityData);

        cityDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Toast.makeText(CityWeatherActivity.this, "City Saved", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                //Toast.makeText(CityWeatherActivity.this, "City Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
