package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 1/21/2016.
 */
public class CreateTimeSlotResponse {
    private String statusCode;
    private TimeSlotResponse data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public TimeSlotResponse getData() {
        return data;
    }

    public void setData(TimeSlotResponse data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    private class TimeSlotResponse {
        TimeSlotResponseJson[] timeslotList;

        public TimeSlotResponseJson[] getTimeslotList() {
            return timeslotList;
        }

        public void setTimeslotList(TimeSlotResponseJson[] timeslotList) {
            this.timeslotList = timeslotList;
        }
    }
}
