package comp127.weather.widgets;

import comp127.weather.api.ForecastConditions;
import comp127.weather.api.WeatherData;
import Graphics.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ForecastWidget implements WeatherWidget {

    private final double size;
    private GraphicsGroup group;

    private GraphicsText date;
    private GraphicsText time;
    private GraphicsText currentTemp;
    private GraphicsText highLowTemp;
    private GraphicsText description;
    private Image icon;

    private GraphicsGroup boxGroup;  // Holds all the ForecastBox objects

    private List<ForecastBox> boxes = new ArrayList<>();

    public ForecastWidget(double size) {
        this.size = size;

        group = new GraphicsGroup();

        icon = new Image(0, 0);
        group.add(icon);

        date = new GraphicsText();
        date.setFont(FontStyle.BOLD, size * 0.07);
        group.add(date);

        time = new GraphicsText();
        time.setFont(FontStyle.BOLD, size * 0.07);
        group.add(time);

        currentTemp = new GraphicsText();
        currentTemp.setFont(FontStyle.BOLD, size * 0.1);
        group.add(currentTemp);

        highLowTemp = new GraphicsText();
        highLowTemp.setFont(FontStyle.PLAIN, size * 0.05);
        highLowTemp.setFillColor(Color.GRAY);
        group.add(highLowTemp);

        description = new GraphicsText();
        description.setFont(FontStyle.PLAIN, size * 0.05);
        group.add(description);

        boxGroup = new GraphicsGroup();
        group.add(boxGroup);

        updateLayout();
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    @Override
    public void update(WeatherData data) {
        boxGroup.removeAll();
        boxes.clear();  // Remove all the old ForecastBoxes from our list

        double x = size * 0.06;
        double y = size * 0.9;

        for (ForecastConditions forecast : data.getForecasts()) {
            ForecastBox forecastBox = new ForecastBox(forecast, 0, 0, size * 0.03, size * 0.04);
            forecastBox.setCenter(x, y);
            boxGroup.add(forecastBox);
            boxes.add(forecastBox);

            x += size * 0.04;
            if (x > size * 0.94) {
                x = size * 0.06;
                y += size * 0.05;
            }
        }

        selectForecast(boxes.get(0));
    }

    private void selectForecast(ForecastBox box) {
        for (ForecastBox forecastBox : boxes) {
            if (forecastBox == box) {
                forecastBox.setActive(true);
            } else {
                forecastBox.setActive(false);
            }
        }

        ForecastConditions forecast = box.getForecast();

        icon.setImagePath(forecast.getWeatherIcon());
        icon.setScale(size/icon.getImageWidth()/4);

        date.setText(FormattingHelpers.dayDate(forecast.getPredictionTime()));

        time.setText(FormattingHelpers.time(forecast.getPredictionTime()));

        currentTemp.setText(FormattingHelpers.roundOff(forecast.getTemperature())+ "\u2109");

        highLowTemp.setText(FormattingHelpers.roundOff(forecast.getMinTemperature())+ "\u2109" 
                    + " | " + FormattingHelpers.roundOff(forecast.getMaxTemperature())+ "\u2109");

        description.setText(forecast.getWeatherDescription());

        updateLayout();
    }

    private void updateLayout() {
        icon.setCenter(size * 0.5, size * 0.3);

        date.setPosition(size * 0.01, size * 0.08);

        time.setPosition(size * 0.7, size * 0.08);

        currentTemp.setCenter(size * 0.5, size * 0.5);

        highLowTemp.setCenter(size * 0.5, size * 0.6);

        description.setCenter(size * 0.5, size * 0.68);
    }

    /**
     * Given a position in the widget, this returns the ForecastBox at that position if one exists
     *
     * @param location pos to check
     * @return null if not over a forecast box
     */
    private ForecastBox getBoxAt(Point location) {
        GraphicsObject obj = group.getElementAt(location);
        if (obj instanceof ForecastBox) {
            return (ForecastBox) obj;
        }
        return null;
    }

    /**
     * Updates the currently displayed forecast information as the mouse moves over the widget.
     * If there is not a ForecastBox at that position, the display does not change.
     */
    @Override
    public void onHover(Point position) {
        GraphicsObject obj = group.getElementAt(position);
            if (obj instanceof ForecastBox) {
                selectForecast(getBoxAt(position));
            }
    }
}
