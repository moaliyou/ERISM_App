package com.example.erismapp.helpers;

public class RetirementPlanHelperClass {

    public static final String TABLE_NAME = "retirement_plan_tbl";
    public static final String COLUMN_ID = "plan_id";
    public static final String COLUMN_PLAN_NAME = "plan_name";
    public static final String COLUMN_PLAN_TYPE = "plan_type";
    public static final String COLUMN_EMPLOYER_CONTRIBUTION_RATE = "employer_contribution_rate";
    public static final String COLUMN_EMPLOYEE_CONTRIBUTION_RATE = "employee_contribution_rate";
    public static final String COLUMN_VESTING_PERIOD = "vesting_period";
    public static final String COLUMN_MAX_CONTRIBUTION_LIMIT = "max_contribution_limit";
    public static final String COLUMN_MIN_CONTRIBUTION_LIMIT = "min_contribution_limit";

    public static String createRetirementPlanTable() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAN_NAME + " TEXT NOT NULL, " +
                COLUMN_PLAN_TYPE + " TEXT NOT NULL, " +
                COLUMN_EMPLOYER_CONTRIBUTION_RATE + " REAL, " +
                COLUMN_EMPLOYEE_CONTRIBUTION_RATE + " REAL, " +
                COLUMN_VESTING_PERIOD + " INTEGER NOT NULL, " +
                COLUMN_MAX_CONTRIBUTION_LIMIT + " REAL, " +
                COLUMN_MIN_CONTRIBUTION_LIMIT + " REAL " +
                ");";
    }

    public static String deleteRetirementPlanTable() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static String displayDataRetirementPlanTable() {
        return "SELECT * FROM " + TABLE_NAME;
    }


}
