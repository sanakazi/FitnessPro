package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 1/21/2016.
 */
public class GetCustomer {
    private String statusCode;
    private CustomerList data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public CustomerList getData() {
        return data;
    }

    public void setData(CustomerList data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
