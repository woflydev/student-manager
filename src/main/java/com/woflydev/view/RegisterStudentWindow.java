package com.woflydev.view;

import com.woflydev.controller.GeneralUtils;
import com.woflydev.controller.StudentUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Globals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class RegisterStudentWindow extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField addressField;
    private final JComboBox<String> genderComboBox;
    private final JComboBox<String> houseComboBox;
    private final JTextField ageField;

    private static RegisterStudentWindow instance = null;

    public RegisterStudentWindow() {
        setTitle("Enter Information");
        setSize(400, 420);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Use DISPOSE_ON_CLOSE to just close the window
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameLabel);

        usernameField = new JTextField(30);
        formPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordLabel);

        passwordField = new JPasswordField(30);
        formPanel.add(passwordField);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(firstNameLabel);

        firstNameField = new JTextField(30);
        formPanel.add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lastNameLabel);

        lastNameField = new JTextField(30);
        formPanel.add(lastNameField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(addressLabel);

        addressField = new JTextField(30);
        formPanel.add(addressField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(genderLabel);

        genderComboBox = new JComboBox<>(Globals.GENDER_OPTIONS);
        formPanel.add(genderComboBox);

        JLabel schoolHouseLabel = new JLabel("School House:");
        schoolHouseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(schoolHouseLabel);

        houseComboBox = new JComboBox<>(Globals.HOUSE_OPTIONS);
        formPanel.add(houseComboBox);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(ageLabel);

        ageField = new JTextField(30);
        formPanel.add(ageField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    StudentUtils.saveToFile(
                            usernameField.getText(),
                            new String(passwordField.getPassword()),
                            firstNameField.getText(),
                            lastNameField.getText(),
                            addressField.getText(),
                            (String) genderComboBox.getSelectedItem(),
                            (String) houseComboBox.getSelectedItem(),
                            Integer.parseInt(ageField.getText()),
                            UUID.randomUUID().toString()
                    );
                    dispose();
                }
            }
        });
        buttonPanel.add(submitButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean validateFields() {
        if (
                        usernameField.getText().isEmpty() ||
                        passwordField.getPassword().length == 0 ||
                        firstNameField.getText().isEmpty() ||
                        lastNameField.getText().isEmpty() ||
                        addressField.getText().isEmpty() ||
                        ageField.getText().isEmpty()
        )
        {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (GeneralUtils.hasInteger(firstNameField.getText()) || GeneralUtils.hasInteger(lastNameField.getText())) {
            WindowUtils.errorBox("Numeric characters are not allowed in names.");
            return false;
        }

        String selectedGender = (String) genderComboBox.getSelectedItem();
        String selectedHouse = (String) houseComboBox.getSelectedItem();
        if (selectedGender == null || selectedHouse == null || selectedGender.equalsIgnoreCase(Globals.GENDER_OPTIONS[0])) {
            WindowUtils.errorBox("Please select both gender and house.");
            return false;
        }

        try { int age = Integer.parseInt(ageField.getText()); }
        catch (NumberFormatException ex) {
            WindowUtils.errorBox("Please enter a valid age.");
            return false;
        }

        return true;
    }

    public static void open() {
        if (instance == null) {
            instance = new RegisterStudentWindow();
            instance.setVisible(true);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (instance != null) {
            instance = null;
        }
    }
}
