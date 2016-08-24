package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 2/29/2016.
 */
public class UserJsonResponse {
    private String statusCode;
    private UserId data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public UserId getData() {
        return data;
    }

    public void setData(UserId data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    private class UserId {

    }
}
