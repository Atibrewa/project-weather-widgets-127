package comp127.weather.api;

import net.aksingh.owmjapis.AbstractWeather;
import net.aksingh.owmjapis.HourlyForecast;

import java.util.Date;

/**
 * A prediction about weather conditions at some point in time. (That point in time is presumably in
 * the future — though as Neils Bohr remarked, prediction is hard, especially of the future.)
 */
public class ForecastConditions extends Conditions {
    public static final ForecastConditions BLANK = new ForecastConditions();

    private Date predictionTime;
    private Double minTemperature;
    private Double maxTemperature;

    private ForecastConditions() {
    }

    /**
     * For fetching from API
     */
    ForecastConditions(HourlyForecast.Forecast rawForecast) {
        predictionTime = rawForecast.getDateTime();
        if (rawForecast.hasCloudsInstance()) {
            cloudCoverage = nullIfNaN(rawForecast.getCloudsInstance().getPercentageOfClouds());
        }
        if (rawForecast.hasMainInstance()) {
            temperature = nullIfNaN(rawForecast.getMainInstance().getTemperature());
            minTemperature = nullIfNaN(rawForecast.getMainInstance().getMinTemperature());
            maxTemperature = nullIfNaN(rawForecast.getMainInstance().getMaxTemperature());
            pressure = nullIfNaN(rawForecast.getMainInstance().getPressure());
            humidity = nullIfNaN(rawForecast.getMainInstance().getHumidity());
        }
        if (rawForecast.hasWindInstance() && rawForecast.getWindInstance().hasWindSpeed()) {
            windSpeed = nullIfNaN(rawForecast.getWindInstance().getWindSpeed());
            windDirectionInDegrees = nullIfNaN(rawForecast.getWindInstance().getWindDegree());
        }
        if (rawForecast.hasWeatherInstance() && rawForecast.getWeatherCount() > 0 && rawForecast.getWeatherInstance(0) != null) {
            AbstractWeather.Weather weather = rawForecast.getWeatherInstance(0);
            if (weather.hasWeatherDescription()) {
                weatherDescription = nullIfBlank(weather.getWeatherDescription());
                weatherIconFile = nullIfBlank(weather.getWeatherIconName());
            }
        }
    }

    /**
     * For generating test data
     */
    ForecastConditions(Date predictionTime, double temperature, double minTemperature, double maxTemperature,
                       double humidity, double pressure, double cloudCoverage,
                       double windSpeed, double windDirectionInDegrees,
                       String weatherDescription, String weatherIconFile) {
        this.predictionTime = predictionTime;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.cloudCoverage = cloudCoverage;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirectionInDegrees = windDirectionInDegrees;
        this.weatherDescription = weatherDescription;
        this.weatherIconFile = weatherIconFile;
    }

    /**
     * Returns the moment in time that this prediction is for.
     */
    public Date getPredictionTime() {
        return predictionTime;
    }

    /**
     * Gets the predicted minimum temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit).
     * The min/max range around getTemperature() reflects uncertainty in the forecast.
     */
    public Double getMinTemperature() {
        return minTemperature;
    }

    /**
     * Gets the predicted maximum temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * The min/max range around getTemperature() reflects uncertainty in the forecast.
     */
    public Double getMaxTemperature() {
        return maxTemperature;
    }

    void addUncertainty(double delta) {
        if (minTemperature == null) {
            minTemperature = temperature;
        }
        if (maxTemperature == null) {
            maxTemperature = temperature;
        }
        minTemperature -= delta;
        maxTemperature += delta;
    }

    @Override
    public String toString() {
        return "ForecastConditions{"
            + "predictionTime=" + predictionTime
            + ", cloudCoverage=" + cloudCoverage
            + ", temperature=" + temperature
            + ", minTemperature=" + minTemperature
            + ", maxTemperature=" + maxTemperature
            + ", pressure=" + pressure
            + ", humidity=" + humidity
            + ", windSpeed=" + windSpeed
            + ", windDirectionInDegrees=" + windDirectionInDegrees
            + ", weatherDescription='" + weatherDescription + '\''
            + ", weatherIconFile='" + weatherIconFile + '\''
            + '}';
    }
}
