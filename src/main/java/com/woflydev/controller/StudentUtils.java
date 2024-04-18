package com.woflydev.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woflydev.model.Globals;
import com.woflydev.model.Student;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;
import java.util.List;

public class StudentUtils {
    public static void saveToFile(
            String fName,
            String lName,
            String addr,
            String gender,
            String house,
            int age,
            String uuid
    ) {
        FileUtils.createBlank();
        List<Student> existingStudentList = loadStudentsFromDisk();

        String fNameFixed = SettingsUtils.getSetting(Globals.SETTINGS_NORMALIZE_NAMES)
                ? fName.trim().substring(0, 1).toUpperCase() + fName.trim().substring(1).toLowerCase()
                : fName;
        String lNameFixed = SettingsUtils.getSetting(Globals.SETTINGS_NORMALIZE_NAMES)
                ? lName.trim().substring(0, 1).toUpperCase() + lName.trim().substring(1).toLowerCase()
                : fName;

        Student studentInfo = new Student(
                fNameFixed,
                lNameFixed,
                addr.trim(),
                gender,
                house,
                age,
                uuid
        );

        if (existingStudentList == null) existingStudentList = new ArrayList<>();
        existingStudentList.add(studentInfo);
        saveToDisk(existingStudentList);
    }

    public static @Nullable List<Student> loadStudentsFromDisk() {
        FileUtils.createBlank();
        try (BufferedReader reader = new BufferedReader(new FileReader(Globals.STUDENTS_FILE_NAME))) {
            Gson gson = new Gson();
            java.lang.reflect.Type studentListType = new TypeToken<List<Student>>() {}.getType();
            return gson.fromJson(reader, studentListType);
        } catch (IOException e) {
            System.err.println("Error loading student information: " + e.getMessage());
            WindowUtils.errorBox(
                    "Could not load student information. \n " + e.getMessage());
            return null;
        }
    }

    public static DefaultTableModel searchStudentsTableModel(String term, String criteria) {
        List<Student> studentList = loadStudentsFromDisk();
        DefaultTableModel model = new DefaultTableModel(Globals.STUDENT_TABLE_COLUMN_NAMES, 0);

        if (studentList != null) {
            if (term == null || term.isEmpty()) {
                for (Student student : studentList) {
                    model.addRow(new Object[]{
                            student.getUuid(),
                            student.getFirstName(),
                            student.getLastName(),
                            student.getAddress(),
                            student.getGender(),
                            student.getSchoolHouse(),
                            student.getAge(),
                            "Delete"
                    });
                }
            } else {
                studentList.sort(new Comparator<Student>() {
                    @Override
                    public int compare(Student s1, Student s2) {
                        double similarity1 = calculateSimilarity(s1, term, criteria);
                        double similarity2 = calculateSimilarity(s2, term, criteria);
                        return Double.compare(similarity2, similarity1);
                    }
                });

                for (Student student : studentList) {
                    double similarity = calculateSimilarity(student, term, criteria);
                    if (similarity > 0) {
                        model.addRow(new Object[]{
                                student.getUuid(),
                                student.getFirstName(),
                                student.getLastName(),
                                student.getAddress(),
                                student.getGender(),
                                student.getSchoolHouse(),
                                student.getAge(),
                                "Delete"
                        });
                    }
                }
            }
        } else {
            WindowUtils.errorBox("Error when searching for students.\n This may be due to an error when loading students from disk.");
        }

        if (model.getRowCount() == 0) { model.addRow(Globals.STUDENT_TABLE_NOT_FOUND_CONTENT); }

        return model;
    }

    public static @Nullable Student getStudentByUUID(String uuid) {
        List<Student> studentList = loadStudentsFromDisk();
        if (studentList != null) {
            for (Student student : studentList) {
                if (student.getUuid().equals(uuid)) {
                    return student;
                }
            }
        }
        return null;
    }

    public static void removeStudent(Student studentToRemove) {
        List<Student> studentList = loadStudentsFromDisk();
        if (studentList != null) {
            studentList.removeIf(student -> student.getUuid().equals(studentToRemove.getUuid()));
            saveToDisk(studentList);
        }
    }

    public static void showStudentPopup(Student student) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.append("UUID: " + student.getUuid() + "\n");
        textArea.append("Name: " + student.getFirstName() + " " + student.getLastName() + "\n");
        textArea.append("Address: " + student.getAddress() + "\n");
        textArea.append("Gender: " + student.getGender() + "\n");
        textArea.append("House: " + student.getSchoolHouse() + "\n");
        textArea.append("Age: " + student.getAge() + "\n");

        JScrollPane scrollPane = new JScrollPane(textArea);
        WindowUtils.infoBox(scrollPane, "Student Details");
    }

    private static void saveToDisk(List<Student> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Globals.STUDENTS_FILE_NAME))) {
            gson.toJson(list, writer);
            WindowUtils.infoBox("Records updated successfully!");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            WindowUtils.errorBox("Error when updating records: " + ex.getMessage());
        }
    }

    private static double calculateSimilarity(Student student, String searchTerm, String selectedCriteria) {
        return switch (Objects.requireNonNull(selectedCriteria)) {
            case "First Name" -> GeneralUtils.similarity(student.getFirstName().toLowerCase(), searchTerm);
            case "Last Name" -> GeneralUtils.similarity(student.getLastName().toLowerCase(), searchTerm);
            case "UUID" -> GeneralUtils.similarity(student.getUuid().toLowerCase(), searchTerm);
            default -> 0;
        };
    }
}
