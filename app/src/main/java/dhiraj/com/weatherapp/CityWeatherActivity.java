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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CityWeatherActivity extends AppCompatActivity implements CurrentCityAsyncTask.IData,CityWeatherDetailsAsyncTask.ICityWeatherDetailsData,GridRecyclerAdapter.IGridListener {
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
    String country, city, dayImageUrl, nightImageUrl, outputDateString;
    GridRecyclerAdapter gridRecyclerAdapter;
    LinearLayoutManager layoutManager;
    Date inputDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        getSupportActionBar().setTitle("Weather App");
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
        country=getIntent().getStringExtra(MainActivity.COUNTRY_KEY);
        progressBarCityWeather.setVisibility(View.VISIBLE);
        relativeLayoutCityWeatherContainer.setVisibility(View.INVISIBLE);
        new CurrentCityAsyncTask(this).execute("http://dataservice.accuweather.com/locations/v1/"+country+"/search?apikey=GGGvhnax8EYhgq1ICf6Qo5x2bMLjTBGh&q="+city);

    }

    @Override
    public void setupData(String key) {
        if(key==null)
            Toast.makeText(this, "City Not Found", Toast.LENGTH_SHORT).show();
        else{
            new CityWeatherDetailsAsyncTask(this).execute("http://dataservice.accuweather.com/forecasts/v1/daily/5day/"+key+"?apikey=GGGvhnax8EYhgq1ICf6Qo5x2bMLjTBGh");
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
}
