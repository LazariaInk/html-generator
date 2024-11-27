package com.pluriverse.htmlgenerator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Settings {
    private static final String PROPERTIES_FILE = "settings.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Could not load settings. Using default values.");
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void set(String key, String value) {
        System.out.println("Setting " + key + " to " + value);
        properties.setProperty(key, value);
    }

    public static void save() {
        try (OutputStream output = Files.newOutputStream(Paths.get(PROPERTIES_FILE))) {
            properties.store(output, null);
        } catch (IOException e) {
            System.out.println("Could not save settings.");
        }
    }
}
