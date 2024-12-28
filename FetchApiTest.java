import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * This class contains test cases for the {@link FetchApi} class.
 * It tests the methods {@link FetchApi#getWeatherData(String)} and
 * {@link FetchApi#getLocationData(String)} for correctness.
 */
public class FetchApiTest {

    /**
     * Test case for {@link FetchApi#getWeatherData(String)} method.
     * Verifies that the weather data is not null and contains expected keys.
     */
    @Test
    public void testGetWeatherData() {
        String locationName = "London";
        JSONObject result = FetchApi.getWeatherData(locationName);
        assertNotNull(result, "Weather data should not be null");
        assertTrue(result.containsKey("temperature"), "Weather data should contain temperature");
        assertTrue(result.containsKey("weather_condition"), "Weather data should contain weather condition");
    }

    /**
     * Test case for {@link FetchApi#getLocationData(String)} method.
     * Verifies that the location data is not null and contains entries.
     */
    @Test
    public void testGetLocationData() {
        String locationName = "New York";

        JSONArray locationData = FetchApi.getLocationData(locationName);
        assertNotNull(locationData, "Location data should not be null");
        assertTrue(locationData.size() > 0, "Location data should not be empty");
    }
}
