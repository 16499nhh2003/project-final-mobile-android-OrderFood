package com.example.myapplication.finalMobile.Utils.Validator;

import android.text.TextUtils;
import android.util.Patterns;

public class EmailUtils {
    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
