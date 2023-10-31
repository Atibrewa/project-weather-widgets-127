package comp127.weather.widgets;

import comp127.weather.api.WeatherData;
import comp127.weather.api.WeatherDataFixtures;
import Graphics.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Create multiple instances of a single widget at a variety of different sizes, and feeds the
 * instances varied test conditions.
 */
public class SingleWidgetTest {

    private static WeatherWidget makeWidget(double size) {
        // Replace TemperatureWidget with your own widget to test
        //         ⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇
        return new SunriseSunsetWidget(size);
    }

    // –––––– Test code ––––––

    private final GraphicsGroup
        widgetLayer = new GraphicsGroup(),
        borderLayer = new GraphicsGroup(),
        dimensionLabelLayer = new GraphicsGroup();
    private final List<WeatherWidget> widgets = new ArrayList<>();
    private final GraphicsText seedLabel, errorLabel;
    private int testDataSeed = 0;

    private SingleWidgetTest() {
        CanvasWindow canvas = new CanvasWindow("Weather widget test", 800, 600);
        canvas.add(borderLayer);
        canvas.add(widgetLayer);
        canvas.add(dimensionLabelLayer);

        addTestWidget(0, 0, 500);
        addTestWidget(500, 0, 300);
        addTestWidget(500, 300, 200);
        addTestWidget(700, 300, 100);

        errorLabel = new GraphicsText("", 10, 540);
        errorLabel.setFontStyle(FontStyle.BOLD);
        errorLabel.setFillColor(new Color(0xA80700));
        canvas.add(errorLabel);

        seedLabel = new GraphicsText("Showing state before update() is called", 10, 580);
        canvas.add(seedLabel);

        GraphicsText helpText = new GraphicsText("Click anywhere to generate new test data", 0, 580);
        helpText.setX(canvas.getWidth() - helpText.getWidth() - 10);
        helpText.setFillColor(Color.BLUE);
        canvas.add(helpText);

        canvas.onClick((event) -> {
            seedLabel.setText("Showing testDataSeed = " + testDataSeed
                + (testDataSeed == 0 ? " (state when data is missing from API)" : ""));
            WeatherData data = WeatherDataFixtures.generateWeatherData(testDataSeed);
            System.out.println(data);

            for (WeatherWidget widget : widgets) {
                try {
                    widget.update(data);
                } catch(Exception e) {
                    System.out.println();
                    e.printStackTrace();
                    errorLabel.setText(
                        e.getClass().getSimpleName()
                        + " while updating widget. See console for details."
                        + " (with testDataSeed = " + testDataSeed + ")");
                }
            }
            testDataSeed++;
        });

        canvas.onMouseMove((event) -> {
            for (WeatherWidget widget : widgets) {
                if (widget.getGraphics().isInBounds(event.getPosition())) {
                    widget.onHover(event.getPosition());
                }
            }
        });
    }

    private void addTestWidget(double x, double y, double size) {
        WeatherWidget widget = makeWidget(size);
        widgetLayer.add(widget.getGraphics(), x, y);
        widgets.add(widget);

        Rectangle border = new Rectangle(x, y, size, size);
        border.setStrokeColor(new Color(0x40000000, true));
        border.setStrokeWidth(4);
        borderLayer.add(border);

        GraphicsText dimensionsLabel = new GraphicsText(Math.round(size) + "×" + Math.round(size), x + 4, y + size - 5);
        dimensionsLabel.setFontSize(7);
        dimensionsLabel.setFillColor(new Color(0x8EC1FF));
        dimensionLabelLayer.add(dimensionsLabel);
    }

    public static void main(String[] args) {
        new SingleWidgetTest();
    }
}
