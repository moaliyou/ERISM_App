package com.example.erismapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.erismapp.helpers.EmployeeHelperClass;
import com.example.erismapp.helpers.PayoutHelperClass;
import com.example.erismapp.helpers.RetirementBenefitHelperClass;
import com.example.erismapp.helpers.RetirementPlanHelperClass;
import com.example.erismapp.helpers.UserHelperClass;

import java.util.HashMap;
import java.util.Map;

public class EmployeeRetirementDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "employee_retirement_db";
    private static final int DATABASE_VERSION = 1;

    public EmployeeRetirementDatabase(@Nullable Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(EmployeeHelperClass.createEmployeeTable());
        sqLiteDatabase.execSQL(RetirementBenefitHelperClass.createRetirementBenefitTable());
        sqLiteDatabase.execSQL(UserHelperClass.createUserTable());
        sqLiteDatabase.execSQL(RetirementPlanHelperClass.createRetirementPlanTable());
        sqLiteDatabase.execSQL(PayoutHelperClass.createPayoutTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(EmployeeHelperClass.deleteEmployeeTable());
        sqLiteDatabase.execSQL(RetirementBenefitHelperClass.deleteRetirementBenefitTable());
        sqLiteDatabase.execSQL(RetirementPlanHelperClass.deleteRetirementPlanTable());
        sqLiteDatabase.execSQL(PayoutHelperClass.deletePayoutTable());
        sqLiteDatabase.execSQL(UserHelperClass.deleteUserTable());
        onCreate(sqLiteDatabase);
    }

    public void insertData(
            String tableName,
            HashMap<String, String> dataList
    ) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            database.beginTransaction();

            if (dataList != null && dataList.size() > 0) {

                for (Map.Entry<String, String> data : dataList.entrySet())
                    contentValues.put(data.getKey(), data.getValue());

                long codeResult = database.insert(tableName, null, contentValues);

                if (codeResult != -1) {
                    database.setTransactionSuccessful();
                }

            }

        } catch (Exception e) {
            database.endTransaction();
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }

    }

    public void updateData(
            String tableName,
            String columnConditionName,
            String idToUpdate,
            HashMap<String, String> dataList
    ) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            database.beginTransaction();

            if (dataList != null && dataList.size() > 0) {

                for (Map.Entry<String, String> data : dataList.entrySet())
                    contentValues.put(data.getKey(), data.getValue());

                long codeResult = database
                        .update(
                                tableName,
                                contentValues,
                                columnConditionName + "=?",
                                new String[]{idToUpdate}
                        );

                if (codeResult != -1) {
                    database.setTransactionSuccessful();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }

    }

    public Cursor readDataFrom(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = null;

        if (db != null) {
            mCursor = db.rawQuery(query, null);
        }

        return mCursor;
    }

    public void deleteById(String tableName, String columnConditionName, String idToDelete) {
        SQLiteDatabase database = this.getWritableDatabase();

        database
                .delete(
                        tableName,
                        columnConditionName + "=?",
                        new String[]{idToDelete}
                );

    }


}
