package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 1/8/2016.
 */
public class ScheduleJson {
    private String classNumber;
    private String dayofWeek;
    private String startTime;
    private String endTime;
    public String getClassNumber()
    {
        return classNumber;
    }
    public void setClassNumber(String classNumber)
    {
        this.classNumber=classNumber;

    }

    public String getDayofWeek() {
        return dayofWeek;
    }

    public void setDayofWeek(String dayofWeek) {
        this.dayofWeek = dayofWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
