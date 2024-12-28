import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Kelas ini digunakan untuk mengambil data cuaca dari API menggunakan geocoding
 * dan API cuaca yang disediakan oleh Open-Meteo. Kelas ini menyediakan metode
 * untuk mengambil data lokasi berdasarkan nama lokasi dan untuk mengambil data
 * cuaca terkait dengan lokasi tersebut.
 *
 * @version 1.0
 * @author Putri Nayla Sabri dan Herdiana Dwi Maharani
 */
public class FetchApi {

    /**
     * Mengambil data cuaca untuk lokasi yang diberikan.
     *
     * <p>Metode ini pertama-tama mencari data lokasi berdasarkan nama lokasi,
     * kemudian menggunakan koordinat geografis (latitude, longitude) untuk
     * mendapatkan data cuaca dari API Open-Meteo. Data cuaca yang diperoleh
     * mencakup suhu, kondisi cuaca, kelembapan relatif, dan kecepatan angin.
     * Data ini dikembalikan dalam bentuk objek JSON.</p>
     *
     * @param locationName Nama lokasi yang ingin dicari data cuacanya.
     * @return Objek JSON yang berisi data cuaca (suhu, kondisi cuaca, kelembapan, kecepatan angin),
     *         atau null jika ada kesalahan atau data tidak ditemukan.
     */
    public static JSONObject getWeatherData(String locationName) {
        JSONArray locationData = getLocationData(locationName);

        if (locationData == null || locationData.isEmpty()) {
            System.out.println("Error: Location data is missing or empty.");
            return null;
        }

        JSONObject location = (JSONObject) locationData.get(0);
        if (location == null) {
            System.out.println("Error: Unable to extract location data.");
            return null;
        }

        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=America%2FSao_Paulo";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }

            scanner.close();
            conn.disconnect();
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weatherCode.get(index), temperature);

            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            JSONArray windspeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windspeed = (double) windspeedData.get(index);

            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);

            return weatherData;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Mengambil data lokasi berdasarkan nama lokasi.
     *
     * <p>Metode ini menghubungi API geocoding untuk mendapatkan data lokasi
     * seperti koordinat geografis (latitude dan longitude) berdasarkan nama
     * lokasi yang diberikan.</p>
     *
     * @param locationName Nama lokasi yang ingin dicari.
     * @return Array JSON yang berisi data lokasi yang ditemukan, atau null jika tidak ada data.
     */
    public static JSONArray getLocationData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }

                scanner.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Menghubungi API dan mengambil respons dalam bentuk HttpURLConnection.
     *
     * @param urlString URL dari API yang akan diakses.
     * @return HttpURLConnection yang mewakili koneksi ke API.
     */
    static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Menemukan indeks dari waktu saat ini dalam daftar waktu yang diberikan.
     *
     * @param timeList Daftar waktu yang diterima dari API cuaca.
     * @return Indeks dari waktu saat ini dalam daftar waktu.
     */
    static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                return i;
            }
        }

        return 0;
    }

    /**
     * Mengambil waktu saat ini dalam format yang sesuai dengan yang dibutuhkan API.
     *
     * @return Waktu saat ini dalam format "yyyy-MM-dd'T'HH:00".
     */
    public static String getCurrentTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        return currentDateTime.format(formatter);
    }

    /**
     * Mengonversi kode cuaca menjadi kondisi cuaca yang dapat dibaca.
     *
     * @param weatherCode Kode cuaca dari API.
     * @param temperature Suhu saat ini.
     * @return Kondisi cuaca yang dapat dibaca (seperti "Clear", "Cloudy", "Rain", "Snow").
     */
    static String convertWeatherCode(long weatherCode, double temperature) {
        String weatherCondition = "";

        if (temperature <= 0) {
            weatherCondition = "Snow";
        } else {
            if (weatherCode == 0L) {
                weatherCondition = "Clear";
            } else if (weatherCode > 0L && weatherCode <= 3L) {
                weatherCondition = "Cloudy";
            } else if ((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L)) {
                weatherCondition = "Rain";
            } else if (weatherCode >= 71L && weatherCode <= 77L) {
                weatherCondition = "Snow";
            } else {
                weatherCondition = "Unknown";
            }
        }

        return weatherCondition;
    }
}
