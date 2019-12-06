package com.example.weitblickapp_android.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String emailAdd;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName /*, String emailAdd */) {
        this.displayName = displayName;
        //this.emailAdd = emailAdd;
    }

    String getDisplayName() {
        return displayName;
    }
    String getEmailAdd(){return emailAdd;}
}
