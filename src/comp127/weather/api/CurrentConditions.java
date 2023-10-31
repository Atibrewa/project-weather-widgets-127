package comp127.weather.api;

import net.aksingh.owmjapis.AbstractWeather;
import net.aksingh.owmjapis.CurrentWeather;

import java.util.Date;

/**
 * Information about current weather conditions.
 *
 * @see Conditions
 */
public class CurrentConditions extends Conditions {
    public static final CurrentConditions BLANK = new CurrentConditions();

    private Date sunriseTime;
    private Date sunsetTime;

    private CurrentConditions() {
    }

    /**
     * For fetching from API
     */
    CurrentConditions(CurrentWeather rawCurrentConditions) {
        if (rawCurrentConditions.hasCloudsInstance()) {
            cloudCoverage = nullIfNaN(rawCurrentConditions.getCloudsInstance().getPercentageOfClouds());
        }
        if (rawCurrentConditions.hasMainInstance()) {
            temperature = nullIfNaN(rawCurrentConditions.getMainInstance().getTemperature());
            pressure = nullIfNaN(rawCurrentConditions.getMainInstance().getPressure());
            humidity = nullIfNaN(rawCurrentConditions.getMainInstance().getHumidity());
        }
        if (rawCurrentConditions.hasWindInstance()) {
            windSpeed = nullIfNaN(rawCurrentConditions.getWindInstance().getWindSpeed());
            windDirectionInDegrees = nullIfNaN(rawCurrentConditions.getWindInstance().getWindDegree());
        }
        if (rawCurrentConditions.hasSysInstance()) {
            sunriseTime = rawCurrentConditions.getSysInstance().getSunriseTime();
            sunsetTime = rawCurrentConditions.getSysInstance().getSunsetTime();
        }
        if (rawCurrentConditions.hasWeatherInstance()
                && rawCurrentConditions.getWeatherCount() > 0
                && rawCurrentConditions.getWeatherInstance(0) != null) {
            AbstractWeather.Weather weather = rawCurrentConditions.getWeatherInstance(0);
            if (weather.hasWeatherDescription()) {
                weatherDescription = nullIfBlank(weather.getWeatherDescription());
                weatherIconFile = nullIfBlank(weather.getWeatherIconName());
            }
        }
    }

    /**
     * For generating test data
     */
    CurrentConditions(double temperature, double humidity, double pressure, double cloudCoverage,
                      double windSpeed, double windDirectionInDegrees,
                      String weatherIconFile, String weatherDescription, Date sunriseTime, Date sunsetTime) {
        this.cloudCoverage = cloudCoverage;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirectionInDegrees = windDirectionInDegrees;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.weatherDescription = weatherDescription;
        this.weatherIconFile = weatherIconFile;
    }

    /**
     * The instant in time when sunrise will occur today.
     */
    public Date getSunriseTime() {
        return sunriseTime;
    }

    /**
     * The instant in time when sunset will occur today.
     */
    public Date getSunsetTime() {
        return sunsetTime;
    }

    @Override
    public String toString() {
        return "CurrentConditions{"
            + "cloudCoverage=" + cloudCoverage
            + ", temperature=" + temperature
            + ", pressure=" + pressure
            + ", humidity=" + humidity
            + ", windSpeed=" + windSpeed
            + ", windDirectionInDegrees=" + windDirectionInDegrees
            + ", sunriseTime=" + sunriseTime
            + ", sunsetTime=" + sunsetTime
            + ", currentWeather='" + weatherDescription + '\''
            + ", weatherIconFile='" + weatherIconFile + '\''
            + '}';
    }
}
