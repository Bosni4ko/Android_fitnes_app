package com.coursework.fitnessapp.supportclasses;

//#Class to validate duration field
public class DurationFieldValidator {
    public static boolean validate(String text){
        boolean isValid = true;
        if(text.isEmpty() || Integer.parseInt(text) == 0) isValid = false;
        return isValid;
    }
}
