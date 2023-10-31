package comp127.weather.api;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Generates conditions for use in tests. The generated conditions are pseudorandom, but will always
 * be the same given the same seed.
 */
public class WeatherDataFixtures {
    public static WeatherData generateWeatherData(int seed) {
        if(seed == 0) {
            return new WeatherData(null, CurrentConditions.BLANK, List.of(ForecastConditions.BLANK));
        }
        return new WeatherData(
            generateCityName(seed),
            generateCurrentConditions(seed),
            IntStream.range(1, Math.abs(seed) * 17 % 39)
                .mapToObj((offset) ->
                    generateForecastConditions((offset + 1) * 3, seed + offset * 127))
                .collect(toList()));
    }

    private static CurrentConditions generateCurrentConditions(int seed) {
        return new CurrentConditions(
            generateDouble(seed, "temperature", -60, 120),
            generateDouble(seed, "humidity", 0, 100),
            generateDouble(seed, "pressure", 26, 36),
            generateDouble(seed, "cloudCoverage", 0, 100),
            generateDouble(seed, "windSpeed", 0, 150),
            generateDouble(seed, "windDirectionInDegrees", 0, 360),
            generateWeatherIcon(seed),
            generateCondition(seed),
            generateTimeOfDay(seed, "sunrise", 4, 8),
            generateTimeOfDay(seed, "sunset", 16, 20));
    }

    private static ForecastConditions generateForecastConditions(int hoursFromNow, int seed) {
        LocalDateTime randomTimeOfDay = LocalDateTime.now().plusHours(hoursFromNow);
        Date predictionTime = toDate(randomTimeOfDay);

        double temperature = generateDouble(seed, "temperature", -40, 100);
        return new ForecastConditions(
            predictionTime,
            temperature,
            temperature - 2 * Math.sqrt(hoursFromNow) - generateDouble(seed, "minTemperature", 1, 20),
            temperature + 2 * Math.sqrt(hoursFromNow) + generateDouble(seed, "maxTemperature", 1, 20),
            generateDouble(seed, "humidity", 0, 100),
            generateDouble(seed, "pressure", 26, 36),
            generateDouble(seed, "cloudCoverage", 0, 100),
            generateDouble(seed, "windSpeed", 0, 150),
            generateDouble(seed, "windDirectionInDegrees", 0, 360),
            generateCondition(seed),
            generateWeatherIcon(seed));
    }

    // Helpers to generate stable pseudorandom values so the same seed always generates the same
    // test data

    private static int generateInt(int seed, String property, int min, int maxInclusive) {
        return new Random(seed ^ property.hashCode()).nextInt(maxInclusive - min + 1) + min;
    }

    private static double generateDouble(int seed, String property, double min, double maxInclusive) {
        return generateInt(seed, property, 0, 1_000_000) / 1_000_000.0 * (maxInclusive - min) + min;
    }
    
    private static <T> T generateFromOptions(int seed, String property, List<T> options) {
        return options.get(generateInt(seed, property, 0, options.size() - 1));
    }

    private static String generateCityName(int seed) {
        return generateFromOptions(seed, "cityName", List.of(
            "Hill Valley",
            "Emerald City",
            "Mos Eisley",
            "Toontown",
            "Sunnydale",
            "Neptune",
            "Springfield",
            "Hogsmeade",
            "Brigadoon",
            "Middlemarch",
            "Wortlethorpe"));
    }

    private static Date generateTimeOfDay(int seed, String property, int minHour, int maxHour) {
        // Hat tip to https://stackoverflow.com/a/6850919/239816
        LocalDate now = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(now, LocalTime.MIDNIGHT);
        LocalDateTime randomTimeOfDay = todayMidnight.plusMinutes(
            generateInt(seed, property, minHour * 60, maxHour * 60));
        return toDate(randomTimeOfDay);
    }

    // Surely there's a better way to do this? -PPC
    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(Instant.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault())));
    }

    private static String generateCondition(int seed) {
        return generateFromOptions(seed, "currentWeather", List.of(
            "Sunnish",
            "Partly clowny",
            "Blue Blue Electric Blue",
            "Rainbows and unicorns",
            "Clear with UFO sightings",
            "Punderstorm",
            "Heavy Thunderdome",
            "Sailors take warning",
            "Ineluctible drizzle",
            "Volcanic asps",
            "Apocalyptic cold",
            "Cloudy with a chance of meatballs"));
    }

    private static String generateWeatherIcon(int seed) {
        return generateFromOptions(seed, "weatherIcon",
            Stream.of("01", "02", "03", "04", "09", "10", "11", "13", "50")
                .flatMap((num) -> Stream.of(num + "d", num + "n"))
                .collect(toList()));
    }
}
