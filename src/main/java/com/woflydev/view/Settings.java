package com.woflydev.view;

import com.woflydev.model.Globals;

import java.io.*;
import java.util.Properties;

public class Settings {
    private static boolean deleteWithoutConfirmation = false;
    private static final String SETTINGS_FILE_NAME = "settings.properties";

    public static boolean isDeleteWithoutConfirmation() {
        return deleteWithoutConfirmation;
    }

    public static void setDeleteWithoutConfirmation(boolean value) {
        deleteWithoutConfirmation = value;
        saveSettings();
    }

    public static void loadSettings() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(Globals.SETTINGS_FILE_NAME)) {
            properties.load(input);
            deleteWithoutConfirmation = Boolean.parseBoolean(properties.getProperty("deleteWithoutConfirmation", "false"));
        } catch (IOException ex) {
            // If file doesn't exist or error reading, use default settings
            deleteWithoutConfirmation = false;
        }
    }

    private static void saveSettings() {
        Properties properties = new Properties();
        properties.setProperty("deleteWithoutConfirmation", Boolean.toString(deleteWithoutConfirmation));
        try (OutputStream output = new FileOutputStream(Globals.SETTINGS_FILE_NAME)) {
            properties.store(output, "Settings");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
