package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 1/18/2016.
 */
public class GetVenueJson {
    private String statusCode;
    private String statusMsg;
    private VenueList data;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public VenueList getData() {
        return data;
    }

    public void setData(VenueList data) {
        this.data = data;
    }



}
