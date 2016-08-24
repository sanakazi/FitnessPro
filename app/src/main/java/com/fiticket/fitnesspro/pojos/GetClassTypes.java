package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 2/18/2016.
 */
public class GetClassTypes {
    private String statusCode;
    private ClassTypeList data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ClassTypeList getData() {
        return data;
    }

    public void setData(ClassTypeList data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
