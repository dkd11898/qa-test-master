package mission.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ConfigReader {

    private static final String CONFIG_PATH = "src/test/java/TestData/TestData.properties";
    private static final Properties PROPERTIES = load();

    private ConfigReader() {
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(Path.of(CONFIG_PATH))) {
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load configuration from " + CONFIG_PATH, e);
        }
        return properties;
    }

    public static String getProperty(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing config property: " + key);
        }
        return value;
    }
}
