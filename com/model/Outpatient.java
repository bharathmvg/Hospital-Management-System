package com.model;

public class Outpatient extends Patient {
    private double registration_fees;

    public Outpatient(String patient_id, String patient_name, String phone_number, int age, String gender, String medical_history, String preffered_specialist, double medicine_fee, String patient_type, double registration_fees) {
        super(patient_id, patient_name, phone_number, age, gender, medical_history, preffered_specialist, medicine_fee, patient_type);
        this.registration_fees = registration_fees;
    }

    public double getRegistration_fees() {
        return registration_fees;
    }

    public void setRegistration_fees(double registration_fees) {
        this.registration_fees = registration_fees;
    }

    @Override
    public String toString() {
        return "Outpatient{" +
                "registration_fees=" + registration_fees +
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
