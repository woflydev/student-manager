package com.woflydev.controller;

import com.woflydev.model.Globals;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static void createBlank() {
        File file = new File(Globals.STUDENTS_FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                WindowUtils.errorBox("Error creating blank file: " + e.getMessage());
            }
        }
    }
}
