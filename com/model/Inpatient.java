package com.model;

public class Inpatient extends Patient {
    private double admissionFees;
    private String treatment;
    private String roomType;
    private String wantFood;

    public Inpatient(String patientId, String patientName, String phoneNumber, int age, String gender, String medicalHistory, String preferredSpecialist, double medicineFee, String patientType, double admissionFees, String treatment, String roomType, String wantFood) {
        super(patientId, patientName, phoneNumber, age, gender, medicalHistory, preferredSpecialist, medicineFee, patientType);
        this.admissionFees = admissionFees;
        this.treatment = treatment;
        this.roomType = roomType;
        this.wantFood = wantFood;
    }

    public double getAdmissionFees() {
        return admissionFees;
    }

    public void setAdmissionFees(double admissionFees) {
        this.admissionFees = admissionFees;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getWantFood() {
        return wantFood;
    }

    public void setWantFood(String wantFood) {
        this.wantFood = wantFood;
    }

    @Override
    public String toString() {
        return "Inpatient{" +
                "admissionFees=" + admissionFees +
                ", treatment='" + treatment + '\'' +
                ", roomType='" + roomType + '\'' +
                ", wantFood='" + wantFood + '\'' +
                ", patientId='" + patientId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                ", preferredSpecialist='" + preferredSpecialist + '\'' +
                ", medicineFee=" + medicineFee +
                ", patientType='" + patientType + '\'' +
                "} " + super.toString();
    }
}
