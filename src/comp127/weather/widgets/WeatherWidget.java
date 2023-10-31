package comp127.weather.widgets;

import comp127.weather.api.WeatherData;
import Graphics.GraphicsObject;
import Graphics.Point;

/**
 * A square widget that graphically presents weather conditions.
 */
public interface WeatherWidget {

    /**
     * This widget’s visual interface, which you can add to a CanvasWindow or GraphicsGroup.
     */
    GraphicsObject getGraphics();

    /**
     * Displays the given weather data in the widget, writing over any information the widget was
     * previously displaying.
     */
    void update(WeatherData data);

    /**
     * Called when the mouse moves over the widget.
     *
     * @param position A location in the widget’s local coordinates.
     */
    void onHover(Point position);
}
