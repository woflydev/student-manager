package com.woflydev.controller;

import com.woflydev.model.Globals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SettingsUtils {
    public static Map<String, Boolean> loadSettings() {
        Properties properties = new Properties();
        Map<String, Boolean> settingsMap = new HashMap<>();
        try (InputStream input = new FileInputStream(Globals.SETTINGS_FILE_NAME)) {
            properties.load(input);
            for (String key : properties.stringPropertyNames()) {
                settingsMap.put(key, Boolean.parseBoolean(properties.getProperty(key, "false")));
            }
        } catch (IOException ex) {
            // if file doesn't exist or error reading, use default settings
        }
        return settingsMap;
    }

    public static void saveSettings(Map<String, Boolean> settingsMap) {
        Properties properties = new Properties();
        settingsMap.forEach((key, value) -> properties.setProperty(key, Boolean.toString(value)));
        try (FileOutputStream output = new FileOutputStream(Globals.SETTINGS_FILE_NAME)) {
            properties.store(output, "Settings");
        } catch (IOException ex) {
            System.err.println("An error occurred while saving settings! Error: " + ex.getMessage());
        }
    }

    public static boolean getSetting(String key) {
        if (loadSettings().containsKey(key))
            return loadSettings().get(key);
        WindowUtils.errorBox("An error occurred while getting the settings for the key: " + key);
        return false; // returns false by default if nothing is found
    }
}
