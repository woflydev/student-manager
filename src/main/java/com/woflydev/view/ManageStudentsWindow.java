package com.woflydev.view;

import com.woflydev.controller.SettingsUtils;
import com.woflydev.controller.StudentUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Globals;
import com.woflydev.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ManageStudentsWindow extends JFrame {
    private static ManageStudentsWindow instance = null;

    private final JTextField searchField;
    private final JComboBox<String> searchCriteriaDropdown;
    private final JPanel resultPanel;

    private final String[] criteriaOptions = {"First Name", "Last Name", "UUID"};

    public ManageStudentsWindow() {
        WindowUtils.applyWindowSettings(
                this,
                "Manage Students",
                new Dimension(800, 400),
                new BorderLayout(),
                JFrame.DISPOSE_ON_CLOSE
        );

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 1));

        JPanel searchCriteriaPanel = new JPanel();
        searchCriteriaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchCriteriaPanel.add(new JLabel("Search by: "));
        searchCriteriaDropdown = new JComboBox<>(criteriaOptions);
        searchCriteriaPanel.add(searchCriteriaDropdown);
        inputPanel.add(searchCriteriaPanel);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Enter search term: "));
        searchField = new JTextField(20);
        searchField.addActionListener(e -> performQuery());
        searchPanel.add(searchField);
        inputPanel.add(searchPanel);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performQuery());
        inputPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);

        resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(0, 2));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        add(scrollPane, BorderLayout.CENTER);

        displayAllStudents();
    }

    private void performQuery() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        String selectedCriteria = (String) searchCriteriaDropdown.getSelectedItem();

        // depending on the input, display search results or all students
        if (searchTerm.isEmpty()) {
            displayAllStudents();
        } else {
            displaySearchResults(searchTerm, selectedCriteria);
        }
    }

    private void displaySearchResults(String term, String criteria) {
        resultPanel.removeAll();

        DefaultTableModel model = StudentUtils.searchStudentsTableModel(term, criteria);
        JTable table = createTable(model);
        resultPanel.add(new JScrollPane(table));

        WindowUtils.refreshPanel(resultPanel);
    }

    private void displayAllStudents() {
        resultPanel.removeAll();

        DefaultTableModel model = StudentUtils.searchStudentsTableModel("", "");
        JTable table = createTable(model);
        resultPanel.add(new JScrollPane(table));

        WindowUtils.refreshPanel(resultPanel);
    }

    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);

        // prevent user f*cking with the table, so it stays pretty
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setPreferredScrollableViewportSize(new Dimension(800, 400));

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int column = table.getColumnModel().getColumnIndexAtX(evt.getX());
                int row = evt.getY() / table.getRowHeight();
                if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                    if (table.getValueAt(row, column).equals("Delete")) {
                        // get the UUID -> student first...
                        Student studentToRemove = StudentUtils.getStudentByTableRow(table, row);

                        // if setting for non-confirmational delete is on, delete straight away
                        if (SettingsUtils.getSetting(Globals.SETTINGS_DELETE_WITHOUT_CONFIRM)) {
                            if (studentToRemove != null) {
                                StudentUtils.removeStudent(studentToRemove);
                                displayAllStudents();
                            } else { WindowUtils.errorBox("Error: Unable to find student to delete."); }
                        } else {
                            int option = WindowUtils.confirmationBox("Delete this record?");
                            if (option == JOptionPane.YES_OPTION) {
                                if (studentToRemove != null) {
                                    StudentUtils.removeStudent(studentToRemove);
                                    displayAllStudents();
                                } else { WindowUtils.errorBox("Error: Unable to find student to delete."); }
                            }
                        }
                    }

                    if (table.getValueAt(row, column).equals("Edit")) {
                        System.out.println("Edit button was clicked!");
                        Student studentToEdit = StudentUtils.getStudentByTableRow(table, row);
                        if (studentToEdit != null) {
                            StudentEditWindow.open(studentToEdit);
                        }
                    } else {
                        // grab UUID of student for popup
                        String uuid = (String) table.getValueAt(row, 0);
                        Student student = StudentUtils.getStudentByUUID(uuid);
                        if (student != null) {
                            StudentUtils.showStudentPopup(student);
                        } else {
                            WindowUtils.errorBox("Error: Unable to find student data.");
                        }
                    }
                }
            }
        });

        return table;
    }

    public static void open() {
        if (instance == null) {
            instance = new ManageStudentsWindow();
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
