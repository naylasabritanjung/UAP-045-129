import javax.swing.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WeatherCheckController {
    private final WeatherCheckView check;

    public WeatherCheckController(WeatherCheckView check) {
        this.check = check;

        check.getCari().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String locationName = check.getTextField().getText();
                JSONArray locationData = FetchApi.getLocationData(locationName);

                if (locationData != null && locationData.size() > 0) {
                    JSONObject location = (JSONObject) locationData.get(0);
                    String cityName = (String) location.get("name");

                    double latitude = (double) location.get("latitude");
                    double longitude = (double) location.get("longitude");

                    JSONObject weatherData = FetchApi.getWeatherData(cityName);

                    if (weatherData != null) {
                        String message = String.format("Location: %s\nLatitude: %.4f\nLongitude: %.4f\nTemperature: %.2f°C\nWeather Condition: %s\nHumidity: %d%%\nWindspeed: %.2f m/s",
                                cityName,
                                latitude,
                                longitude,
                                weatherData.get("temperature"),
                                weatherData.get("weather_condition"),
                                weatherData.get("humidity"),
                                weatherData.get("windspeed"));

                        // Cetak pesan cuaca ke konsol
                        System.out.println(message);

                        // Tutup frame saat ini
                        JFrame currentFrame = (JFrame) check.getTopLevelAncestor();
                        if (currentFrame != null) {
                            currentFrame.dispose();
                        }

                        // Create a new DisplayModel instance
                        DisplayModel model = new DisplayModel();

                        // Create a new WeatherDisplayView instance with the model
                        WeatherDisplayView displayView = new WeatherDisplayView(model);

                        // Create a new JFrame to display the WeatherDisplayView
                        JFrame weatherFrame = new JFrame("Weather Details");
                        weatherFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        weatherFrame.setSize(900, 700);

                        // Add WeatherDisplayView to the frame
                        weatherFrame.add(displayView);

                        // Create JSONObject to pass weather data
                        JSONObject weatherInfo = new JSONObject();
                        weatherInfo.put("location_name", cityName);
                        weatherInfo.put("latitude", latitude);
                        weatherInfo.put("longitude", longitude);
                        weatherInfo.put("temperature", weatherData.get("temperature"));
                        weatherInfo.put("weather_condition", weatherData.get("weather_condition"));
                        weatherInfo.put("humidity", weatherData.get("humidity"));
                        weatherInfo.put("windspeed", weatherData.get("windspeed"));

                        // Update the display with weather info
                        displayView.updateWeatherInfo(weatherInfo);

                        // Show the new frame
                        weatherFrame.setLocationRelativeTo(null);
                        weatherFrame.setVisible(true);
                    }
                } else {
                    System.out.println("Location not found.");
                }
            }
        });

        check.getBack().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) check.getTopLevelAncestor();
                DisplayModel model = new DisplayModel();
                DisplayView view = new DisplayView(model);
                new DisplayController(model, view);

                JFrame frame = new JFrame("Weather Monitoring App");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(900, 700);
                frame.add(view);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                if (currentFrame != null) {
                    currentFrame.dispose();
                }
            }
        });
    }
}