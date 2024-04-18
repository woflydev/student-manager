package com.woflydev.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeWindow extends JFrame {
    public HomeWindow() {
        setTitle("Home");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton createStudentBtn = new JButton("Create Student");
        createStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateStudent.open();
            }
        });
        add(createStudentBtn);

        JButton mngStudentBtn = new JButton("Manage Students");
        mngStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageStudents.open();
            }
        });
        add(mngStudentBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.out.println("Going offline!");
                System.exit(0);
            }
        });
        add(exitBtn);
    }

    public static void open() {
        HomeWindow frame = new HomeWindow();
        frame.setVisible(true);
    }
}
