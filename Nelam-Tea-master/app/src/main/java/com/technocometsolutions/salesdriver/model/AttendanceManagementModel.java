package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceManagementModel {
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class Item {

        @SerializedName("punch_in_time")
        @Expose
        private String punchInTime;
        @SerializedName("punch_in_date")
        @Expose
        private String punchInDate;
        @SerializedName("work_hour")
        @Expose
        private String workHour;
        @SerializedName("punch_out_time")
        @Expose
        private String punchOutTime;
        @SerializedName("punch_out_date")
        @Expose
        private String punchOutDate;

        public String getPunchInTime() {
            return punchInTime;
        }

        public void setPunchInTime(String punchInTime) {
            this.punchInTime = punchInTime;
        }

        public String getPunchInDate() {
            return punchInDate;
        }

        public void setPunchInDate(String punchInDate) {
            this.punchInDate = punchInDate;
        }

        public String getWorkHour() {
            return workHour;
        }

        public void setWorkHour(String workHour) {
            this.workHour = workHour;
        }

        public String getPunchOutTime() {
            return punchOutTime;
        }

        public void setPunchOutTime(String punchOutTime) {
            this.punchOutTime = punchOutTime;
        }

        public String getPunchOutDate() {
            return punchOutDate;
        }

        public void setPunchOutDate(String punchOutDate) {
            this.punchOutDate = punchOutDate;
        }

    }
}
