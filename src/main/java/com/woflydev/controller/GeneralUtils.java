package com.woflydev.controller;

public class GeneralUtils {
    public static boolean hasInteger(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    public static double similarity(String s1, String s2) {
        int maxLength = Math.max(s1.length(), s2.length());
        int common = 0;
        for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                common++;
            }
        }
        return (double) common / maxLength;
    }
}
