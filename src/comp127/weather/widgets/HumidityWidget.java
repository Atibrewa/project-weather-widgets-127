package comp127.weather.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import comp127.weather.api.CurrentConditions;
import comp127.weather.api.WeatherData;
import Graphics.FontStyle;
import Graphics.GraphicsGroup;
import Graphics.GraphicsObject;
import Graphics.GraphicsText;
import Graphics.Image;
import Graphics.Point;

public class HumidityWidget implements WeatherWidget{
    private final double size;
    private GraphicsGroup group;
    private Random rand;

    private List<Image> droplets;

    private Double humidity;

    private GraphicsText humidityLabel;

    public HumidityWidget(Double size) {
        this.size = size;

        droplets = new ArrayList<>();

        rand = new Random();
        group = new GraphicsGroup();

        humidity = null;

        humidityLabel = new GraphicsText();
        humidityLabel.setFont(FontStyle.BOLD, size * 0.08);

    }

    private void updateLayout() {
        addDroplets();

        humidityLabel.setCenter(size * 0.5, size * 0.5);
        group.add(humidityLabel);

    }

    private void addDroplets() {
        if (droplets != null) {
            droplets.removeAll(droplets);
            group.removeAll();
        }
        
        if (humidity != null) {
            for (int i = 0; i < humidity; i++) {
                makeDrop();
            }
        }
    }

    private void makeDrop() {
        Image droplet = new Image(0, 0);
        droplet.setImagePath("condition-icons/drop.png");

        int height = rand.nextInt((int)(size * 0.05)) + 5; // +5 to make sure the drops aren't too small
        droplet.setMaxHeight(height);

        int x = rand.nextInt((int)(Math.round(size) - droplet.getWidth()));
        int y = rand.nextInt((int)(Math.round(size) - droplet.getHeight()));
        droplet.setPosition(x, y);
        
        group.add(droplet);
        droplets.add(droplet);
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    @Override
    public void update(WeatherData data) {
        CurrentConditions currentConditions = data.getCurrentConditions();

        humidity = currentConditions.getHumidity();

        humidityLabel.setText("Humidity: " + FormattingHelpers.roundOff(humidity) + "%");

        updateLayout();
    }

    @Override
    public void onHover(Point position) {
        // This widget is not interactive, so this method does nothing.
    }
}
