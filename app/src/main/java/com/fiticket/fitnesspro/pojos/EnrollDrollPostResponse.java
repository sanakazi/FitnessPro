package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 2/1/2016.
 */
public class EnrollDrollPostResponse {
    private String statusCode;
    private EnrollResponse data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public EnrollResponse getData() {
        return data;
    }

    public void setData(EnrollResponse data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    private class EnrollResponse {
        private int customerId;
        private int classId;

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public int getClassId() {
            return classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }
    }
}
