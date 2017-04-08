package dhiraj.com.weatherapp;

import java.io.Serializable;

/**
 * Created by dhira on 07-04-2017.
 */

public class CityWeatherDetails implements Serializable {
    String headline;
    String forecastDate;
    String temperatureMin;
    String temperatureMax;
    String dayImage;
    String nightImage;
    String moreDetails;
    String extendedForeCast;
    String dayText;

    public String getNightText() {
        return nightText;
    }

    public void setNightText(String nightText) {
        this.nightText = nightText;
    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    String nightText;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(String temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public String getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(String temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public String getDayImage() {
        return dayImage;
    }

    public void setDayImage(String dayImage) {
        this.dayImage = dayImage;
    }

    public String getNightImage() {
        return nightImage;
    }

    public void setNightImage(String nightImage) {
        this.nightImage = nightImage;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }

    public String getExtendedForeCast() {
        return extendedForeCast;
    }

    public void setExtendedForeCast(String extendedForeCast) {
        this.extendedForeCast = extendedForeCast;
    }
}

