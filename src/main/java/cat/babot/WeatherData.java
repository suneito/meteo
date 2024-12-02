package cat.babot;

import org.json.JSONObject;

import java.util.concurrent.ThreadLocalRandom;

import static cat.babot.Util.getCurrentTimestamp;

public class WeatherData {
    public String getData() {
        JSONObject weatherData = new JSONObject();
        weatherData.put("temperature", ThreadLocalRandom.current().nextInt(0, 35 + 1)); // Simulació de temperatura
        weatherData.put("humidity", ThreadLocalRandom.current().nextInt(20, 99 + 1));    // Simulació d'humitat
        weatherData.put("pressure", ThreadLocalRandom.current().nextInt(1000, 1049 + 1));  // Simulació de pressió
        weatherData.put("measureTime", getCurrentTimestamp());
        return weatherData.toString();
    }
}
