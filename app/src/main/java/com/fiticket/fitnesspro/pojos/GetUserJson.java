package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 2/14/2016.
 */
public class GetUserJson {
    private String statusCode;
    private UserList data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public UserList getData() {
        return data;
    }

    public void setData(UserList data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
