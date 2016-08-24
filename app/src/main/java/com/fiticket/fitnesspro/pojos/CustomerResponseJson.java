package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 2/9/2016.
 */
public class CustomerResponseJson {
    private String statusCode;
    private CustomerId data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public CustomerId getData() {
        return data;
    }

    public void setData(CustomerId data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    private class CustomerId {
        private int customerId;

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }
    }
}
