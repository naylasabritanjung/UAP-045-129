import org.json.simple.JSONObject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Kelas ini merupakan representasi tampilan antarmuka pengguna untuk menampilkan informasi cuaca,
 * termasuk lokasi, suhu, kondisi cuaca, kelembaban, dan kecepatan angin. Kelas ini juga menangani
 * pembaruan data cuaca dan tampilan ikon cuaca sesuai dengan kondisi cuaca yang diterima.
 *
 * @author Putri Nayla Sabri dan Herdiana Dwi Maharani
 */
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
    private static JButton tambah;
    private JButton back;

    /**
     * Konstruktor untuk membuat tampilan cuaca dengan model yang diberikan.
     *
     * @param model Model yang digunakan untuk pembaruan data cuaca dan tampilan.
     */
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

        // Mengatur posisi dan properti elemen-elemen UI
        icon.setBounds(290, 120, 300, 300);
        tambah = new JButton("Tambah");
        tambah.setBounds(720, 580, 130, 40);
        tambah.setFont(new Font("Verdana", Font.BOLD, 15));
        tambah.setContentAreaFilled(false);
        tambah.setFocusable(false);
        tambah.setBorder(BorderFactory.createEmptyBorder());
        add(tambah);

        back = new JButton("Kembali");
        back.setBounds(50, 580, 130, 40);
        back.setFont(new Font("Verdana", Font.BOLD, 15));
        back.setContentAreaFilled(false);
        back.setFocusable(false);

        back.addActionListener(e -> {
            // Menutup jendela saat ini
            SwingUtilities.getWindowAncestor(this).dispose();

            // Membuat dan menampilkan DisplayView
            DisplayView displayView = new DisplayView(new DisplayModel());
            displayView.openNewPanel(new DisplayModel());
            displayView.setVisible(true);
        });


        add(back);

        // Mengatur font dan warna elemen-elemen UI
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

        humidityLabel.setBounds(200, 350, 30, 30);
        windspeedLabel.setBounds(700, 350, 30, 30);

        add(locationLabel);
        add(temperatureLabel);
        add(conditionLabel);
        add(weatherInfoLabel, BorderLayout.SOUTH);
        add(icon);

        Timer timer = new Timer(50, e -> {
            model.updateAngle(0.03f);
            repaint();
        });
        timer.start();
    }

    /**
     * Mengembalikan tombol "Tambah" yang dapat digunakan untuk menambahkan elemen ke tampilan.
     *
     * @return Tombol "Tambah".
     */
    public static JButton getTambahButton() {
        return tambah;
    }

    /**
     * Menggambar batas panel dengan menggunakan efek gradien.
     *
     * @param g Grafik yang digunakan untuk menggambar panel.
     */
    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(0, 0, Color.ORANGE, 1, getHeight(), Color.ORANGE.brighter());
        g2d.setPaint(gradient);

        // Menggambar tombol dengan sudut yang melengkung
        RoundRectangle2D.Float roundButtonCari = new RoundRectangle2D.Float(tambah.getX(), tambah.getY(), tambah.getWidth(), tambah.getHeight(), 20, 20);
        g2d.fill(roundButtonCari);

        RoundRectangle2D.Float roundButtonBack = new RoundRectangle2D.Float(back.getX(), back.getY(), back.getWidth(), back.getHeight(), 20, 20);
        g2d.fill(roundButtonBack);
    }

    /**
     * Menggambar komponen panel dengan menggunakan efek gradien pada latar belakang.
     *
     * @param g Grafik yang digunakan untuk menggambar komponen.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        frameGradasi(g2d);
    }

    /**
     * Membuat efek gradien pada latar belakang panel.
     *
     * @param g2d Grafik 2D yang digunakan untuk menggambar efek gradien.
     */
    private void frameGradasi(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        float angle = model.getAngle();

        // Menghitung posisi titik untuk gradien
        float x1 = (float) (Math.cos(angle) * width / 2 + width / 2);
        float y1 = (float) (Math.sin(angle) * height / 2 + height / 2);
        float x2 = (float) (Math.cos(angle + Math.PI) * width / 2 + width / 2);
        float y2 = (float) (Math.sin(angle + Math.PI) * height / 2 + height / 2);

        GradientPaint gradient = new GradientPaint(x1, y1, model.getColor1(), x2, y2, model.getColor2());
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

        locationLabel.setBounds((width - locationLabel.getPreferredSize().width) / 2, 40, locationLabel.getPreferredSize().width, 100);
        temperatureLabel.setBounds((width - temperatureLabel.getPreferredSize().width) / 2, 320, temperatureLabel.getPreferredSize().width, 200);
        conditionLabel.setBounds((width - conditionLabel.getPreferredSize().width) / 2, 440, conditionLabel.getPreferredSize().width, 50);
        humidityLabel.setBounds((width / 2 - humidityLabel.getPreferredSize().width) / 2, 500, humidityLabel.getPreferredSize().width, 50);
        windspeedLabel.setBounds((width / 2 + 600) / 2, 500, windspeedLabel.getPreferredSize().width, 50);

        // Menempatkan ikon kelembaban dan kecepatan angin
        humidityIcon.setBounds(humidityLabel.getX() - 50, humidityLabel.getY() - 20, 30, 30);
        windspeedIcon.setBounds(windspeedLabel.getX() - 50, windspeedLabel.getY() - 20, 30, 30);
    }

    /**
     * Memperbarui informasi cuaca pada tampilan berdasarkan data cuaca yang diterima.
     *
     * @param weatherData Data cuaca dalam format JSON.
     */
    public void updateWeatherInfo(JSONObject weatherData) {
        if (weatherData != null) {
            String location = (String) weatherData.get("location_name");
            double temperature = (double) weatherData.get("temperature");
            String weatherCondition = (String) weatherData.get("weather_condition");
            long humidity = (long) weatherData.get("humidity");
            double windspeed = (double) weatherData.get("windspeed");

            // Menampilkan ikon cuaca berdasarkan kondisi
            ImageIcon resizedIcon = getWeatherIcon(weatherCondition);
            icon.setIcon(resizedIcon);

            // Memperbarui label dengan informasi cuaca
            weatherInfoLabel.setText(String.format(
                    "<html></h2>Temperature: %.2f°C<br>Condition: %s<br>Humidity: %d%%<br>Windspeed: %.2f m/s</html>",
                    temperature, weatherCondition, humidity, windspeed
            ));

            locationLabel.setText(String.format(location));
            temperatureLabel.setText(String.format("%.1f°C", temperature));
            conditionLabel.setText(String.format("%s", weatherCondition));
            humidityLabel.setText(String.format("%d%%", humidity));
            windspeedLabel.setText(String.format("%.2f m/s", windspeed));

            repaint();
        }
    }

    /**
     * Mengembalikan ikon yang sesuai dengan kondisi cuaca yang diberikan.
     *
     * @param weatherCondition Kondisi cuaca.
     * @return Ikon cuaca yang sesuai.
     */
    private ImageIcon getWeatherIcon(String weatherCondition) {
        ImageIcon icon = null;

        switch (weatherCondition.toLowerCase()) {
            case "cloudy":
                icon = new ImageIcon("img/cloudy.png");
                break;
            case "clear":
                icon = new ImageIcon("img/clear.png");
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
        Image scaledImg = img.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    /**
     * Membuat dan menampilkan jendela utama aplikasi cuaca.
     *
     * @param model Model yang digunakan untuk tampilan cuaca.
     */
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
