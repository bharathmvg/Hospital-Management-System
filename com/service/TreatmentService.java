package com.service;

import com.model.Treatment;

import java.util.ArrayList;
import java.util.List;

public class TreatmentService {
  public List<Treatment> getTreatmentList() {
    List<Treatment> treatments = new ArrayList<>();
    treatments.add(new Treatment("Coronary Angioplasty", 300000, "Cardiologist"));
    treatments.add(new Treatment("Skin Allergy Testing", 20000, "Dermatologist"));
    treatments.add(new Treatment("MRI Brain Scan", 75000, "Neurologist"));
    treatments.add(new Treatment("Knee Replacement Surgery", 500000, "Orthopedic Surgeon"));
    treatments.add(new Treatment("Child Vaccination", 12000, "Pediatrician"));
    treatments.add(new Treatment("Colonoscopy", 85000, "Gastroenterologist"));
    treatments.add(new Treatment("X-ray Analysis", 10000, "Radiologist"));
    treatments.add(new Treatment("Cognitive Behavioral Therapy", 18000, "Psychiatrist"));
    treatments.add(new Treatment("Prostate Exam", 25000, "Urologist"));
    treatments.add(new Treatment("Thyroid Function Test", 15000, "Endocrinologist"));

    return treatments;
  }
}
