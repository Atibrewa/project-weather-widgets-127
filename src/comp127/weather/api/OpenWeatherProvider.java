package comp127.weather.api;

import net.aksingh.owmjapis.OpenWeatherMap;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Fetches weather data from the OpenWeather API.
 *
 * To create one of these objects you need to have an OpenWeather API key. Instructions for how to
 * get one of these are in the instructions for this assignment. You also need to specify a location.
 * You can either do that through latitude and longitude OR city name and country code. The starter
 * code specifies Macalester as the location, but you can change that.
 *
 * This class is a custom wrapper over aksingh’s OpenWeatherMap class, providing a simpler and
 * tidier API for the weather widgets homework.
 */
public class OpenWeatherProvider {

    private static final String PROPERTIES_FILE = "/weather-display.properties";

    private static String getApiKey() {
        Properties props = new Properties();
        try {
            props.load(OpenWeatherProvider.class.getResourceAsStream(PROPERTIES_FILE));
        } catch (Exception e) {
            System.err.print("Unable to load " + PROPERTIES_FILE + ": ");
            e.printStackTrace();
        }
        String result = props.getProperty("api.key");
        if (result == null || result.isBlank()) {
            System.err.println();
            System.err.println("ERROR: No api.key in res" + PROPERTIES_FILE);
            System.err.println("       Please see README to configure the API key");
            System.err.println();
            System.exit(1);
        }
        return result;
    }

    // There are two (well 4, but I'm not implementing all that) ways to specify where where you want
    // weather for. if cityName & stateName are non-null we will use those for every api call, otherwise we
    // will use lat/long.
    private final String cityName, countryCode;
    private final Double lat, lng;

    private final OpenWeatherMap openWeather;

    private static final ExecutorService requestQueue = Executors.newSingleThreadExecutor();

    /**
     * Creates a provider that will return weather for a given city.
     *
     * @param cityName The name of the city, including the state/province.
     * @param countryCode The two-letter country code for the country you want to get weather from
     */
    public OpenWeatherProvider(String cityName, String countryCode) {
        openWeather = new OpenWeatherMap(getApiKey());
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.lat = this.lng = null;
        setUnitsImperial();
    }

    /**
     * Creates a provider that will return weather for an arbitrary location.
     */
    public OpenWeatherProvider(double latitude, double longitude) {
        openWeather = new OpenWeatherMap(getApiKey());
        this.lat = latitude;
        this.lng = longitude;
        this.cityName = this.countryCode = null;
        setUnitsImperial();
    }

    /**
     * Set the interface to use fahrenheit and miles
     */
    public void setUnitsImperial() {
        openWeather.setUnits(OpenWeatherMap.Units.IMPERIAL);
    }

    /**
     * Set the interface to use celsius and meters
     */
    public void setUnitsMetric() {
        openWeather.setUnits(OpenWeatherMap.Units.METRIC);
    }

    /**
     * Fetches up-to-date weather data from the server. Runs completionCallback if the
     * request succeeds.
     */
    public void fetchWeather(Consumer<WeatherData> completionCallback) {
        requestQueue.submit(() -> {
            try {
                WeatherData result = new WeatherData(
                    fetch("current conditions",
                        openWeather::currentWeatherByCityName,
                        openWeather::currentWeatherByCoordinates),
                    fetch("hourly forecast",
                        openWeather::hourlyForecastByCityName,
                        openWeather::hourlyForecastByCoordinates));

                System.out.println("Got weather data: " + result);

                SwingUtilities.invokeLater(() ->
                    completionCallback.accept(result));
            } catch (WeatherException e) {
                System.out.println("Unable to fetch weather: " + e);
            }
        });
    }

    private <T> T fetch(
            String requestName,
            APIRequest<String, String, T> cityRequest,
            APIRequest<Float, Float, T> coordinateRequest)
        throws WeatherException {

        System.out.println("Updating " + requestName + " ...");
        T result;
        try {
            if (usingCityName()) {
                result = cityRequest.request(cityName, countryCode);
            } else {
                result = coordinateRequest.request(lat.floatValue(), lng.floatValue());
            }
        } catch (IOException ex) {
            throw new WeatherException("Weather API request failed", ex);
        }
        if (result == null) {
            throw new WeatherException("Could not parse weather API response");
        }
        System.out.println("Done.");
        return result;
    }

    /**
     * Returns true if we should use the city name to fetch weather info.
     */
    private boolean usingCityName() {
        if (cityName != null && countryCode != null) {
            return true;
        }
        if (lat != null && lng != null) {
            return false;
        }
        throw new IllegalStateException("Insufficient location information");
    }

    private interface APIRequest<Arg0, Arg1, Data> {
        Data request(Arg0 arg0, Arg1 arg1) throws IOException;
    }

    public static void main(String[] args) {
        new OpenWeatherProvider(44.9, -93.0)
            .fetchWeather(System.out::println);

        System.out.println();

        new OpenWeatherProvider("Fort Collins, CO", "US")
            .fetchWeather(System.out::println);
    }
}
