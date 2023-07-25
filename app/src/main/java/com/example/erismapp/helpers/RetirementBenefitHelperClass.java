package com.example.erismapp.helpers;

public class RetirementBenefitHelperClass {

    public static final String TABLE_NAME = "retirement_benefit_tbl";
    public static final String COLUMN_ID = "benefit_id";
    public static final String COLUMN_EMPLOYEE_ID = EmployeeHelperClass.COLUMN_ID;
    public static final String COLUMN_BENEFIT_TYPE = "benefit_type";
    public static final String COLUMN_PLAN_ID = "plan_id";
    public static final String COLUMN_CONTRIBUTION_AMOUNT = "contribution_amount";
    public static final String COLUMN_CONTRIBUTION_FREQUENCY = "contribution_frequency";
    public static final String COLUMN_BENEFIT_START_DATE = "benefit_start_date";
    public static final String COLUMN_BENEFIT_END_DATE = "benefit_end_date";

    public static String createRetirementBenefitTable() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMPLOYEE_ID + " INTEGER NOT NULL, " +
                COLUMN_BENEFIT_TYPE + " TEXT NOT NULL, " +
                COLUMN_PLAN_ID + " INTEGER NOT NULL, " +
                COLUMN_CONTRIBUTION_AMOUNT + " REAL, " +
                COLUMN_CONTRIBUTION_FREQUENCY + " TEXT NOT NULL, " +
                COLUMN_BENEFIT_START_DATE + " TEXT NOT NULL, " +
                COLUMN_BENEFIT_END_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_EMPLOYEE_ID + ")" +
                " REFERENCES " + EmployeeHelperClass.TABLE_NAME + " (" + COLUMN_EMPLOYEE_ID + ")" +
                " ON UPDATE CASCADE " +
                " ON DELETE CASCADE );";
    }

    public static String deleteRetirementBenefitTable() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static String displayDataRetirementBenefitTable() {
        return "SELECT * FROM " + TABLE_NAME;
    }

    public static String displayBenefitTypes() {
        return "SELECT DISTINCT " + COLUMN_BENEFIT_TYPE + " FROM " + TABLE_NAME;
    }

    public static String displayBenefitTypesFor(String employeeId) {
        return "SELECT " + COLUMN_BENEFIT_TYPE + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_EMPLOYEE_ID + " = " + employeeId;
    }

    public static String getBenefitTypes(String benefitId) {
        return "SELECT " + COLUMN_BENEFIT_TYPE + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID + " = " + benefitId;
    }

    public static String getBenefitId(String benefitType, String employeeId) {
        return "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_BENEFIT_TYPE + " = '" + benefitType + "' AND " +
                COLUMN_EMPLOYEE_ID + " = " + employeeId + ";";
    }

}
