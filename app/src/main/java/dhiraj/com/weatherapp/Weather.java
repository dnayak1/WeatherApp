package dhiraj.com.weatherapp;

import java.io.Serializable;

/**
 * Created by dhira on 06-04-2017.
 */

public class Weather implements Serializable {
    String localObservationDateTime, weatherText, weatherIcon, metricCelsius, metricFahrenheit;

    public String getLocalObservationDateTime() {
        return localObservationDateTime;
    }

    public void setLocalObservationDateTime(String localObservationDateTime) {
        this.localObservationDateTime = localObservationDateTime;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getMetricCelsius() {
        return metricCelsius;
    }

    public void setMetricCelsius(String metricCelsius) {
        this.metricCelsius = metricCelsius;
    }

    public String getMetricFahrenheit() {
        return metricFahrenheit;
    }

    public void setMetricFahrenheit(String metricFahrenheit) {
        this.metricFahrenheit = metricFahrenheit;
    }
}
