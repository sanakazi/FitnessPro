package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 1/27/2016.
 */
public class GetTimeSlots {
    private String statusCode;
    private TimeslotList data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public TimeslotList getData() {
        return data;
    }

    public void setData(TimeslotList data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
