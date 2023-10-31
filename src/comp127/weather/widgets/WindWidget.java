package comp127.weather.widgets;

import comp127.weather.api.CurrentConditions;
import comp127.weather.api.WeatherData;
import Graphics.Ellipse;
import Graphics.FontStyle;
import Graphics.GraphicsGroup;
import Graphics.GraphicsObject;
import Graphics.GraphicsText;
import Graphics.Line;
import Graphics.Point;

public class WindWidget implements WeatherWidget {
    private final double size;
    private GraphicsGroup group;

    private Ellipse ellipse;
    private GraphicsText north;
    private GraphicsText speed;
    private GraphicsText labelDirection;
    private Line directionLine;
    private Double angle;
    private Point center, start, end;
   
    public WindWidget (double size) {
        this.size = size;
        center = new Point(size * 0.5, size * 0.5);
        start = new Point(size * 0.5, size * 0.4);
        end = new Point(size * 0.5, size * 0.25);

        group = new GraphicsGroup();

        ellipse = new Ellipse(0, 0, size * 0.5, size * 0.5);
        group.add(ellipse);

        north = new GraphicsText();
        north.setText("N");
        north.setFont(FontStyle.PLAIN, size * 0.03);
        group.add(north);

        speed = new GraphicsText();
        speed.setFont(FontStyle.BOLD, size * 0.04);
        group.add(speed);

        labelDirection = new GraphicsText();
        labelDirection.setFont(FontStyle.BOLD, size * 0.05);
        group.add(labelDirection);
        
        directionLine = new Line(size * 0.5, size * 0.4, size * 0.5, size * 0.25);
        group.add(directionLine);

        updateLayout();
    }

    private void updateLayout() {
        ellipse.setCenter(center);

        labelDirection.setCenter(size * 0.5, size * 0.9);

        speed.setCenter(center);

        north.setCenter(size * 0.5, size * 0.3);

        if (angle != null) {
            directionLine.setStartPosition(start.rotate(angle, center));
            directionLine.setEndPosition(end.rotate(angle, center));
        } else {
            directionLine.setStartPosition(0, 0);
            directionLine.setEndPosition(0, 0);
        }
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    @Override
    public void update(WeatherData data) {
        CurrentConditions currentConditions = data.getCurrentConditions();

        speed.setText(FormattingHelpers.roundOff(currentConditions.getWindSpeed()));

        labelDirection.setText("Wind direction: " + currentConditions.getWindDirectionAsString());

        angle = Math.toRadians(currentConditions.getWindDirectionInDegrees());

        updateLayout();
    }

    @Override
    public void onHover(Point position) {
        // This widget is not interactive, so this method does nothing.
    }
}
