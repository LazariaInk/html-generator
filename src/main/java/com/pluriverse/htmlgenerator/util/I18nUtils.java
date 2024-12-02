package com.pluriverse.htmlgenerator.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class I18nUtils {

    private static final String SETTINGS_FILE = "settings.properties";
    private static ResourceBundle resourceBundle;

    static {
        loadLanguage();
    }
    public static void loadLanguage() {
        try {
            Properties settings = new Properties();
            settings.load(Files.newInputStream(Paths.get(SETTINGS_FILE)));

            String language = settings.getProperty("language", "en");
            Locale locale = new Locale(language);

            resourceBundle = ResourceBundle.getBundle("i18n.dialogs", locale);

        } catch (IOException e) {
            System.err.println("Error loading settings file: " + e.getMessage());
            resourceBundle = ResourceBundle.getBundle("i18n.dialogs", Locale.ENGLISH);
        }
    }

    public static String getText(String key) {
        if (resourceBundle == null) {
            loadLanguage();
        }
        return resourceBundle.getString(key);
    }
}
