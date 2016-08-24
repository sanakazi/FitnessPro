package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 2/28/2016.
 */
public class VenueResponseJson {
    private String statusCode;
    private VenueId data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public VenueId getData() {
        return data;
    }

    public void setData(VenueId data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    private class VenueId {
        private int venueId;

        public int getVenueId() {
            return venueId;
        }

        public void setVenueId(int venueId) {
            this.venueId = venueId;
        }
    }
}
