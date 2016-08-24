package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 1/31/2016.
 */
public class GetCustomerByClassId {
    private String statusCode;
    private CustomerClassList data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public CustomerClassList getData() {
        return data;
    }

    public void setData(CustomerClassList data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
