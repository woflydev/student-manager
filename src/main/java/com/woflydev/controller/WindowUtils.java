package com.woflydev.controller;
import javax.swing.*;

public class WindowUtils
{
    public static void infoBox(String infoMessage) {
        JOptionPane.showMessageDialog(
                null,
                infoMessage,
                "Info",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void infoBox(JScrollPane pane, String title) {
        JOptionPane.showMessageDialog(
                null,
                pane,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void errorBox(String errorMessage) {
        JOptionPane.showMessageDialog(
                null,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static int confirmationBox(String confirmMessage) {
        return JOptionPane.showConfirmDialog(
                null,
                confirmMessage,
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );
    }

    public static void refreshPanel(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }
}