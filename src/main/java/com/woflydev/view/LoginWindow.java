package com.woflydev.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.woflydev.controller.StudentUtils;
import com.woflydev.controller.WindowUtils;

public class LoginWindow extends JFrame implements ActionListener {
    private static LoginWindow instance = null;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginWindow() {
        WindowUtils.applyWindowSettings(
                this,
                "Login",
                new Dimension(300, 150),
                new GridLayout(3, 2),
                JFrame.EXIT_ON_CLOSE
        );

        JLabel usernameLabel = new JLabel("Username:");
        add(usernameLabel);

        usernameField = new JTextField();
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        add(passwordLabel);

        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        add(registerButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (StudentUtils.validateStudentLogin(username, password)) {
                WindowUtils.infoBox("Login Success!");
                HomeWindow.open(username);
                dispose();
            } else {
                WindowUtils.infoBox("Invalid username or password! Register?");
            }
        } else if (e.getSource() == registerButton) {
            RegisterStudentWindow.open();
        }
    }

    public static void open() {
        if (instance == null) {
            instance = new LoginWindow();
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
