package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 1/21/2016.
 */
public class LoginResponseJson {
    private String statusCode;
    private DataJson data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public DataJson getData() {
        return data;
    }

    public void setData(DataJson data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public class DataJson {
        int tenantId;
        String contactNumber;
        String address;
        String email;
        int userId;
        String roleName;
        String venueName;
        String tenantName;
        int roleId;
        String sessionToken;
        Long expiryDate;
        String userName;
        String displayName;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getSessionToken() {
            return sessionToken;
        }

        public void setSessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
        }

        public Long getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(Long expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getTenantId() {
            return tenantId;
        }

        public void setTenantId(int tenantId) {
            this.tenantId = tenantId;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getVenueName() {
            return venueName;
        }

        public void setVenueName(String venueName) {
            this.venueName = venueName;
        }

        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
        }

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [tenantId = "+tenantId+", sessionToken = "+sessionToken+", contactNumber = "+contactNumber+", email = "+email+", address = "+address+", expiryDate = "+expiryDate+", userId = "+userId+", userName = "+userName+", roleName = "+roleName+", displayName = "+displayName+", tenantName = "+tenantName+", roleId = "+roleId+"]";
        }
    }
}
