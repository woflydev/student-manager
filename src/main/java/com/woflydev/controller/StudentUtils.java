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
            String username,
            String password,
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
                username,
                password,
                fNameFixed,
                lNameFixed,
                addr.trim(),
                gender,
                house,
                age,
                uuid
        );

        boolean found = false;
        for (int i = 0; i < Objects.requireNonNull(existingStudentList).size(); i++) {
            Student studentInList = existingStudentList.get(i);
            if (studentInList.getUuid().equals(uuid)) {
                existingStudentList.set(i, studentInfo);
                found = true;
                break;
            }
        }

        if (!found) existingStudentList.add(studentInfo);

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
                    model.addRow(rowInformation(student));
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
                        model.addRow(rowInformation(student));
                    }
                }
            }
        } else { WindowUtils.errorBox("Error when searching for students.\n This may be due to an error when loading students from disk."); }

        if (model.getRowCount() == 0) { model.addRow(Globals.STUDENT_TABLE_NOT_FOUND_CONTENT); }

        return model;
    }

    private static Object[] rowInformation(Student student) {
        return new Object[]{
                student.getUuid(),
                student.getFirstName(),
                student.getLastName(),
                student.getAddress(),
                student.getGender(),
                student.getHouse(),
                student.getAge(),
                "Edit",
                "Delete"
        };
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
        WindowUtils.errorBox("An error occurred when getting student from UUID.");
        return null;
    }

    public static @Nullable Student getStudentByTableRow(JTable t, int row) {
        String uuid = (String) t.getValueAt(row, 0);
        Student s = getStudentByUUID(uuid);
        return s;
    }

    public static boolean validateStudentLogin(String username, String password) {
        List<Student> studentList = loadStudentsFromDisk();
        if (studentList != null) {
            for (Student student : studentList) {
                if (student.getUsername().equals(username) && student.getPassword().equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean usernameExists(String username) {
        List<Student> studentList = loadStudentsFromDisk();
        if (studentList != null) {
            for (Student student : studentList) {
                if (student.getUsername().equals(username)) {
                    return true;
                }
            }
        }
        return false;
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
        textArea.append("Username: " + student.getUsername() + "\n");
        textArea.append("Password: " + student.getPassword() + "\n");
        textArea.append("Name: " + student.getFirstName() + " " + student.getLastName() + "\n");
        textArea.append("Address: " + student.getAddress() + "\n");
        textArea.append("Gender: " + student.getGender() + "\n");
        textArea.append("House: " + student.getHouse() + "\n");
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
