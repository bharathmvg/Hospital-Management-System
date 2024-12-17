package com.model;

public class Patient {
    protected String patientId;
    protected String patientName;
    protected String phoneNumber;
    protected int age;
    protected String gender;
    protected String medicalHistory;
    protected String preferredSpecialist;
    protected double medicineFee;
    protected String patientType;

    public Patient(String patientId, String patientName, String phoneNumber, int age, String gender, String medicalHistory, String preferredSpecialist, double medicineFee, String patientType) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.gender = gender;
        this.medicalHistory = medicalHistory;
        this.preferredSpecialist = preferredSpecialist;
        this.medicineFee = medicineFee;
        this.patientType = patientType;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getPreferredSpecialist() {
        return preferredSpecialist;
    }

    public void setPreferredSpecialist(String preferredSpecialist) {
        this.preferredSpecialist = preferredSpecialist;
    }

    public double getMedicineFee() {
        return medicineFee;
    }

    public void setMedicineFee(double medicineFee) {
        this.medicineFee = medicineFee;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }
}
