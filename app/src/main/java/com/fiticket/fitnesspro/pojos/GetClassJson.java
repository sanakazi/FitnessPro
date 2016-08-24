package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 12/10/2015.
 */
public class GetClassJson {
    private String status;
    private String status_msg;
    private ClassList data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }

    public ClassList getData() {
        return data;
    }

    public void setData(ClassList data) {
        this.data = data;
    }
}
