package com.woflydev.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeWindow extends JFrame {
    private static HomeWindow instance = null;

    private JLabel loggedInUserLabel;
    private String loggedInUserName;

    public HomeWindow(String loggedInUserName) {
        setTitle("Home");
        setSize(300, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));

        JButton createStudentBtn = new JButton("Create Student");
        createStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterStudentWindow.open();
            }
        });
        buttonPanel.add(createStudentBtn);

        JButton mngStudentBtn = new JButton("Manage Students");
        mngStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageStudentsWindow.open();
            }
        });
        buttonPanel.add(mngStudentBtn);

        JButton settingsBtn = new JButton("Settings");
        settingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsWindow.open();
            }
        });
        buttonPanel.add(settingsBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.out.println("Going offline!");
                System.exit(0);
            }
        });
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        loggedInUserLabel = new JLabel("Logged in as: " + loggedInUserName);
        loggedInUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(loggedInUserLabel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static void open(String user) {
        HomeWindow frame = new HomeWindow(user);
        frame.setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (instance != null) {
            instance = null;
        }
    }
}
