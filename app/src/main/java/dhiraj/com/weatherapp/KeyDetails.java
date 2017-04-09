package dhiraj.com.weatherapp;

import java.io.Serializable;

/**
 * Created by dhira on 08-04-2017.
 */

public class KeyDetails implements Serializable {
    String country, city,key;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

