package com.example.erismapp.helpers;

public class PayoutHelperClass {

    public static final String TABLE_NAME = "payout_tbl";
    public static final String COLUMN_ID = "payout_id";
    public static final String COLUMN_EMPLOYEE_ID = EmployeeHelperClass.COLUMN_ID;
    public static final String COLUMN_BENEFIT_ID = RetirementBenefitHelperClass.COLUMN_ID;
    public static final String COLUMN_AMOUNT = "contribution_amount";
    public static final String COLUMN_PAYOUT_DATE = "payout_date";

    public static String createPayoutTable() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMPLOYEE_ID + " INTEGER NOT NULL, " +
                COLUMN_BENEFIT_ID + " INTEGER NOT NULL, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_PAYOUT_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_EMPLOYEE_ID + ")" +
                " REFERENCES " + EmployeeHelperClass.TABLE_NAME + " (" + COLUMN_EMPLOYEE_ID + ")" +
                " ON UPDATE CASCADE " +
                " ON DELETE CASCADE );";
    }

    public static String deletePayoutTable() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static String displayDataPayoutTable() {
        return "SELECT * FROM " + TABLE_NAME;
    }

}
