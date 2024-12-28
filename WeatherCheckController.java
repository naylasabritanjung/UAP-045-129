import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Kelas exception yang mewakili kesalahan ketika lokasi tidak ditemukan.
 * Exception ini dilemparkan ketika query lokasi tidak menghasilkan hasil
 * dari sumber data cuaca atau lokasi.
 */
class LocationNotFoundException extends Exception {

    /**
     * Konstruktor untuk membuat {@code LocationNotFoundException} baru dengan pesan kesalahan tertentu.
     *
     * @param message pesan detail yang akan diberikan bersama exception.
     */
    public LocationNotFoundException(String message) {
        super(message);
    }
}

/**
 * Kelas pengontrol untuk menangani fungsionalitas pengecekan cuaca.
 * Kelas ini berinteraksi dengan {@link WeatherCheckView} dan menangani aksi
 * untuk mencari data cuaca berdasarkan lokasi, menampilkan detail cuaca,
 * serta mengelola navigasi antar tampilan yang berbeda.
 *
 * @author Putri Nayla Sabri
 * @author Herdiana Dwi Maharani
 * @version 1.0
 */
public class WeatherCheckController {
    private final WeatherCheckView check;

    /**
     * Konstruktor untuk menginisialisasi pengontrol dengan tampilan yang diberikan.
     * Konstruktor ini juga menyetel pendengar aksi untuk tombol cari dan tombol kembali.
     *
     * @param check tampilan yang terkait dengan pengontrol ini.
     */
    public WeatherCheckController(WeatherCheckView check) {
        this.check = check;

        // Menambahkan pendengar aksi untuk tombol cari
        check.getCari().addActionListener(e -> handleSearch());

        // Menambahkan pendengar aksi untuk tombol kembali
        check.getBack().addActionListener(e -> handleBack());
    }

    /**
     * Menangani aksi pencarian ketika pengguna mengklik tombol cari.
     * Fungsi ini mengambil data lokasi dan cuaca lalu menampilkan detail cuaca.
     * Jika terjadi exception, pesan kesalahan yang sesuai akan ditampilkan.
     */
    private void handleSearch() {
        try {
            String locationName = check.getTextField().getText();

            // Memeriksa jika nama lokasi kosong
            if (locationName == null || locationName.isEmpty()) {
                throw new IllegalArgumentException("Nama lokasi tidak boleh kosong.");
            }

            // Mengambil data lokasi dari API
            JSONArray locationData = FetchApi.getLocationData(locationName);

            // Memeriksa jika data lokasi kosong
            if (locationData == null || locationData.isEmpty()) {
                throw new LocationNotFoundException("Lokasi tidak ditemukan");
            }

            // Mengambil detail lokasi dari respons API
            JSONObject location = (JSONObject) locationData.get(0);
            String cityName = (String) location.get("name");
            double latitude = (double) location.get("latitude");
            double longitude = (double) location.get("longitude");

            // Mengambil data cuaca untuk lokasi
            JSONObject weatherData = FetchApi.getWeatherData(cityName);

            // Memeriksa jika data cuaca tersedia
            if (weatherData == null) {
                throw new Exception("Data cuaca tidak ditemukan untuk lokasi ini.");
            }

            // Menampilkan detail cuaca
            displayWeatherDetails(cityName, latitude, longitude, weatherData);
        } catch (LocationNotFoundException ex) {
            // Menampilkan pesan kesalahan untuk lokasi yang tidak ditemukan
            JOptionPane.showMessageDialog(
                    check,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            // Menampilkan pesan peringatan untuk input yang tidak valid
            JOptionPane.showMessageDialog(
                    check,
                    ex.getMessage(),
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception ex) {
            // Mencetak stack trace untuk exception lain yang terjadi
            ex.printStackTrace();
        }
    }

    /**
     * Menampilkan detail cuaca di jendela baru.
     *
     * @param cityName nama kota.
     * @param latitude lintang lokasi.
     * @param longitude bujur lokasi.
     * @param weatherData data cuaca untuk lokasi tersebut.
     */
    private void displayWeatherDetails(String cityName, double latitude, double longitude, JSONObject weatherData) {
        // Membuat tampilan baru WeatherDisplayView untuk menampilkan informasi cuaca
        WeatherDisplayView displayView = new WeatherDisplayView(new DisplayModel());

        // Membuat dan mengonfigurasi JFrame baru untuk menampilkan informasi cuaca
        JFrame weatherFrame = new JFrame("Weather Details");
        weatherFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        weatherFrame.setSize(900, 700);
        weatherFrame.add(displayView);
        weatherFrame.setLocationRelativeTo(null);
        weatherFrame.setVisible(true);

        // Membuat objek JSON untuk menyimpan informasi cuaca
        JSONObject weatherInfo = new JSONObject();
        weatherInfo.put("location_name", cityName);
        weatherInfo.put("latitude", latitude);
        weatherInfo.put("longitude", longitude);
        weatherInfo.put("temperature", weatherData.get("temperature"));
        weatherInfo.put("weather_condition", weatherData.get("weather_condition"));
        weatherInfo.put("humidity", weatherData.get("humidity"));
        weatherInfo.put("windspeed", weatherData.get("windspeed"));

        // Memperbarui tampilan cuaca dengan informasi cuaca yang diambil
        displayView.updateWeatherInfo(weatherInfo);

        // Menambahkan pendengar aksi untuk tombol "Tambah"
        WeatherDisplayView.getTambahButton().addActionListener(e -> handleTambahButton(cityName, weatherData, latitude, longitude));
    }

    /**
     * Menangani aksi ketika tombol "Tambah" diklik.
     * Data cuaca akan ditambahkan ke tabel pemantauan dan detail cuaca ditampilkan.
     *
     * @param cityName nama kota.
     * @param weatherData data cuaca untuk lokasi tersebut.
     * @param latitude lintang lokasi.
     * @param longitude bujur lokasi.
     */
    private void handleTambahButton(String cityName, JSONObject weatherData, double latitude, double longitude) {
        String currentTime = java.time.LocalTime.now().toString();

        // Menambahkan data cuaca ke tabel pemantauan
        WeatherMonitoring.MonitoringPanel.addDataToTable(
                cityName,
                weatherData.get("weather_condition").toString(),
                (double) weatherData.get("temperature"),
                currentTime
        );

        // Menyusun dan menampilkan informasi cuaca dalam dialog
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

    /**
     * Menangani aksi kembali ketika tombol kembali diklik.
     * Fungsi ini akan menavigasi ke tampilan utama.
     */
    private void handleBack() {
        // Menutup jendela saat ini dan menavigasi ke tampilan utama
        JFrame currentFrame = (JFrame) check.getTopLevelAncestor();
        DisplayModel model = new DisplayModel();
        DisplayView view = new DisplayView(model);
        new DisplayController(model, view);

        // Membuat dan menampilkan jendela baru
        JFrame frame = new JFrame("Weather Monitoring App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.add(view);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Menutup jendela yang sedang aktif
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    }
}