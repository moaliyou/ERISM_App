package com.example.erismapp.helpers;

public class EmployeeHelperClass {

    public static final String TABLE_NAME = "employee_tbl";
    public static final String COLUMN_ID = "employee_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    public static final String COLUMN_HIRE_DATE = "hire_date";
    public static final String COLUMN_JOB_TITLE = "job_title";
    public static final String COLUMN_SALARY = "salary";

    public static String createEmployeeTable() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                COLUMN_DATE_OF_BIRTH + " TEXT NOT NULL, " +
                COLUMN_JOB_TITLE + " TEXT NOT NULL, " +
                COLUMN_SALARY + " REAL, " +
                COLUMN_HIRE_DATE + " TEXT NOT NULL " +
                ");";
    }

    public static String deleteEmployeeTable() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static String displayDataEmployeeTable() {
        return "SELECT * FROM " + TABLE_NAME;
    }

    public static String displayEmployeeNames() {
        return "SELECT " + COLUMN_FIRST_NAME + " || ' ' || " + COLUMN_LAST_NAME +
                " FROM " + TABLE_NAME;
    }

    public static String getEmployeeId(String fullName) {
        return "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_FIRST_NAME + " || ' ' || " + COLUMN_LAST_NAME +
                " = '" + fullName + "';";
    }

    public static String getEmployeeName(String employeeId) {
        return "SELECT " + COLUMN_FIRST_NAME + " || ' ' || " + COLUMN_LAST_NAME +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID +
                " = " + employeeId + ";";
    }


}
