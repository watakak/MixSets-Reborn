package net.watakak;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class Config {
    private static final String CONFIG_PATH = "config/mixsets.properties";
    private static Properties properties = new Properties();

    static {
        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadConfig() throws IOException {
        File configFile = new File(CONFIG_PATH);
        if (!configFile.exists()) {
            try (InputStream input = Config.class.getClassLoader().getResourceAsStream("assets/mixsets/mixsets.properties")) {
                if (input != null) {
                    Files.copy(input, configFile.toPath());
                }
            }
        }
        try (FileInputStream input = new FileInputStream(configFile)) {
            properties.load(input);
        }
    }

    public static boolean getBool(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public static void setBool(String key, boolean value) {
        properties.setProperty(key, Boolean.toString(value));
        saveConfig();
    }

    public static String getString(String key) {
        String value = properties.getProperty(key);
        return value != null ? Placeholders.parse(value) : null;
    }

    public static void setString(String key, String value) {
        properties.setProperty(key, value);
        saveConfig();
    }

    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key, "0")); // Default to 0 if not found
    }

    public static void setInt(String key, int value) {
        properties.setProperty(key, Integer.toString(value));
        saveConfig();
    }

    public static float getFloat(String key) {
        return Float.parseFloat(properties.getProperty(key, "0.0f")); // Default to 0.0f if not found
    }

    public static void setFloat(String key, float value) {
        properties.setProperty(key, Float.toString(value));
        saveConfig();
    }

    private static void saveConfig() {
        try (FileOutputStream output = new FileOutputStream(CONFIG_PATH)) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
