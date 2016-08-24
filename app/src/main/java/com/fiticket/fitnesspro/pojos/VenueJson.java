package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 12/9/2015.
 */
public class VenueJson {
    private int tenantId;
    private int venueId;
    private String venueName;
    private String email;
    private String address;
    private String city;
   /* private String state;*/
    private int pinCode;
    private String landmark;
    private String area;
    private String contactNumber;
    private double latitude;
    private double longitude;
    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String venueEmail) {
        this.email = venueEmail;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

   /* public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }*/

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pincode) {
        this.pinCode = pincode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

