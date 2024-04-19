package com.woflydev.view;

import com.woflydev.controller.StudentUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.view.skeleton.StudentInformation;

public class StudentRegisterWindow extends StudentInformation {

    @Override
    protected boolean fieldsValid() {
        String username = super.getUsernameField().getText();
        if (super.fieldsValid() && !StudentUtils.usernameExists(username)) return true;
        WindowUtils.errorBox("Username already exists. Please choose a different username.");
        return false;
    }

    // this works because it's a shared instance,
    // i.e. the instance is protected and shared by parent
    public static void open() {
        if (instance == null) {
            instance = new StudentRegisterWindow();
            instance.setVisible(true);
        }
    }
}