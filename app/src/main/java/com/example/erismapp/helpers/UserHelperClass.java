package com.example.erismapp.helpers;

public class UserHelperClass {

    public static final String TABLE_NAME = "user_tbl";
    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_PASSWORD = "password";

    public static String createUserTable() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL " +
                ");";
    }

    public static String deleteUserTable() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static String displayUserDataTable() {
        return "SELECT * FROM " + TABLE_NAME;
    }

    public static String checkUserInfo(String username, String password) {
        return "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_USERNAME + " = '" + username +
                "' AND " + COLUMN_PASSWORD + " = '" + password + "';";
    }

    public static String getUserDate(String fullName) {
        return "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_FULL_NAME + " = '" + fullName + "';";
    }

    public static String getUserPassword(String username) {
        return "SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_USERNAME + " = '" + username + "';";
    }

}
