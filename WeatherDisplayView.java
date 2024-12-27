import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.json.simple.JSONObject;

public class WeatherDisplayView extends JPanel {
    private final DisplayModel model;
    private JLabel weatherInfoLabel;
    private JLabel locationLabel;
    private JLabel temperatureLabel;
    private JLabel conditionLabel;
    private JLabel humidityLabel;
    private JLabel windspeedLabel;
    private JLabel icon;
    private JLabel humidityIcon;
    private JLabel windspeedIcon;

    public WeatherDisplayView(DisplayModel model) {
        this.model = model;
        this.weatherInfoLabel = new JLabel("", SwingConstants.CENTER);
        this.icon = new JLabel("");
        this.locationLabel = new JLabel("", SwingConstants.CENTER);
        this.temperatureLabel = new JLabel("", SwingConstants.CENTER);
        this.conditionLabel = new JLabel("", SwingConstants.CENTER);
        this.humidityLabel = new JLabel("", SwingConstants.CENTER);
        this.windspeedLabel = new JLabel("", SwingConstants.CENTER);
        this.humidityIcon = new JLabel(new ImageIcon("img/humidity.png"));
        this.windspeedIcon = new JLabel(new ImageIcon("img/windspeed.png"));
        setLayout(null);

        icon.setBounds(100, 100, 300, 300);

        locationLabel.setFont(new Font("Verdana", Font.BOLD, 30));
        locationLabel.setForeground(Color.orange);

        temperatureLabel.setFont(new Font("Verdana", Font.BOLD, 60));
        temperatureLabel.setForeground(Color.orange);

        conditionLabel.setFont(new Font("Verdana", Font.BOLD, 26));
        conditionLabel.setForeground(Color.white);

        humidityLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        humidityLabel.setForeground(Color.white);

        windspeedLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        windspeedLabel.setForeground(Color.white);

        humidityLabel.setBounds(100, 350, 30, 30);
        windspeedLabel.setBounds(150, 350, 30, 30);

        add(locationLabel);
        add(temperatureLabel);
        add(conditionLabel);
        add(humidityLabel);
        add(windspeedLabel);
        add(weatherInfoLabel, BorderLayout.SOUTH);
        add(icon);
        add(humidityIcon);
        add(windspeedIcon);

        Timer timer = new Timer(50, e -> {
            model.updateAngle(0.03f);
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        frameGradasi(g2d);
    }

    private void frameGradasi(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        float angle = model.getAngle();

        float x1 = (float) (Math.cos(angle) * width / 2 + width / 2);
        float y1 = (float) (Math.sin(angle) * height / 2 + height / 2);
        float x2 = (float) (Math.cos(angle + Math.PI) * width / 2 + width / 2);
        float y2 = (float) (Math.sin(angle + Math.PI) * height / 2 + height / 2);

        GradientPaint gradient = new GradientPaint(x1, y1, model.getColor1(), x2, y2, model.getColor2());
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

        locationLabel.setBounds((width - locationLabel.getPreferredSize().width) / 2, 40, locationLabel.getPreferredSize().width, 100);
        temperatureLabel.setBounds((width - temperatureLabel.getPreferredSize().width) / 2, 320, temperatureLabel.getPreferredSize().width, 200);
        conditionLabel.setBounds((width - conditionLabel.getPreferredSize().width) / 2, 440, conditionLabel.getPreferredSize().width, 50); // Tempatkan conditionLabel di bawah temperatureLabel
        humidityLabel.setBounds((width / 2 - humidityLabel.getPreferredSize().width) / 2, 500, humidityLabel.getPreferredSize().width, 50); // Tempatkan humidityLabel di bawah conditionLabel
        windspeedLabel.setBounds((width / 2 + 400) / 2, 500, windspeedLabel.getPreferredSize().width, 50); // Tempatkan windspeedLabel di sebelah humidityLabel

        // Set positions for the icons
        humidityIcon.setBounds(humidityLabel.getX() - 50, humidityLabel.getY() - 20, 30, 30); // Icon to the left of humidity
        windspeedIcon.setBounds(windspeedLabel.getX() - 50, windspeedLabel.getY() - 20, 30, 30); // Icon to the left of windspeed
    }

    public void updateWeatherInfo(JSONObject weatherData) {
        if (weatherData != null) {
            // Extract weather data
            String location = (String) weatherData.get("location_name"); // Nama lokasi
            double temperature = (double) weatherData.get("temperature");
            String weatherCondition = (String) weatherData.get("weather_condition");
            long humidity = (long) weatherData.get("humidity");
            double windspeed = (double) weatherData.get("windspeed");

            // Set the weather icon
            ImageIcon resizedIcon = getWeatherIcon(weatherCondition);
            icon.setIcon(resizedIcon);

            // Update the weather info label with formatted text
            weatherInfoLabel.setText(String.format(
                    "<html></h2>Temperature: %.2f°C<br>Condition: %s<br>Humidity: %d%%<br>Windspeed: %.2f m/s</html>",
                    temperature, weatherCondition, humidity, windspeed
            ));

            locationLabel.setText(String.format(location));
            temperatureLabel.setText(String.format("%.1f°C", temperature));
            conditionLabel.setText(String.format("%s", weatherCondition)); // Update the conditionLabel with weather condition
            humidityLabel.setText(String.format("%d%%", humidity)); // Update the humidityLabel
            windspeedLabel.setText(String.format("%.2f m/s", windspeed)); // Update the windspeedLabel

            repaint();
        }
    }

    // Function to return the corresponding icon based on weather condition
    private ImageIcon getWeatherIcon(String weatherCondition) {
        ImageIcon icon = null;

        switch (weatherCondition.toLowerCase()) {
            case "cloudy":
                icon = new ImageIcon("img/cloudy.png");
                break;
            case "clear":
                icon = new ImageIcon("img/weather.png");
                break;
            case "rain":
                icon = new ImageIcon("img/rainy.png");
                break;
            case "snow":
                icon = new ImageIcon("img/snowy.png");
                break;
            default:
                new JOptionPane("Nama kota yang anda cari tidak ditemukan!");
                break;
        }
        int iconWidth = 300;
        int iconHeight = 300;
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH); // Resize the image
        return new ImageIcon(scaledImg);
    }

    public static void createAndShowFrame(DisplayModel model) {
        JFrame frame = new JFrame("Weather Check");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);

        WeatherDisplayView weatherDisplayView = new WeatherDisplayView(model);
        frame.add(weatherDisplayView);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
