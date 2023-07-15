package com.example.erismapp.helpers;

import android.content.Context;
import android.widget.Toast;

public class MyHelperClass {
    public static void showLongToastMessage(Context mContext, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
    public static void showToastMessage(Context mContext, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
