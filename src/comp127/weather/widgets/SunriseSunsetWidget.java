package comp127.weather.widgets;

import comp127.weather.api.CurrentConditions;
import comp127.weather.api.WeatherData;
import Graphics.FontStyle;
import Graphics.GraphicsGroup;
import Graphics.GraphicsObject;
import Graphics.GraphicsText;
import Graphics.Image;
import Graphics.Point;

public class SunriseSunsetWidget implements WeatherWidget{
    private final double size;
    private GraphicsGroup group;

    private GraphicsText riseTime;
    private GraphicsText setTime;
    private Image icon;

    private double direction = -1;

    public SunriseSunsetWidget(double size) {
        this.size = size;

        group = new GraphicsGroup();

        icon = new Image(0, 0);
        icon.setImagePath("condition-icons/01d.png");
        icon.setMaxWidth(size * 0.3);
        // icon.setScale(size/icon.getImageWidth()/5);
        group.add(icon);

        riseTime = new GraphicsText();
        riseTime.setFont(FontStyle.PLAIN, size * 0.05);
        group.add(riseTime);

        setTime = new GraphicsText();
        setTime.setFont(FontStyle.PLAIN, size * 0.05);
        group.add(setTime);

        updateLayout();
        
    }

    // This method animates the widget - the sun "rises" and "sets"
    private void riseAndShine() {
        double maxY = size * 0.7;
        double minY = size * 0.2;
        
        if (icon.getY() < minY) {
            direction = 1;
        } else if (icon.getY() > maxY) {
            direction = -1;
        }
        double newY = icon.getY() + (direction * size / 300);
        icon.setY(newY);
        
    }

    private void updateLayout() {
        riseTime.setCenter(size * 0.5, size * 0.2);

        setTime.setCenter(size * 0.5, size * 0.8);

        icon.setCenter(size * 0.5, size * 0.7);

    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    @Override
    public void update(WeatherData data) {
        CurrentConditions currentConditions = data.getCurrentConditions();

        riseTime.setText("Sunrise: " + FormattingHelpers.time(currentConditions.getSunriseTime()));

        setTime.setText("Sunset: " + FormattingHelpers.time(currentConditions.getSunsetTime()));
        
        updateLayout();        
    }

    @Override
    public void onHover(Point position) {
        riseAndShine();
    }
}
