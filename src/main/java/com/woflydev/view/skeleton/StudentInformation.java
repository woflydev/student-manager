package com.woflydev.view.skeleton;

import com.woflydev.controller.GeneralUtils;
import com.woflydev.controller.StudentUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Globals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class StudentInformation extends JFrame {
    protected static StudentInformation instance = null;

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField addressField;
    private final JComboBox<String> genderComboBox;
    private final JComboBox<String> houseComboBox;
    private final JTextField ageField;

    private final JButton submitButton;

    public StudentInformation() {
        WindowUtils.applyWindowSettings(
                this,
                "Enter Information",
                new Dimension(400, 420),
                new BorderLayout(),
                JFrame.DISPOSE_ON_CLOSE
        );

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

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordField = new JPasswordField(30);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        JButton showPasswordButton = new JButton("Show");
        showPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordButton.getText().equals("Show")) {
                    passwordField.setEchoChar((char) 0);
                    showPasswordButton.setText("Hide");
                } else {
                    passwordField.setEchoChar('*');
                    showPasswordButton.setText("Show");
                }
            }
        });
        passwordPanel.add(showPasswordButton, BorderLayout.EAST);
        formPanel.add(passwordPanel);

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
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fieldsValid()) {
                    StudentUtils.saveToFile(
                            usernameField.getText(),
                            new String(passwordField.getPassword()),
                            firstNameField.getText(),
                            lastNameField.getText(),
                            addressField.getText(),
                            (String) genderComboBox.getSelectedItem(),
                            (String) houseComboBox.getSelectedItem(),
                            Integer.parseInt(ageField.getText()),
                            supplyUUID()
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

    // this is overridden in the EditWindow so that it overwrites an existing student
    // instead of creating a new one.
    protected String supplyUUID() { return UUID.randomUUID().toString(); }

    protected boolean fieldsValid() {
        if (
                usernameField.getText().isEmpty() ||
                        passwordField.getPassword().length == 0 ||
                        firstNameField.getText().isEmpty() ||
                        lastNameField.getText().isEmpty() ||
                        addressField.getText().isEmpty() ||
                        ageField.getText().isEmpty()
        ) {
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

        try {
            int age = Integer.parseInt(ageField.getText());
        } catch (NumberFormatException ex) {
            WindowUtils.errorBox("Please enter a valid age.");
            return false;
        }

        return true;
    }

    public static void open() {
        if (instance == null) {
            instance = new StudentInformation();
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

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JTextField getFirstNameField() {
        return firstNameField;
    }

    public JTextField getLastNameField() {
        return lastNameField;
    }

    public JTextField getAddressField() {
        return addressField;
    }

    public JComboBox<String> getGenderComboBox() {
        return genderComboBox;
    }

    public JComboBox<String> getHouseComboBox() {
        return houseComboBox;
    }

    public JTextField getAgeField() {
        return ageField;
    }
}
