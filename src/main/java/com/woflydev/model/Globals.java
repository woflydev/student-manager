package com.woflydev.model;

public class Globals {
    public static final String STUDENTS_FILE_NAME = "students.json";
    public static final String SETTINGS_FILE_NAME = "settings.properties";

    public static final String[] GENDER_OPTIONS = {"Select an Option", "Male", "Female"};
    public static final String[] HOUSE_OPTIONS = {"Agnesi", "Curie", "DaVinci", "Franklin", "Hawking", "Hollows", "Jackson", "Newton"};

    public static final String[] STUDENT_TABLE_COLUMN_NAMES = {
            "UUID",
            "First Name",
            "Last Name",
            "Address",
            "Gender",
            "School House",
            "Age",
            "",
            ""
    };

    public static final Object[] STUDENT_TABLE_NOT_FOUND_CONTENT = {
            "No results found", "", "", "", "", "", "", "", "",
    };

    public static final String SETTINGS_DELETE_WITHOUT_CONFIRM = "deleteWithoutConfirmation";
    public static final String SETTINGS_NORMALIZE_NAMES = "normalizeNames";
}
