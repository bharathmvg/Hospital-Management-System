package com.model;

public class Treatment {
  private String name;
  private double priceRange;
  private String specialization;

  public Treatment(String name, double priceRange, String specialization) {
    this.name = name;
    this.priceRange = priceRange;
    this.specialization = specialization;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPriceRange() {
    return priceRange;
  }

  public void setPriceRange(double priceRange) {
    this.priceRange = priceRange;
  }

  public String getSpecialization() {
    return specialization;
  }

  public void setSpecialization(String specialization) {
    this.specialization = specialization;
  }
}
