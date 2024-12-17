package com.model;

import java.util.Date;

public class Allocation {
    private String allocationId;
    private String patientId;
    private int roomNumber;
    private int noOfDaysAdmitted;
    private Date admissionDate;
    private Date dischargeDate;
    private String treatment;
    private String roomType;
    private String wantFood;

    public Allocation(String allocationId, String patientId, int roomNumber, int noOfDaysAdmitted, Date admissionDate, Date dischargeDate, String treatment, String roomType, String wantFood) {
        this.allocationId = allocationId;
        this.patientId = patientId;
        this.roomNumber = roomNumber;
        this.noOfDaysAdmitted = noOfDaysAdmitted;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.treatment = treatment;
        this.roomType = roomType;
        this.wantFood = wantFood;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public String getPatientId() {
        return patientId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getNoOfDaysAdmitted() {
        return noOfDaysAdmitted;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getWantFood() {
        return wantFood;
    }

    @Override
    public String toString() {
        return "Allocation{" +
                "allocationId='" + allocationId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", roomNumber=" + roomNumber +
                ", noOfDaysAdmitted=" + noOfDaysAdmitted +
                ", admissionDate=" + admissionDate +
                ", dischargeDate=" + dischargeDate +
                ", treatment='" + treatment + '\'' +
                ", roomType='" + roomType + '\'' +
                ", wantFood='" + wantFood + '\'' +
                '}';
    }
}
