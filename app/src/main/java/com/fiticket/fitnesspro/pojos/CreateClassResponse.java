package com.fiticket.fitnesspro.pojos;

/**
 * Created by InFinItY on 1/21/2016.
 */
public class CreateClassResponse {
    private String statusCode;
    private ClassId data;
    private String statusMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ClassId getData() {
        return data;
    }

    public void setData(ClassId data) {
        this.data = data;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public class ClassId {
        private int classId;

        public int getClassId() {
            return classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }
    }


}
