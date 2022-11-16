package com.coursework.fitnessapp.supportclasses;

import java.util.Arrays;
import java.util.List;

public class TimeDuration {
    private String hours = "00";
    private String minutes = "00";
    private String seconds = "00";

    public TimeDuration(String duration){
        if(duration != null){
            List<String> timeValues= Arrays.asList(duration.split(":"));
            System.out.println(duration);
            hours = timeValues.get(0);
            minutes = timeValues.get(1);
            seconds = timeValues.get(2);
        }
    }
    public TimeDuration(String hours, String minutes, String seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public TimeDuration(String minutes, String seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }


    public String getToStringDuration(){

        return (hours + ":" + minutes + ":" + seconds );
    }
}
