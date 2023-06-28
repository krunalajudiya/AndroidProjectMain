package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PunchInModel {
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

        @SerializedName("atte_id")
        @Expose
        private String atteId;
        @SerializedName("emp_id")
        @Expose
        private String empId;
        @SerializedName("punch_in_time")
        @Expose
        private String punchInTime;
        @SerializedName("punch_out_time")
        @Expose
        private String punchOutTime;
        @SerializedName("work_hour")
        @Expose
        private String workHour;
        @SerializedName("leave_type")
        @Expose
        private String leaveType;
        @SerializedName("leave_description")
        @Expose
        private String leaveDescription;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public String getAtteId() {
            return atteId;
        }

        public void setAtteId(String atteId) {
            this.atteId = atteId;
        }

        public String getEmpId() {
            return empId;
        }

        public void setEmpId(String empId) {
            this.empId = empId;
        }

        public String getPunchInTime() {
            return punchInTime;
        }

        public void setPunchInTime(String punchInTime) {
            this.punchInTime = punchInTime;
        }

        public String getPunchOutTime() {
            return punchOutTime;
        }

        public void setPunchOutTime(String punchOutTime) {
            this.punchOutTime = punchOutTime;
        }

        public String getWorkHour() {
            return workHour;
        }

        public void setWorkHour(String workHour) {
            this.workHour = workHour;
        }

        public String getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(String leaveType) {
            this.leaveType = leaveType;
        }

        public String getLeaveDescription() {
            return leaveDescription;
        }

        public void setLeaveDescription(String leaveDescription) {
            this.leaveDescription = leaveDescription;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
