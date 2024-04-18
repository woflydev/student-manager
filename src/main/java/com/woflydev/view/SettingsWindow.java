package com.woflydev.view;

import com.woflydev.controller.SettingsUtils;
import com.woflydev.model.Globals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class SettingsWindow extends JFrame {
    private static SettingsWindow instance = null;
    private static Map<String, Boolean> settingsMap;

    private static JCheckBox deleteQuickCheckbox;
    private static JCheckBox normalizeNamesCheckbox;

    public SettingsWindow() {
        setTitle("Settings");
        setSize(300, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS)); // Vertical layout

        deleteQuickCheckbox = new JCheckBox("Delete without confirmation?");
        settingsPanel.add(deleteQuickCheckbox);

        normalizeNamesCheckbox = new JCheckBox("Normalize names?");
        settingsPanel.add(normalizeNamesCheckbox);

        mainPanel.add(settingsPanel, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeSettings();
                dispose();
            }
        });
        mainPanel.add(saveBtn, BorderLayout.SOUTH);

        populateFields();

        add(mainPanel);
    }

    private void populateFields() {
        settingsMap = SettingsUtils.loadSettings();
        if (settingsMap.containsKey(Globals.SETTINGS_DELETE_WITHOUT_CONFIRM)) {
            deleteQuickCheckbox.setSelected(settingsMap.get(Globals.SETTINGS_DELETE_WITHOUT_CONFIRM));
        }

        if (settingsMap.containsKey(Globals.SETTINGS_NORMALIZE_NAMES)) {
            normalizeNamesCheckbox.setSelected(settingsMap.get(Globals.SETTINGS_NORMALIZE_NAMES));
        }
    }

    private void writeSettings() {
        settingsMap.put(Globals.SETTINGS_DELETE_WITHOUT_CONFIRM, deleteQuickCheckbox.isSelected());
        settingsMap.put(Globals.SETTINGS_NORMALIZE_NAMES, normalizeNamesCheckbox.isSelected());
        SettingsUtils.saveSettings(settingsMap);
    }

    public static void open() {
        if (instance == null) {
            instance = new SettingsWindow();
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
