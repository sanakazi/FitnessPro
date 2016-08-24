package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 1/21/2016.
 */
public class TimeslotJson {
    private String startTime;
    private byte daysOfTheWeek;
    private int classId;
    private String endTime;
    private int timeslotId;

    public int getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
    }

    public byte getDaysOfTheWeek() {
        return daysOfTheWeek;
    }

    public void setDaysOfTheWeek(byte daysOfTheWeek) {
        this.daysOfTheWeek = daysOfTheWeek;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
