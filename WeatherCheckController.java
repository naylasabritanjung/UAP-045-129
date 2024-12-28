import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// exception untuk lokasi yang tidak ditemukan
class LocationNotFoundException extends Exception {
    public LocationNotFoundException(String message) {
        super(message);
    }
}

public class WeatherCheckController {
    private final WeatherCheckView check;

    public WeatherCheckController(WeatherCheckView check) {
        this.check = check;

        check.getCari().addActionListener(e -> handleSearch());
        check.getBack().addActionListener(e -> handleBack());
    }

    private void handleSearch() {
        try {
            String locationName = check.getTextField().getText();

            if (locationName == null || locationName.isEmpty()) {
                throw new IllegalArgumentException("Nama lokasi tidak boleh kosong.");
            }

            JSONArray locationData = FetchApi.getLocationData(locationName);

            if (locationData == null || locationData.isEmpty()) {
                throw new LocationNotFoundException("Soriye lokasi gada");
            }

            JSONObject location = (JSONObject) locationData.get(0);
            String cityName = (String) location.get("name");
            double latitude = (double) location.get("latitude");
            double longitude = (double) location.get("longitude");

            JSONObject weatherData = FetchApi.getWeatherData(cityName);

            if (weatherData == null) {
                throw new Exception("Data cuaca tidak ditemukan untuk lokasi ini.");
            }

            displayWeatherDetails(cityName, latitude, longitude, weatherData);
        } catch (LocationNotFoundException ex) {
            JOptionPane.showMessageDialog(
                    check,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    check,
                    ex.getMessage(),
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayWeatherDetails(String cityName, double latitude, double longitude, JSONObject weatherData) {
        WeatherDisplayView displayView = new WeatherDisplayView(new DisplayModel());

        JFrame weatherFrame = new JFrame("Weather Details");
        weatherFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        weatherFrame.setSize(900, 700);
        weatherFrame.add(displayView);
        weatherFrame.setLocationRelativeTo(null);
        weatherFrame.setVisible(true);

        JSONObject weatherInfo = new JSONObject();
        weatherInfo.put("location_name", cityName);
        weatherInfo.put("latitude", latitude);
        weatherInfo.put("longitude", longitude);
        weatherInfo.put("temperature", weatherData.get("temperature"));
        weatherInfo.put("weather_condition", weatherData.get("weather_condition"));
        weatherInfo.put("humidity", weatherData.get("humidity"));
        weatherInfo.put("windspeed", weatherData.get("windspeed"));

        displayView.updateWeatherInfo(weatherInfo);

        WeatherDisplayView.getTambahButton().addActionListener(e -> handleTambahButton(cityName, weatherData, latitude, longitude));
    }

    private void handleTambahButton(String cityName, JSONObject weatherData, double latitude, double longitude) {
        String currentTime = java.time.LocalTime.now().toString();

        WeatherMonitoring.MonitoringPanel.addDataToTable(
                cityName,
                weatherData.get("weather_condition").toString(),
                (double) weatherData.get("temperature"),
                currentTime
        );

        String weatherDetails = String.format(
                "Location: %s\nLatitude: %.4f\nLongitude: %.4f\nTemperature: %.2f°C\nWeather Condition: %s\nHumidity: %d%%\nWindspeed: %.2f m/s",
                cityName,
                latitude,
                longitude,
                weatherData.get("temperature"),
                weatherData.get("weather_condition"),
                weatherData.get("humidity"),
                weatherData.get("windspeed")
        );

        JOptionPane.showMessageDialog(
                null,
                weatherDetails,
                "Weather Information Added",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void handleBack() {
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
}
