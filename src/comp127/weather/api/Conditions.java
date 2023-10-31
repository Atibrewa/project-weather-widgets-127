package comp127.weather.api;

import net.aksingh.owmjapis.Tools;

/**
 * Weather information shared by both current conditions and future forecasts.
 *
 * Note that almost all fields may be null. In particular, note that the numerical values use Double
 * (a nullable object type), and not double (the non-nullable primitive type).
 */
public abstract class Conditions {
    private static final Tools weatherUtils = new Tools();

    protected Double cloudCoverage;
    protected Double temperature;
    protected Double pressure;
    protected Double humidity;
    protected Double windSpeed;
    protected Double windDirectionInDegrees;
    protected String weatherDescription;
    protected String weatherIconFile;

    protected static Double nullIfNaN(double value) {
        return Double.isNaN(value) ? null : value;
    }

    protected static String nullIfBlank(String str) {
        return (str == null || str.isBlank()) ? null : str;
    }

    /**
     * The current cloud coverage as a percent from 0 to 100%.
     * @return (returns 0 % in case of error)
     */
    public Double getCloudCoverage() {
        return cloudCoverage;
    }

    /**
     * The temperature, in whatever units the OpenWeatherProvider was set to when you requested the
     * data (defaults to Fahrenheit).
     */
    public Double getTemperature() {
        return temperature;
    }

    /**
     * The atmospheric pressure.
     */
    public Double getPressure() {
        return pressure;
    }

    /**
     * The relative humidity, as a percent.
     */
    public Double getHumidity() {
        return humidity;
    }

    /**
     * The speed of the wind. Units are miles/second or meters/second, depending on your choice of
     * units in OpenWeatherProvider.
     */
    public Double getWindSpeed() {
        return windSpeed;
    }

    /**
     * A textual description of the compass direction of the wind, such as "S" or "NNW".
     */
    public String getWindDirectionAsString() {
        if (windDirectionInDegrees != null && windDirectionInDegrees >= 0 && windDirectionInDegrees <= 360) {
            return weatherUtils.convertDegree2Direction(windDirectionInDegrees.floatValue());
        } else {
            return null;
        }
    }

    /**
     * The wind direction, in degrees clockwise from north.
     */
    public Double getWindDirectionInDegrees() {
        return windDirectionInDegrees;
    }

    /**
     * A short description of the weather. If there are multiple weather conditions at once, this
     * only returns the primary weather condition.
     */
    public String getWeatherDescription() {
        return weatherDescription;
    }

    /**
     * Returns the resource path for an image representing the current weather. Never returns null;
     * if the weather conditions are missing or unknown, returns an "unknown" icon.
     */
    public String getWeatherIcon() {
        return "condition-icons/" + (weatherIconFile != null ? weatherIconFile : "unknown") + ".png";
    }
}
