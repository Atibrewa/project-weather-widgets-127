package comp127.weather.widgets;

import comp127.weather.api.ForecastConditions;
import Graphics.Rectangle;

import java.awt.Color;


/**
 * A rectangle that represents a specific forecast. Useful for making an interface that allows the
 * user to select one of many future forecasts.
 */
public class ForecastBox extends Rectangle {

    // This holds the information about a specific forecast
    private ForecastConditions forecast;

    /**
     * Creates a box that references the given forecast, and appears on the screen at the given
     * coordinates.
     */
    public ForecastBox(ForecastConditions forecast, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.forecast = forecast;

        setStrokeWidth(Math.rint((width + height) / 40 + 1) * 0.5);
        setActive(false);
    }

    /**
     * Changes the color of the box to indicate whether it is active. The meaning of “active” is up
     * to each widget that uses this class.
     */
    public void setActive(boolean active) {
        setFillColor(active
            ? new Color(0x3ba634)
            : new Color(0xD9D9D9));
    }

    /**
     * The forecast data this box represents.
     */
    public ForecastConditions getForecast() {
        return forecast;
    }
}
