package com.model;

import java.time.LocalTime;
import java.util.Date;

public class Doctor {
  private String doctor_id;
  private String doctor_name;
  private double doctor_fee;
  private String specialization;
  private Date available_date;
  private LocalTime available_time;

  public Doctor(String doctor_id, String doctor_name, double doctor_fee, String specialization, Date available_date, LocalTime available_time) {
    this.doctor_id = doctor_id;
    this.doctor_name = doctor_name;
    this.doctor_fee = doctor_fee;
    this.specialization = specialization;
    this.available_date = available_date;
    this.available_time = available_time;
  }

  public String getDoctor_id() {
    return doctor_id;
  }

  public void setDoctor_id(String doctor_id) {
    this.doctor_id = doctor_id;
  }

  public String getDoctor_name() {
    return doctor_name;
  }

  public void setDoctor_name(String doctor_name) {
    this.doctor_name = doctor_name;
  }

  public double getDoctor_fee() {
    return doctor_fee;
  }

  public void setDoctor_fee(double doctor_fee) {
    this.doctor_fee = doctor_fee;
  }

  public String getSpecialization() {
    return specialization;
  }

  public void setSpecialization(String specialization) {
    this.specialization = specialization;
  }

  public Date getAvailable_date() {
    return available_date;
  }

  public void setAvailable_date(Date available_date) {
    this.available_date = available_date;
  }

  public LocalTime getAvailable_time() {
    return available_time;
  }

  public void setAvailable_time(LocalTime available_time) {
    this.available_time = available_time;
  }

  @Override
  public String toString() {
    return "Doctor{" +
            "doctor_id='" + doctor_id + '\'' +
            ", doctor_name='" + doctor_name + '\'' +
            ", doctor_fee=" + doctor_fee +
            ", specialization='" + specialization + '\'' +
            ", available_date='" + available_date + '\'' +
            ", available_time='" + available_time + '\'' +
            '}';
  }
}
