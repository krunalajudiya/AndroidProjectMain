package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponsModel {
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

        @SerializedName("id")
        @Expose
        private String empId;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("middle_name")
        @Expose
        private String middleName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("emp_code")
        @Expose
        private String empCode;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("birth_date")
        @Expose
        private String birthDate;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("blood _group")
        @Expose
        private String bloodGroup;
        @SerializedName("gender_type")
        @Expose
        private String genderType;
        @SerializedName("reporting_to")
        @Expose
        private String reportingTo;
        @SerializedName("designation")
        @Expose
        private String designation;
        @SerializedName("joining_date")
        @Expose
        private String joiningDate;
        @SerializedName("qualification")
        @Expose
        private String qualification;
        @SerializedName("official_email")
        @Expose
        private String officialEmail;
        @SerializedName("personal_email")
        @Expose
        private String personalEmail;
        @SerializedName("mobile_no")
        @Expose
        private String mobileNo;
        @SerializedName("charge_per_km")
        @Expose
        private String charge_per_km;

        public String getEmpId() {
            return empId;
        }

        public void setEmpId(String empId) {
            this.empId = empId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmpCode() {
            return empCode;
        }

        public void setEmpCode(String empCode) {
            this.empCode = empCode;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(String birthDate) {
            this.birthDate = birthDate;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBloodGroup() {
            return bloodGroup;
        }

        public void setBloodGroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
        }

        public String getGenderType() {
            return genderType;
        }

        public void setGenderType(String genderType) {
            this.genderType = genderType;
        }

        public String getReportingTo() {
            return reportingTo;
        }

        public void setReportingTo(String reportingTo) {
            this.reportingTo = reportingTo;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getJoiningDate() {
            return joiningDate;
        }

        public void setJoiningDate(String joiningDate) {
            this.joiningDate = joiningDate;
        }

        public String getQualification() {
            return qualification;
        }

        public void setQualification(String qualification) {
            this.qualification = qualification;
        }

        public String getOfficialEmail() {
            return officialEmail;
        }

        public void setOfficialEmail(String officialEmail) {
            this.officialEmail = officialEmail;
        }

        public String getPersonalEmail() {
            return personalEmail;
        }

        public void setPersonalEmail(String personalEmail) {
            this.personalEmail = personalEmail;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getCharge_per_km() {
            return charge_per_km;
        }

        public void setCharge_per_km(String charge_per_km) {
            this.charge_per_km = charge_per_km;
        }
    }

}
