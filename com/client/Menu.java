package com.client;

public class Menu {
  public void title() {
    System.out.println("+" + "—".repeat(51) + "+");
    System.out.print("│ 🏥 Welcome to APOLLO MULTI SPECIALITY HOSPITAL 🏥 │\n");
    System.out.println("+" + "—".repeat(51) + "+");
    System.out.println();
  }

  // ---------------------- MAIN MENU ----------------------
  public void mainMenu() {
    System.out.println("+" + "—".repeat(22) + "+");
    System.out.printf("│ %-20S │\n", "Sections");
    System.out.println("+" + "—".repeat(22) + "+");
    System.out.printf("│ %-20s │\n", "1. Doctor Section");
    System.out.printf("│ %-20s │\n", "2. Patient Section");
    System.out.printf("│ %-20s │\n", "3. Exit Application");
    System.out.println("+" + "—".repeat(22) + "+");
  }

  public void invalidId() {
    System.out.println("The entered ID is invalid. Please recheck the ID and try again.");
  }

  // ---------------------- DOCTOR RELATED OPTIONS ----------------------
  public void doctorMenu() {
    System.out.println("+" + "—".repeat(38) + "+");
    System.out.printf("│ %-36S │\n", "Doctor Panel");
    System.out.println("+" + "—".repeat(38) + "+");
    System.out.printf("│ %-36s │\n", "1. Add Doctor");
    System.out.printf("│ %-36s │\n", "2. Update Doctor's Fee");
    System.out.printf("│ %-36s │\n", "3. Update Doctor's Availability Date");
    System.out.printf("│ %-36s │\n", "4. Retrieve Doctor Details");
    System.out.printf("│ %-36s │\n", "5. Retrieve All Doctors' Details");
    System.out.printf("│ %-36s │\n", "6. Return to Previous Menu");
    System.out.println("+" + "—".repeat(38) + "+");
  }

  public void doctorInputFormat() {
    System.out.println("+" + "—".repeat(74) + "+");
    System.out.println("│ Format: <name>:<fees>:<specialization>:<available_date>:<available_time> │");
    System.out.println("│ Example: John:200:Cardiologist:dd/MM/yy:hh:mm                            │");
    System.out.println("+" + "—".repeat(74) + "+");
  }

  public void allDoctorDetails() {
    System.out.println("+" + "—".repeat(108) + "+");
    System.out.printf("│ %-11S │ %-18S │ %-10S │ %-22S │ %-15S │ %-15S │\n", "Doctor ID", "Doctor Name", "Fee", "Specialization", "Available Date", "Available Time");
    System.out.println("+" + "—".repeat(108) + "+");
  }

  // ---------------------- PATIENT RELATED OPTIONS ----------------------
  public void patientMenu() {
    System.out.println("+" + "—".repeat(37) + "+");
    System.out.printf("│ %-35S │\n", "Patient Panel");
    System.out.println("+" + "—".repeat(37) + "+");
    System.out.printf("│ %-35s │\n", "1. Inpatient Services");
    System.out.printf("│ %-35s │\n", "2. Outpatient Services");
    System.out.printf("│ %-35s │\n", "3. Billing and Payments");
    System.out.printf("│ %-35s │\n", "4. Return to Previous Menu");
    System.out.println("+" + "—".repeat(37) + "+");
  }

  // ---------------------- INPATIENT RELATED OPTIONS ----------------------
  public void inPatientMenu() {
    System.out.println("+" + "—".repeat(44) + "+");
    System.out.printf("│ %-42S │\n", "Inpatient Services");
    System.out.println("+" + "—".repeat(44) + "+");
    System.out.printf("│ %-42s │\n", "1. Add Patient Records");
    System.out.printf("│ %-42s │\n", "2. Update Patient Contact Details");
    System.out.printf("│ %-42s │\n", "3. Update Room Type for Patients");
    System.out.printf("│ %-42s │\n", "4. Display Available Room Types");
    System.out.printf("│ %-42s │\n", "5. Update Meal Preferences for Patients");
    System.out.printf("│ %-42s │\n", "6. Retrieve Patient Details");
    System.out.printf("│ %-42s │\n", "7. Bed Allocation Queries");
    System.out.printf("│ %-42s │\n", "8. Retrieve All Patient Records");
    System.out.printf("│ %-42s │\n", "9. Remove Patient Records");
    System.out.printf("│ %-42s │\n", "10. Return to Previous Menu");
    System.out.println("+" + "—".repeat(44) + "+");
  }

  public void roomsList() {
    System.out.println("+" + "—".repeat(38) + "+");
    System.out.printf("│ %-17s │ %-16s │\n", "Room", "Price (per hour)");
    System.out.println("+" + "—".repeat(38) + "+");
    System.out.printf("│ %-17s │ %-16s │\n", "General Ward", "₹100");
    System.out.printf("│ %-17s │ %-16s │\n", "Semi-Private", "₹270");
    System.out.printf("│ %-17s │ %-16s │\n", "Private", "₹500");
    System.out.printf("│ %-17s │ %-16s │\n", "Deluxe", "₹800");
    System.out.printf("│ %-17s │ %-16s │\n", "Suite", "₹1500");
    System.out.printf("│ %-17s │ %-16s │\n", "ICU", "₹2500");
    System.out.printf("│ %-17s │ %-16s │\n", "HDU", "₹2000"); //(High Dependency Unit)
    System.out.printf("│ %-17s │ %-16s │\n", "Maternity", "₹1200");
    System.out.printf("│ %-17s │ %-16s │\n", "Pediatric", "₹1000");
    System.out.printf("│ %-17s │ %-16s │\n", "Isolation", "₹1800");
    System.out.println("+" + "—".repeat(38) + "+");
  }

  public void allInpatientDetails() {
    System.out.println("+" + "—".repeat(197) + "+");
    System.out.printf("│ %-12s │ %-18s │ %-12s │ %-3s │ %-10s │ %-15s │ %-20s │ %-12s │ %-15s │ %-25s │ %-16s │ %-4s │\n",
            "PATIENT_ID", "PATIENT_NAME", "PHONE_NUMBER", "AGE", "GENDER", "MEDICAL_HISTORY", "PREFERRED_SPECIALIST", "MEDICINE_FEE", "ADMISSION_FEES", "TREATMENT", "ROOM_TYPE", "FOOD");
    System.out.println("+" + "—".repeat(197) + "+");
  }

  public void inpatientInputFormat() {
    System.out.println("+" + "—".repeat(163) + "+");
    System.out.println("│ Format: <patient_name>:<phone_number>:<age>:<gender>:<medical_history>:<preferred_specialist>:<medicine_fee>:<admission_fees>:<treatment>:<room_type>:<want_food> │");
    System.out.println("│ Example: John Doe:9876543210:45:Male:Diabetes:Cardiologist:1500.50:5000.00:Colonoscopy:Deluxe:Yes                                                                 │");
    System.out.println("+" + "—".repeat(163) + "+");
  }

//  public void treatmentList() {
//    // Table header
//    System.out.println("+" + "—".repeat(98) + "+");
//    System.out.printf("│ %-29s │ %-28s │ %-28s │%n", "Treatment", "Price (Approx in INR)", "Doctor's Specialization");
//    System.out.println("+" + "—".repeat(98) + "+");
//
//    // Table rows
//    System.out.printf("│ %-29s │ %-28s │ %-28s │%n", "Coronary Angioplasty", "₹2,50,000 - ₹4,00,000", "Cardiologist");
//    System.out.printf("│ %-29s │ %-28s │ %-28s │%n", "Skin Allergy Testing", "₹16,000 - ₹32,000", "Dermatologist");
//    System.out.printf("│ %-29s │ %-28s │ %-28s │%n", "MRI Brain Scan", "₹65,000 - ₹1,20,000", "Neurologist");
//    System.out.printf("│ %-29s │ %-28s │ %-28s │%n", "Knee Replacement Surgery", "₹4,00,000 - ₹10,00,000", "Orthopedic Surgeon");
//    System.out.printf("│ %-29s │ %-28s │ %-28s │%n", "Child Vaccination", "₹8,000 - ₹24,000", "Pediatrician");
//    System.out.printf("│ %-29s │ %-28s │ %-28s │%n", "Colonoscopy", "₹80,000 - ₹1,60,000", "Gastroenterologist");
//    System.out.printf("│ %-29s │ %-28s │ %-28s │%n", "X-ray Analysis", "₹8,000 - ₹16,000", "Radiologist");
//    System.out.printf("│ %-29s │ %-28s │ %-28s │%n", "Cognitive Behavioral Therapy", "₹12,000 - ₹24,000/session", "Psychiatrist");
//    System.out.printf("│ %-29s │ %-28s │ %-28s │%n", "Prostate Exam", "₹16,000 - ₹40,000", "Urologist");
//    System.out.printf("│ %-29s │ %-28s │ %-28s │%n", "Thyroid Function Test", "₹8,000 - ₹20,000", "Endocrinologist");
//    System.out.println("+" + "—".repeat(98) + "+");
//  }

  // ---------------------- OUTPATIENT RELATED OPTIONS ----------------------
  public void outPatientMenu() {
    System.out.println("+" + "—".repeat(39) + "+");
    System.out.printf("│ %-37S │\n", "Outpatient Services");
    System.out.println("+" + "—".repeat(39) + "+");
    System.out.printf("│ %-37s │\n", "1. Add Patient Records");
    System.out.printf("│ %-37s │\n", "2. Update Patient Contact Details");
    System.out.printf("│ %-37s │\n", "3. Retrieve Patient Details");
    System.out.printf("│ %-37s │\n", "4. Schedule Appointments for Patients");
    System.out.printf("│ %-37s │\n", "5. Retrieve All Patient Records");
    System.out.printf("│ %-37s │\n", "6. Remove Patient Records");
    System.out.printf("│ %-37s │\n", "7. Return to Previous Menu");
    System.out.println("+" + "—".repeat(39) + "+");
  }

//  public void allInpatientDetails() {
//    System.out.println("+" + "—".repeat(197) + "+");
//    System.out.printf("│ %-12s │ %-18s │ %-12s │ %-3s │ %-10s │ %-15s │ %-20s │ %-12s │ %-15s │ %-25s │ %-16s │ %-4s │\n",
//            "PATIENT_ID", "PATIENT_NAME", "PHONE_NUMBER", "AGE", "GENDER", "MEDICAL_HISTORY", "PREFERRED_SPECIALIST", "MEDICINE_FEE", "ADMISSION_FEES", "TREATMENT", "ROOM_TYPE", "FOOD");
//    System.out.println("+" + "—".repeat(197) + "+");
//  }

  public void allOutpatientDetails() {
    System.out.println("+" + "—".repeat(162) + "+");
    System.out.printf("│ %-12s │ %-18s │ %-12s │ %-3s │ %-10s │ %-15s │ %-20s │ %-12s │ %-15s │ %-16s │\n",
            "PATIENT_ID", "PATIENT_NAME", "PHONE_NUMBER", "AGE", "GENDER", "MEDICAL_HISTORY", "PREFERRED_SPECIALIST", "MEDICINE_FEE", "PATIENT_TYPE", "REGISTRATION_FEE");
    System.out.println("+" + "—".repeat(162) + "+");
  }

  public void outpatientInputFormat() {
    System.out.println("+" + "—".repeat(129) + "+");
    System.out.println("│ Format: <patient_name>:<phone_number>:<age>:<gender>:<medical_history>:<preferred_specialist>:<medicine_fee>:<registration_fee> │");
    System.out.println("│ Example: John Doe:9876543210:35:Male:Hypertension:Cardiologist:2000.00:500.00                                                   │");
    System.out.println("+" + "—".repeat(129) + "+");
  }

  // ---------------------- APPOINTMENT RELATED OPTIONS ----------------------
  public void allAppointmentDetails() {
    System.out.println("+" + "—".repeat(160) + "+");
    System.out.printf("│ %-18s │ %-18s │ %-18s │ %-15s │ %-18s │ %-18s │ %-18s │ %-18s │\n",
            "APPOINTMENT_ID", "PATIENT_NAME", "PHONE_NUMBER", "DOCTOR_ID", "SPECIALIZATION", "APPOINTMENT_DATE", "APPOINTMENT_TIME", "MODE_OF_APPOINTMENT");
    System.out.println("+" + "—".repeat(160) + "+");
  }

  public void appointmentMenu() {
    System.out.println("+" + "—".repeat(60) + "+");
    System.out.printf("│ %-58S │\n", "Appointment Services");
    System.out.println("+" + "—".repeat(60) + "+");
    System.out.printf("│ %-58s │\n", "1. Add Appointment Records");
    System.out.printf("│ %-58s │\n", "2. Retrieve Doctor Details by Specialization");
    System.out.printf("│ %-58s │\n", "3. Retrieve Appointment Record");
    System.out.printf("│ %-58s │\n", "4. Update Appointment Date and Time");
    System.out.printf("│ %-58s │\n", "5. Retrieve All Appointment Records");
    System.out.printf("│ %-58s │\n", "6. Cancel Appointment");
    System.out.printf("│ %-58s │\n", "7. Return to Previous Menu");
    System.out.println("+" + "—".repeat(60) + "+");
  }

  public void appointmentInputFormat() {
    System.out.println("+" + "—".repeat(124) + "+");
    System.out.println("│ Format: <patient_name>:<phone_number>:<specialization>:<doctor_id>:<available_date>:<available_time>:<mode_of_appointment> │");
    System.out.println("│ Example: John:9876543210:Cardiologist:APL/DOC/101:dd/MM/yy:hh:mm:offline                                                   │");
    System.out.println("+" + "—".repeat(124) + "+");
  }

  // ---------------------- BED ALLOCATION RELATED OPTIONS ----------------------
  public void allocateBedMenu() {
    System.out.println("+" + "—".repeat(43) + "+");
    System.out.printf("│ %-41S │\n", "Bed Allocation Services");
    System.out.println("+" + "—".repeat(43) + "+");
    System.out.printf("│ %-41s │\n", "1. Add Bed Allocation");
    System.out.printf("│ %-41s │\n", "2. Retrieve Bed Allocation Details");
    System.out.printf("│ %-41s │\n", "3. Retrieve All Bed Allocation Details");
    System.out.printf("│ %-41s │\n", "4. Remove Allocation Details");
    System.out.printf("│ %-41s │\n", "5. Return to Previous Menu");
    System.out.println("+" + "—".repeat(43) + "+");
  }

  public void allocateBedInputFormat() {
    System.out.println("+" + "—".repeat(70) + "+");
    System.out.println("│ Format: <patient_id>:<room_number>:<admission_date>:<discharge_date> │");
    System.out.println("│ Example: APL/INP/100:101:dd/MM/yyyy:dd/MM/yyy                        │");
    System.out.println("+" + "—".repeat(70) + "+");
  }

  public void allocationDetails() {
    System.out.println("+" + "—".repeat(106) + "+");
    System.out.printf("│ %-12s │ %-11s │ %-14s │ %-14s │ %-14s │ %-16s │ %-4s │\n",
                      "ALLOCATION ID", "PATIENT ID", "ADMISSION DATE", "DISCHARGE DATE", "ROOM NO", "ROOM TYPE", "FOOD");
    System.out.println("+" + "—".repeat(106) + "+");
  }

  // ---------------------- PAYMENT RELATED OPTIONS ----------------------
  public void paymentMenu() {
    System.out.println("+" + "—".repeat(48) + "+");
    System.out.printf("│ %-46S │\n", "Payment and Billing Services");
    System.out.println("+" + "—".repeat(48) + "+");
    System.out.printf("│ %-46s │\n", "1. Add Payment Details");
    System.out.printf("│ %-46s │\n", "2. Retrieve Payment Details");
    System.out.printf("│ %-46s │\n", "3. Retrieve All Payment Details");
    System.out.printf("│ %-46s │\n", "4. Return to Previous Menu");
    System.out.println("+" + "—".repeat(48) + "+");
  }

  public void paymentInputFormat() {
    System.out.println("+" + "—".repeat(49) + "+");
    System.out.println("│ Format: <patient_id>:<mode_of_payment>          │");
    System.out.println("│ Example: [APL/INP/100][APL/OUP/100]:Credit Card │");
    System.out.println("+" + "—".repeat(49) + "+");
  }

  public void allPaymentDetails() {
    System.out.println("+" + "—".repeat(125) + "+");
    System.out.printf("│ %-15s │ %-15s │ %-15s │ %-15s │ %-15s │ %-15s │ %-15s │\n",
                      "PAYMENT ID", "INPATIENT ID", "OUTPATIENT ID", "PATIENT NAME", "PAYMENT DATE", "PAYMENT MODE", "TOTAL BILL");
    System.out.println("+" + "—".repeat(125) + "+");
  }

  // ---------------------- DB RELATED OPTIONS ----------------------
  public String dbConErrorMsg() {
    return "Error: Unable to connect to the database. Please check your connection settings and try again.";
  }

  public void idNotFound(String role) {
    System.out.println("Error: The entered " + role + " ID is not found. Please recheck the ID and try again.");
  }

  public void idNotFound(String role, String id) {
    System.out.println("Error: " + role + " with ID " + id + " does not exist.");
  }

  public void appointmentNotFound() {
    System.out.println("Error: ");
  }

  public void missingDataId(int i) {
    System.out.println("Record " + (i + 1) + ": Missing or incorrect data. Please verify and provide the required information.");
  }

  public void checkInputId(int i) {
    System.out.println("Please check the input data for record " + (i + 1));
  }
}

