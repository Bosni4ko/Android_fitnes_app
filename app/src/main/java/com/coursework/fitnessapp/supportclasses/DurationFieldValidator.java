package com.coursework.fitnessapp.supportclasses;

import android.content.Intent;

public class DurationFieldValidator {
    public static boolean validate(String text){
        boolean isValid = true;
        if(text.isEmpty() || Integer.parseInt(text) == 0) isValid = false;
        return isValid;
    }
}
