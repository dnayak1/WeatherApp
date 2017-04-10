package dhiraj.com.weatherapp;

import java.io.Serializable;

/**
 * Created by dhira on 09-04-2017.
 */

public class CityData implements Serializable {
    String  keyCity,cityName,country,temperature,cityId,updatedTime;
    Boolean favorite;

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getKeyCity() {
        return keyCity;
    }

    public void setKeyCity(String keyCity) {
        this.keyCity = keyCity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
