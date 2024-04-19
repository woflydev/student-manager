package com.woflydev.view;

import com.woflydev.controller.StudentUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Student;
import com.woflydev.view.skeleton.StudentInformation;

public class StudentEditWindow extends StudentInformation {
    private Student editingStudent;

    public StudentEditWindow(Student student) {
        super();

        this.editingStudent = student;

        getUsernameField().setText(student.getUsername());
        getPasswordField().setText(student.getPassword());
        getFirstNameField().setText(student.getFirstName());
        getLastNameField().setText(student.getLastName());
        getAddressField().setText(student.getAddress());
        getGenderComboBox().setSelectedItem(student.getGender());
        getHouseComboBox().setSelectedItem(student.getHouse());
        getAgeField().setText(Integer.toString(student.getAge()));
    }

    @Override
    protected boolean fieldsValid() {
        String newUsername = getUsernameField().getText();
        if (!super.fieldsValid() || (StudentUtils.usernameExists(newUsername) && !newUsername.equals(editingStudent.getUsername()))) {
            WindowUtils.errorBox("Username already exists. Please choose a different username.");
            return false;
        }
        return true;
    }

    @Override
    protected String supplyUUID() { return editingStudent.getUuid(); }

    public static void open(Student s) {
        if (instance == null) {
            instance = new StudentEditWindow(s);
            System.out.println("this is being called");

            instance.setVisible(true);
        }
    }
}