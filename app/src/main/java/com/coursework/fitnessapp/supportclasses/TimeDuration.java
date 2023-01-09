package com.coursework.fitnessapp.supportclasses;

import java.util.Arrays;
import java.util.List;

//Class to store workout and exercise duration
public class TimeDuration {
    private String hours = "00";
    private String minutes = "00";
    private String seconds = "00";

    public TimeDuration(String duration){
        if(duration != null){
            List<String> timeValues= Arrays.asList(duration.split(":"));
            hours = timeValues.get(0);
            minutes = timeValues.get(1);
            seconds = timeValues.get(2);
        }
    }
    public TimeDuration(int seconds){
        hours = String.valueOf(seconds / 3600);
        if(hours.length() < 2){
            hours = '0' + hours;
        }
        seconds = seconds%3600;
        minutes = String.valueOf(seconds / 60);
        if(minutes.length() < 2){
            minutes = '0' + minutes;
        }
        seconds = seconds%60;
        this.seconds = String.valueOf(seconds);
        if(this.seconds.length() < 2){
            this.seconds = '0' + this.seconds;
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


    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getToStringDuration(){

        return (hours + ":" + minutes + ":" + seconds );
    }
    public Integer getTimeInSeconds(){
        Integer timeInSeconds;
        timeInSeconds = Integer.parseInt(hours) * 3600 + Integer.parseInt(minutes) * 60 + Integer.parseInt(seconds);
        return timeInSeconds;
    }

    public void setTime(int seconds){
        hours = String.valueOf(seconds / 3600);
        if(hours.length() < 2){
            hours = '0' + hours;
        }
        seconds = seconds%3600;
        minutes = String.valueOf(seconds / 60);
        if(minutes.length() < 2){
            minutes = '0' + minutes;
        }
        seconds = seconds%60;
        this.seconds = String.valueOf(seconds);
        if(this.seconds.length() < 2){
            this.seconds = '0' + this.seconds;
        }
    }
}
