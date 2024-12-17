package com.client;

import com.exception.*;
import com.model.*;
import com.service.*;
import com.util.ApplicationUtil;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static com.util.ApplicationUtil.validateName;
import static com.util.ApplicationUtil.validatePhoneNum;

public class UserInterface {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    DoctorService doctorService = new DoctorService();
    InpatientService inpatientService = new InpatientService();
    OutpatientService outpatientService = new OutpatientService();
    AppointmentService appointmentService = new AppointmentService();
    AllocationService allocationService = new AllocationService();
    PaymentService paymentService = new PaymentService();

    ApplicationUtil util = new ApplicationUtil();
    Menu menu = new Menu();
    menu.title();

    mainMenu:
    while (true) {
      try {
        menu.mainMenu(); // MAIN MENU
        System.out.print("Select Department -> ");
        int mainChoice = Integer.parseInt(sc.nextLine());

        switch (mainChoice) {
          // ------------------------------- DOCTOR SECTION -------------------------------
          case 1: {
            doctorMenu:
            while (true) {
              try {
                menu.doctorMenu(); // DOCTOR MENU
                System.out.print("Please enter your choice: ");
                int doctorChoice = Integer.parseInt(sc.nextLine());

                switch (doctorChoice) {
                  // ------------------------------- ADD DOCTOR DETAILS -------------------------------
                  case 1: {
                    System.out.print("Please enter the number of doctors to register: ");
                    int noOfDoctors = Integer.parseInt(sc.nextLine());

                    if (noOfDoctors <= 0) {
                      System.out.println("Error: Invalid input. Please enter a valid number of doctors to register.");
                      break;
                    }

                    System.out.println("Please enter the doctor details in the following format:");
                    menu.doctorInputFormat();

                    String[] doctorDetails = new String[noOfDoctors];
                    for (int i = 0; i < noOfDoctors; i++) {
                      doctorDetails[i] = sc.nextLine();
                    }

                    int isAdded = doctorService.add(doctorDetails);

                    if (isAdded > 0)
                      System.out.println(isAdded + " doctor record(s) have been successfully registered.");
                    else if (isAdded == -1)
                      System.out.println("Error: No records to register. Please make sure you entered valid doctor details.");
                    else if (isAdded == -2)
                      System.out.println("Error: Database connection failed. Please try again later.");
                    break;
                  }

                  // ------------------------------- UPDATE DOCTOR FEE -------------------------------
                  case 2: {
                    String doctorId = util.checkDoctorIdIsPresent(sc);

                    if (doctorId.startsWith("APL/DOC/")) {
                      if (doctorService.checkIfDocExistsById(doctorId)) {
                        System.out.print("Enter the new fees to update -> ");
                        double newFees = Double.parseDouble(sc.nextLine());

                        int isUpdated = doctorService.updateDocFeeById(doctorId, newFees);
                        if (isUpdated == 1)
                          System.out.println("Success: Fees have been successfully updated for Doctor with ID: " + doctorId);
                        else if (isUpdated == -1)
                          System.out.println("Error: Unable to update doctor fees. Please try again later.");
                      } else
                        menu.idNotFound("Doctor", doctorId);
                    } else
                      menu.invalidId();

                    break;
                  }

                  // ------------------------------- UPDATE DOCTOR AVAILABLE DATE -------------------------------
                  case 3: {
                    String doctorId = util.checkDoctorIdIsPresent(sc);

                    if (doctorId.startsWith("APL/DOC/")) {
                      if (doctorService.checkIfDocExistsById(doctorId)) {
                        System.out.print("Enter the new date to update (dd/MM/yy) -> ");
                        String newDate = sc.nextLine();

                        try {
                          int isUpdated = doctorService.updateDocAvailableDateById(doctorId, newDate);

                          if (isUpdated > 0)
                            System.out.println("Success: Date has been successfully updated for Doctor with ID: " + doctorId);
                          else if (isUpdated == -1)
                            System.out.println("Error: Database connection failed. Please try again later.");
                          else if (isUpdated == -2)
                            System.out.println("Error: The new date must be later than the previous date. Please enter a date greater than today's date.");
                        } catch (ParseException e) {
                          System.out.println("Error: Invalid date format. Please specify the date in the correct format (dd/MM/yy).");
                        }
                      } else
                        menu.idNotFound("Doctor", doctorId);
                    } else
                      menu.invalidId();

                    break;
                  }

                  // ------------------------------- RETRIEVE DOCTOR DETAILS -------------------------------
                  case 4: {
                    System.out.println("[1] to search by ID │ [2] to search by Name");
                    int option = Integer.parseInt(sc.nextLine());

                    try {
                      if (option == 1) {
                        String doctorId = util.checkDoctorIdIsPresent(sc);
                        if (doctorId.startsWith("APL/DOC/")) {
                          if (doctorService.checkIfDocExistsById(doctorId)) {
                            Doctor doc = doctorService.retrieveDocDetailsById(doctorId);

                            menu.allDoctorDetails();
                            System.out.printf("│ %-10S │ %-18S │ %-10.1f │ %-22S │ %-15s │ %-15s │\n",
                                    doc.getDoctor_id(), doc.getDoctor_name(), doc.getDoctor_fee(),
                                    doc.getSpecialization(), doc.getAvailable_date(),
                                    doc.getAvailable_time());
                            System.out.println("+" + "—".repeat(108) + "+");
                          } else
                            menu.idNotFound("Doctor", doctorId);
                        } else
                          menu.invalidId();
                      } else if (option == 2) {
                        System.out.print("Please enter the Doctor Name: ");
                        String doctorName = sc.nextLine().trim();

                        if (validateName(doctorName)) {
                          List<Doctor> docList = doctorService.retrieveDocDetailsByName(doctorName);

                          if (docList == null)
                            System.out.println("Error: Unable to retrieve doctor details. Please check your database connection and try again.");
                          else {
                            if (!docList.isEmpty()) {
                              menu.allDoctorDetails();
                              for (Doctor doc : docList) {
                                System.out.printf("│ %-10S │ %-18S │ %-10.1f │ %-22S │ %-15s │ %-15s │\n",
                                        doc.getDoctor_id(), doc.getDoctor_name(),
                                        doc.getDoctor_fee(), doc.getSpecialization(),
                                        doc.getAvailable_date(), doc.getAvailable_time());
                              }
                              System.out.println("+" + "—".repeat(108) + "+");
                            } else
                              System.out.println("Error: No records found for the specified doctor name.");
                          }
                        }
                      } else {
                        System.out.println("Error: Invalid option selected. Please choose a valid option.");
                      }
                    } catch (DBConnectionFailedException | InvalidNameException e) {
                      System.out.println("Error: " + e.getMessage());
                    }

                    break;
                  }

                  // ------------------------------- RETRIEVE ALL DOCTOR DETAILS ------------------------------
                  case 5: {
                    List<Doctor> docList = doctorService.retrieveAllDocDetails();

                    if (docList == null) {
                      System.out.println("Error: Unable to fetch doctor records. Please check your database connection.");
                    } else {
                      if (!docList.isEmpty()) {
                        menu.allDoctorDetails();
                        for (Doctor doc : docList) {
                          System.out.printf("│ %-10S │ %-18S │ %-10.1f │ %-22S │ %-15s │ %-15s │\n",
                                  doc.getDoctor_id(), doc.getDoctor_name(), doc.getDoctor_fee(),
                                  doc.getSpecialization(), doc.getAvailable_date(),
                                  doc.getAvailable_time());
                        }
                        System.out.println("+" + "—".repeat(108) + "+");
                      } else {
                        System.out.println("Error: No records found. Please register doctor details to proceed.");
                      }
                    }

                    break;
                  }

                  // ------------------------------- Go Back to Previous Menu -------------------------------
                  case 6: {
                    break doctorMenu;
                  }

                  default: {
                    System.out.println("Error: Invalid option selected. Please enter a valid option from the menu.");
                  }
                }
              } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please provide a valid numeric option.");
              }
            }
            break;
          }

          // ------------------------------- PATIENT SECTION -------------------------------
          case 2: {
            patientMenu:
            while (true) {
              menu.patientMenu(); // PATIENT MENU
              System.out.print("Please enter your choice: ");
              int patientChoice = Integer.parseInt(sc.nextLine());

              switch (patientChoice) {
                // ------------------------------- Inpatient Services -------------------------------
                case 1: {
                  inpatientMenu:
                  while (true) {
                    try {
                      menu.inPatientMenu(); // INPATIENT MENU
                      System.out.print("Please enter your choice: ");
                      int inpatientChoice = Integer.parseInt(sc.nextLine());

                      switch (inpatientChoice) {
                        // ------------------------------- Add Patient Records -------------------------------
                        case 1: {
                          System.out.print("Please enter the number of Patients to register: ");
                          int noOfInpatient = Integer.parseInt(sc.nextLine());

                          if (noOfInpatient <= 0) {
                            System.out.println("Error: Invalid input!");
                            break;
                          }

                          System.out.println("Please enter the patient details in the following format:");
                          menu.inpatientInputFormat();

                          String[] inpatientDetails = new String[noOfInpatient];
                          for (int i = 0; i < noOfInpatient; i++) {
                            inpatientDetails[i] = sc.nextLine();
                          }

                          int isAdded = inpatientService.add(inpatientDetails);
                          if (isAdded > 0)
                            System.out.println(isAdded + " Patient record(s) have been successfully registered.");

                          break;
                        }

                        // ------------------------------- Update Patient Contact Details By Name and Phone Number -------------------------------
                        case 2: {
                          String patientNameAndPhone = util.checkInpatientIdIsPresent(sc, false);

                          try {
                            if (!patientNameAndPhone.isEmpty()) {
                              String name = patientNameAndPhone.split(",")[0];
                              String phone = patientNameAndPhone.split(",")[1];
                              if (inpatientService.isUserExists(name, phone)) {
                                System.out.print("Please enter new contact number: ");
                                String newPhone = sc.nextLine().trim();

                                validatePhoneNum(newPhone);
                                int isUpdated = inpatientService.updateInpatientPhoneNumByNameAndPhone(name, phone, newPhone);

                                if (isUpdated > 0)
                                  System.out.println("Contact Detail updated successfully for Patient " + util.capitalize(name));
                                else if (isUpdated == 0) System.out.println("Error: Updation unsuccessful");
                                else if (isUpdated == -1)
                                  System.out.println(menu.dbConErrorMsg());
                              } else
                                System.out.println("Error: No such user exists");
                            }
                          } catch (InvalidPhoneNumberException | DBConnectionFailedException e) {
                            System.out.println(e.getMessage());
                          }

                          break;
                        }

                        // ------------------------------- Update Room Type for Patients -------------------------------
                        case 3: {
                          String patientId = util.checkInpatientIdIsPresent(sc, true);

                          try {
                            if (patientId.startsWith("APL/INP/")) {
                              if (inpatientService.isUserExists(patientId)) {
                                System.out.print("Enter New Room Type to Update -> ");
                                String room = sc.nextLine();

                                util.validRoomType(room);
                                int isUpdated = inpatientService.updateInpatientRoomType(patientId, room);

                                if (isUpdated > 0)
                                  System.out.println("Room Type successfully updated for Patient with ID: " + patientId);
                                else if (isUpdated == -1)
                                  System.out.println(menu.dbConErrorMsg());
                              } else menu.idNotFound("Patient", patientId);
                            } else menu.invalidId();
                          } catch (InvalidRoomTypeException e) {
                            System.out.println(e.getMessage());
                          }

                          break;
                        }

                        // ------------------------------- Display Available Room Types -------------------------------
                        case 4: {
                          menu.roomsList();
                          break;
                        }

                        // ------------------------------- Update Meal Preferences for Patients -------------------------------
                        case 5: {
                          String patientId = util.checkInpatientIdIsPresent(sc, true);

                          if (patientId.startsWith("APL/INP/")) {
                            if (inpatientService.isUserExists(patientId)) {
                              System.out.print("Enter Food Preference to Update (Yes/No) -> ");
                              String pref = sc.nextLine();

                              if (pref.equalsIgnoreCase("yes") || pref.equalsIgnoreCase("no")) {
                                int isUpdated = inpatientService.updateFoodPreference(patientId, pref);

                                if (isUpdated > 0)
                                  System.out.println("Food Preference have been successfully updated for Patient with ID: " + patientId);
                                else if (isUpdated == -1)
                                  System.out.println(menu.dbConErrorMsg());
                              } else System.out.println("Error: Please enter valid option");
                            } else menu.idNotFound("Patient", patientId);
                          } else menu.invalidId();

                          break;
                        }

                        // ------------------------------- Retrieve Patient Details -------------------------------
                        case 6: {
                          System.out.println("[1] to search by ID │ [2] to search by Name and Phone");
                          int option = Integer.parseInt(sc.nextLine());

                          try {
                            if (option == 1) {
                              String patientId = util.checkInpatientIdIsPresent(sc, true);
                              if (patientId.startsWith("APL/INP/")) {
                                if (inpatientService.isUserExists(patientId)) {
                                  Inpatient inpatient = inpatientService.retrieveInpatientDetailByPatientId(patientId);
                                  if (inpatient == null) System.out.println("Error: Record not found");
                                  else {
                                    menu.allInpatientDetails();
                                    System.out.printf(
                                            "│ %-12s │ %-18s │ %-12s │ %-3s │ %-10s │ %-15s │ %-20s │ %-12s │ %-15s │ %-25s │ %-16s │ %-4s │ \n",
                                            inpatient.getPatientId(), inpatient.getPatientName(),
                                            inpatient.getPhoneNumber(), inpatient.getAge(), inpatient.getGender(),
                                            inpatient.getMedicalHistory(), inpatient.getPreferredSpecialist(),
                                            inpatient.getMedicineFee(),
                                            inpatient.getAdmissionFees(), inpatient.getTreatment(),
                                            inpatient.getRoomType(), inpatient.getWantFood());
                                    System.out.println("+" + "—".repeat(197) + "+");
                                  }
                                } else menu.idNotFound("Patient", patientId);
                              } else menu.invalidId();
                            } else if (option == 2) {
                              String patientNameAndPhone = util.checkInpatientIdIsPresent(sc, false);
                              if (!patientNameAndPhone.isEmpty()) {
                                String name = patientNameAndPhone.split(",")[0];
                                String phone = patientNameAndPhone.split(",")[1];
                                Inpatient inpatient = inpatientService.retrieveInpatientDetailByNameAndPhone(name, phone);
                                if (inpatient == null)
                                  System.out.println("Error: Patient record with name " + util.capitalize(name) + " does not exist");
                                else {
                                  menu.allInpatientDetails();
                                  System.out.printf(
                                          "│ %-12s │ %-18s │ %-12s │ %-3s │ %-10s │ %-15s │ %-20s │ %-12s │ %-15s │ %-25s │ %-16s │ %-4s │ \n",
                                          inpatient.getPatientId(), inpatient.getPatientName(),
                                          inpatient.getPhoneNumber(), inpatient.getAge(), inpatient.getGender(),
                                          inpatient.getMedicalHistory(), inpatient.getPreferredSpecialist(),
                                          inpatient.getMedicineFee(),
                                          inpatient.getAdmissionFees(), inpatient.getTreatment(),
                                          inpatient.getRoomType(), inpatient.getWantFood());
                                  System.out.println("+" + "—".repeat(197) + "+");
                                }
                              }
                            }
                          } catch (DBConnectionFailedException e) {
                            System.out.println(e.getMessage());
                          }

                          break;
                        }

                        // ------------------------------- Bed Allocation for Patients -------------------------------
                        case 7: {
                          allocateBedMenu:
                          while (true) {
                            try {
                              menu.allocateBedMenu(); // ALLOCATE BED MENU
                              System.out.print("Enter your choice -> ");
                              int allocateBedChoice = Integer.parseInt(sc.nextLine());

                              switch (allocateBedChoice) {
                                // ------------------------------- Add Bed Allocation Records -------------------------------
                                case 1: {
                                  System.out.println("Please enter the Bed Allocation details in the following format:");
                                  menu.allocateBedInputFormat();

                                  String allocationDetails = sc.nextLine();

                                  int isAdded = allocationService.add(allocationDetails);
                                  if (isAdded > 0) System.out.println("Record successfully inserted");

                                  break;
                                }

                                // ------------------------------- Retrieve Bed Allocation Details -------------------------------
                                case 2: {
                                  System.out.println("[1] to search by ID | [2] to search by Name and Phone");
                                  int option = Integer.parseInt(sc.nextLine());

                                  try {
                                    if (option == 1) {
                                      String patientId = util.checkInpatientIdIsPresent(sc, true);
                                      if (patientId.startsWith("APL/INP/")) {
                                        if (inpatientService.isUserExists(patientId)) {
                                          Allocation all = allocationService.retrieveAllocationDetailsByPatientId(patientId);
                                          if (all != null) {
                                            menu.allocationDetails();
                                            System.out.printf("│ %-13s │ %-11s │ %-14s │ %-14s │ %-14s │ %-16s │ %-4s │\n",
                                                    all.getAllocationId(), all.getPatientId(), all.getAdmissionDate(),
                                                    all.getDischargeDate(), all.getRoomNumber(), all.getRoomType(),
                                                    all.getWantFood());
                                            System.out.println("+" + "—".repeat(106) + "+");
                                          } else System.out.println("Error: Patient has not allocated any bed");
                                        } else System.out.println("Error: Please register as Inpatient");
                                      } else menu.invalidId();
                                    } else if (option == 2) {
                                      String patientNameAndPhone = util.checkInpatientIdIsPresent(sc, false);
                                      if (!patientNameAndPhone.isEmpty()) {
                                        String name = patientNameAndPhone.split(",")[0];
                                        String phone = patientNameAndPhone.split(",")[1];
                                        Inpatient inpatient = inpatientService.retrieveInpatientDetailByNameAndPhone(name, phone);
                                        if (inpatient != null) {
                                          Allocation all = allocationService.retrieveAllocationDetailsByPatientId(inpatient.getPatientId());
                                          if (all != null) {
                                            menu.allocationDetails();
                                            System.out.printf("│ %-13s │ %-11s │ %-14s │ %-14s │ %-14s │ %-16s │ %-4s │\n",
                                                    all.getAllocationId(), all.getPatientId(), all.getAdmissionDate(),
                                                    all.getDischargeDate(), all.getRoomNumber(), all.getRoomType(),
                                                    all.getWantFood());
                                            System.out.println("+" + "—".repeat(106) + "+");
                                          } else System.out.println("Error: Patient has not allocated any bed");
                                        } else
                                          System.out.println("Error: Please register as Inpatient");
                                      }
                                    } else {
                                      System.out.println("Error: Invalid option selected. Please choose a valid option.");
                                    }
                                  } catch (DBConnectionFailedException e) {
                                    System.out.println(e.getMessage());
                                  }

                                  break;
                                }

                                // ------------------------------- Retrieve All Bed Allocation Details -------------------------------
                                case 3: {
                                  List<Allocation> allocationDetails = allocationService.retrieveAllAllocationDetails();

                                  if (allocationDetails == null)
                                    System.out.println(menu.dbConErrorMsg());
                                  else {
                                    if (allocationDetails.isEmpty()) {
                                      System.out.println("Error: No records found");
                                    } else {
                                      menu.allocationDetails();
                                      for (Allocation all : allocationDetails) {
                                        System.out.printf("│ %-13s │ %-11s │ %-14s │ %-14s │ %-14s │ %-16s │ %-4s │\n",
                                                all.getAllocationId(), all.getPatientId(), all.getAdmissionDate(),
                                                all.getDischargeDate(), all.getRoomNumber(), all.getRoomType(),
                                                all.getWantFood());
                                      }
                                      System.out.println("+" + "—".repeat(106) + "+");
                                    }
                                  }

                                  break;
                                }

                                // ------------------------------- Remove Allocation Details -------------------------------
                                case 4: {
                                  System.out.println("[1] to remove by ID | [2] to remove by Name and Phone");
                                  int option = Integer.parseInt(sc.nextLine());

                                  try {
                                    if (option == 1) {
                                      String patientId = util.checkInpatientIdIsPresent(sc, true);
                                      if (patientId.startsWith("APL/INP/")) {
                                        if (inpatientService.isUserExists(patientId)) {
                                          int isRemoved = allocationService.deleteAllocationDetails(patientId);

                                          if (isRemoved > 0)
                                            System.out.println("Success: Allocation cancelled");
                                          else if (isRemoved == 0)
                                            System.out.println("Error: Deletion unsuccessful");
                                          else if (isRemoved == -1)
                                            System.out.println(menu.dbConErrorMsg());
                                        } else menu.idNotFound("Patient", patientId);
                                      } else menu.invalidId();
                                    } else if (option == 2) {
                                      String patientNameAndPhone = util.checkInpatientIdIsPresent(sc, false);
                                      if (!patientNameAndPhone.isEmpty()) {
                                        String name = patientNameAndPhone.split(",")[0];
                                        String phone = patientNameAndPhone.split(",")[1];
                                        Inpatient inpatient = inpatientService.retrieveInpatientDetailByNameAndPhone(name, phone);

                                        if (inpatient != null) {
                                          int isRemoved = allocationService.deleteAllocationDetails(inpatient.getPatientId());

                                          if (isRemoved > 0)
                                            System.out.println("Success: Appointment cancelled");
                                          else if (isRemoved == 0)
                                            System.out.println("Error: Deletion unsuccessful");
                                        } else System.out.println("Error: Please register as Inpatient");
                                      }
                                    } else System.out.println("Error: Please register as Inpatient");
                                  } catch (DBConnectionFailedException e) {
                                    System.out.println(e.getMessage());
                                  }

                                  break;
                                }

                                // ------------------------------- Return to Previous Menu -------------------------------
                                case 5: {
                                  break allocateBedMenu;
                                }
                              }
                            } catch (NumberFormatException e) {
                              System.out.println("Error: Please provide valid option.");
                            }
                          }
                          break;
                        }


                        // ------------------------------- Retrieve All Patient Records -------------------------------
                        case 8: {
                          List<Inpatient> inpatients = inpatientService.retrieveAllInpatientDetails();

                          if (inpatients == null)
                            System.out.println(menu.dbConErrorMsg());
                          else {
                            if (inpatients.isEmpty()) {
                              System.out.println(
                                      "Error: No records found. Please register your records to proceed.");
                            } else {
                              menu.allInpatientDetails();
                              for (Inpatient inp : inpatients) {
                                System.out.printf(
                                        "│ %-12s │ %-18s │ %-12s │ %-3s │ %-10s │ %-15s │ %-20s │ %-12s │ %-15s │ %-25s │ %-16s │ %-4s │ \n",
                                        inp.getPatientId(), inp.getPatientName(),
                                        inp.getPhoneNumber(), inp.getAge(), inp.getGender(),
                                        inp.getMedicalHistory(), inp.getPreferredSpecialist(),
                                        inp.getMedicineFee(),
                                        inp.getAdmissionFees(), inp.getTreatment(),
                                        inp.getRoomType(), inp.getWantFood());
                              }
                              System.out.println("+" + "—".repeat(197) + "+");
                            }
                          }

                          break;
                        }

                        // ------------------------------- Remove Patient Records -------------------------------
                        case 9: {
                          String patientId = util.checkInpatientIdIsPresent(sc, true);

                          if (patientId.startsWith("APL/INP/")) {
                            if (inpatientService.isUserExists(patientId)) {
                              int isRemoved = inpatientService.deleteInpatientDetails(patientId);

                              if (isRemoved > 0)
                                System.out.println("The Patient record with ID " + patientId + " has been successfully removed.");
                              else if (isRemoved == -1)
                                System.out.println(menu.dbConErrorMsg());
                            } else menu.idNotFound("Patient", patientId);
                          } else menu.invalidId();

                          break;
                        }

                        // ------------------------------- Return to Previous Menu -------------------------------
                        case 10: {
                          break inpatientMenu;
                        }

                        default: {
                          System.out.println("Error: Invalid Option");
                        }
                      }
                    } catch (NumberFormatException e) {
                      System.out.println("Error: Please provide valid option.");
                    }
                  }

                  break;
                }

                // ------------------------------- Outpatient Services -------------------------------
                case 2: {
                  outpatientMenu:
                  while (true) {
                    try {
                      menu.outPatientMenu(); // OUTPATIENT MENU
                      System.out.print("Enter your choice -> ");
                      int outpatientChoice = Integer.parseInt(sc.nextLine());

                      switch (outpatientChoice) {
                        // ------------------------------- Add Outpatient Records -------------------------------
                        case 1: {
                          System.out.print("Enter the number of Outpatients to register -> ");
                          int noOfOutpatient = Integer.parseInt(sc.nextLine());

                          if (noOfOutpatient <= 0) {
                            System.out.println("Error: Invalid input!");
                            break;
                          }

                          System.out.println("Please enter the patient details in the following format:");
                          menu.outpatientInputFormat();

                          String[] outpatientDetails = new String[noOfOutpatient];
                          for (int i = 0; i < noOfOutpatient; i++) {
                            outpatientDetails[i] = sc.nextLine();
                          }

                          int isAdded = outpatientService.add(outpatientDetails);

                          if (isAdded > 0)
                            System.out.println(isAdded + " Outpatient record(s) have been successfully registered.");
                          else if (isAdded == -2)
                            System.out.println("Error: No records to register");
                          break;
                        }

                        // ------------------------------- Update Outpatient Phone Number -------------------------------
                        case 2: {
                          String patientNameAndPhone = util.checkOutpatientIdIsPresent(sc, false);

                          try {
                            if (!patientNameAndPhone.isEmpty()) {
                              String name = patientNameAndPhone.split(",")[0];
                              String phone = patientNameAndPhone.split(",")[1];
                              if (outpatientService.isUserExists(name, phone)) {
                                System.out.print("Please enter new contact number: ");
                                String newPhone = sc.nextLine().trim();

                                validatePhoneNum(newPhone);
                                int isUpdated = outpatientService.updateOutpatientPhoneNumByNameAndPhone(name, phone, newPhone);

                                if (isUpdated > 0)
                                  System.out.println("Contact Detail updated successfully for Patient " + name.toUpperCase());
                                else if (isUpdated == -1)
                                  System.out.println(menu.dbConErrorMsg());
                              } else System.out.println("Error: No such user exists");
                            }
                          } catch (InvalidPhoneNumberException | DBConnectionFailedException e) {
                            System.out.println(e.getMessage());
                          }

                          break;
                        }

                        // ------------------------------- Retrieve Outpatient Details -------------------------------
                        case 3: {
                          System.out.println("[1] to search by ID | [2] to search by Name and Phone");
                          int option = Integer.parseInt(sc.nextLine());

                          try {
                            if (option == 1) {
                              String patientId = util.checkOutpatientIdIsPresent(sc, true);
                              if (patientId.startsWith("APL/OUP/")) {
                                if (outpatientService.isUserExists(patientId)) {
                                  Outpatient out = outpatientService.retrieveOutpatientDetailsById(patientId);
                                  if (out == null) System.out.println("Error: Record not found");
                                  else {
                                    menu.allOutpatientDetails();
                                    System.out.printf(
                                            "│ %-12S │ %-18S │ %-12s │ %-3s │ %-10s │ %-15s │ %-20s │ %-12.2f │ %-15s │ %-16.2f │\n",
                                            out.getPatientId(), out.getPatientName(),
                                            out.getPhoneNumber(), out.getAge(),
                                            out.getGender(), out.getMedicalHistory(),
                                            out.getPreferredSpecialist(),
                                            out.getMedicineFee(), out.getPatientType(),
                                            out.getRegistration_fees());
                                    System.out.println("+" + "—".repeat(162) + "+");
                                  }
                                } else menu.idNotFound("Patient", patientId);
                              } else menu.invalidId();
                            } else if (option == 2) {
                              String patientNameAndPhone = util.checkOutpatientIdIsPresent(sc, false);
                              if (!patientNameAndPhone.isEmpty()) {
                                String name = patientNameAndPhone.split(",")[0];
                                String phone = patientNameAndPhone.split(",")[1];
                                Outpatient out = outpatientService.retrieveOutpatientDetailByNameAndPhone(name, phone);

                                if (out == null)
                                  System.out.println("Error: Patient record with name " + name + " does not exist");
                                else {
                                  menu.allOutpatientDetails();
                                  System.out.printf(
                                          "│ %-12S │ %-18S │ %-12s │ %-3s │ %-10s │ %-15s │ %-20s │ %-12.2f │ %-15s │ %-16.2f │\n",
                                          out.getPatientId(), out.getPatientName(),
                                          out.getPhoneNumber(), out.getAge(),
                                          out.getGender(), out.getMedicalHistory(),
                                          out.getPreferredSpecialist(),
                                          out.getMedicineFee(), out.getPatientType(),
                                          out.getRegistration_fees());
                                  System.out.println("+" + "—".repeat(162) + "+");
                                }
                              }
                            }
                          } catch (DBConnectionFailedException e) {
                            System.out.println(e.getMessage());
                          }

                          break;
                        }

                        // ------------------------------- Schedule Appointment -------------------------------
                        case 4: {
                          appointmentMenu:
                          while (true) {
                            try {
                              menu.appointmentMenu(); // APPOINTMENT MENU
                              System.out.print("Enter your choice -> ");
                              int appointmentChoice = Integer.parseInt(sc.nextLine());

                              switch (appointmentChoice) {
                                // ------------------------------- Add Appointment Records -------------------------------
                                case 1: {
                                  System.out.print("Enter the number of Appointments to register -> ");
                                  int noOfAppointments = Integer.parseInt(sc.nextLine());

                                  if (noOfAppointments <= 0) {
                                    System.out.println("Error: Invalid input!");
                                    break;
                                  }

                                  System.out.println("Please enter the Appointment details in the following format:");
                                  menu.appointmentInputFormat();

                                  String[] appointmentDetails = new String[noOfAppointments];
                                  for (int i = 0; i < noOfAppointments; i++) {
                                    appointmentDetails[i] = sc.nextLine();
                                  }

                                  int isAdded = appointmentService.add(appointmentDetails);

                                  if (isAdded > 0)
                                    System.out.println(isAdded + " Appointment record(s) have been successfully registered.");

                                  break;
                                }

                                // ------------------------------- Retrieve Doctor Details by Specialization -------------------------------
                                case 2: {
                                  System.out.print("Please enter the Specialization: ");
                                  String specialization = sc.nextLine();

                                  List<Doctor> docList = appointmentService.retrieveDoctorDetailsBasedOnSpecialization(specialization);

                                  if (docList == null)
                                    System.out.println(menu.dbConErrorMsg());
                                  else {
                                    if (!docList.isEmpty()) {
                                      menu.allDoctorDetails();
                                      for (Doctor doc : docList) {
                                        System.out.printf("│ %-10S │ %-18S │ %-10.1f │ %-22S │ %-15s │ %-15s │\n",
                                                doc.getDoctor_id(), doc.getDoctor_name(), doc.getDoctor_fee(),
                                                doc.getSpecialization(), doc.getAvailable_date(),
                                                doc.getAvailable_time());
                                      }
                                      System.out.println("+" + "—".repeat(108) + "+");
                                    } else
                                      System.out.println("Error: No doctors found based on provided specialization");
                                  }

                                  break;
                                }

                                // ------------------------------- Retrieve Appointment Record -------------------------------
                                case 3: {
                                  System.out.println("[1] to search by ID | [2] to search by Name and Phone");
                                  int option = Integer.parseInt(sc.nextLine());

                                  try {
                                    if (option == 1) {
                                      String patientId = util.checkOutpatientIdIsPresent(sc, true);
                                      if (patientId.startsWith("APL/OUP/")) {
                                        if (outpatientService.isUserExists(patientId)) {
                                          Outpatient outpatient = outpatientService.retrieveOutpatientDetailsById(patientId);
                                          if (outpatient == null) {
                                            System.out.println("Error: No record found");
                                            break;
                                          }

                                          List<String> appointmentDetails = appointmentService.retrieveAppointmentDetailsByPatientId(patientId);
                                          if (appointmentDetails == null)
                                            System.out.println("Error: " + outpatient.getPatientName().toUpperCase() + " has not booked any appointment");
                                          else {
                                            System.out.println(appointmentDetails);
                                          }
                                        } else menu.idNotFound("Patient", patientId);
                                      } else menu.invalidId();
                                    } else if (option == 2) {
                                      String patientNameAndPhone = util.checkOutpatientIdIsPresent(sc, false);
                                      if (!patientNameAndPhone.isEmpty()) {
                                        String name = patientNameAndPhone.split(",")[0];
                                        String phone = patientNameAndPhone.split(",")[1];
                                        Outpatient outpatient = outpatientService.retrieveOutpatientDetailByNameAndPhone(name, phone);

                                        if (outpatient == null)
                                          System.out.println("Error: Patient record with name " + name + " does not exist");
                                        else {
                                          List<String> list = appointmentService.retrieveAppointmentDetailsByPatientId(outpatient.getPatientId());
                                          System.out.println(list);
                                        }
                                      }
                                    }
                                  } catch (DBConnectionFailedException e) {
                                    System.out.println(e.getMessage());
                                  }

                                  break;
                                }

                                // ------------------------------- Update Appointment Date and Time -------------------------------
                                case 4: {
                                  System.out.println("[1] to search by ID | [2] to search by Name and Phone");
                                  int option = Integer.parseInt(sc.nextLine());

                                  try {
                                    if (option == 1) {
                                      String patientId = util.checkOutpatientIdIsPresent(sc, true);
                                      if (patientId.startsWith("APL/OUP/")) {
                                        if (outpatientService.isUserExists(patientId)) {
                                          System.out.print("Please enter new date (dd/MM/yyyy): ");
                                          String newDate = sc.nextLine();
                                          Date date = util.strToDateConversion(newDate);

                                          System.out.print("Please enter new time (hh:mm): ");
                                          String newTime = sc.nextLine();
                                          LocalTime time = util.checkAppointmentTimeLimit(newTime);

                                          int isUpdated = appointmentService.updateAppointmentDateAndTimeByPatientId(patientId, date, time);
                                          Outpatient outpatient = outpatientService.retrieveOutpatientDetailsById(patientId);

                                          if (outpatient == null) {
                                            System.out.println("Error: No record found");
                                            break;
                                          }

                                          if (isUpdated > 0)
                                            System.out.println("Success: Appointment Date and Time successfully updated for " + outpatient.getPatientName().toUpperCase());
                                          else if (isUpdated == 0)
                                            System.out.println("Error: No appointment found for patient " + outpatient.getPatientName().toUpperCase());
                                          else if (isUpdated == -1)
                                            System.out.println("Error: The new date must be later than the previous date. Please enter a date greater than today's date");
                                        } else
                                          System.out.println("Error: No record found. Please register as Outpatient");
                                      } else menu.invalidId();
                                    } else if (option == 2) {
                                      String patientNameAndPhone = util.checkOutpatientIdIsPresent(sc, false);
                                      if (!patientNameAndPhone.isEmpty()) {
                                        String name = patientNameAndPhone.split(",")[0];
                                        String phone = patientNameAndPhone.split(",")[1];
                                        if (outpatientService.isUserExists(name, phone)) {
                                          System.out.print("Please enter new date (dd/MM/yyyy): ");
                                          String newDate = sc.nextLine();
                                          Date date = util.strToDateConversion(newDate);

                                          System.out.print("Please enter new time (hh:mm): ");
                                          String newTime = sc.nextLine();
                                          LocalTime time = util.checkAppointmentTimeLimit(newTime);

                                          int isUpdated = appointmentService.updateAppointmentDateAndTimeByPatientNameAndPhoneNo(name, phone, date, time);
                                          Outpatient outpatient = outpatientService.retrieveOutpatientDetailByNameAndPhone(name, phone);

                                          if (isUpdated > 0)
                                            System.out.println("Success: Appointment Date and Time successfully updated for " + outpatient.getPatientName().toUpperCase());
                                          else if (isUpdated == 0)
                                            System.out.println("Error: No appointment found for patient " + outpatient.getPatientName().toUpperCase());
                                          else if (isUpdated == -1)
                                            System.out.println("Error: The new date must be later than the previous date. Please enter a date greater than today's date");
                                        } else
                                          System.out.println("Error: Please register as Outpatient");
                                      }
                                    }
                                  } catch (DBConnectionFailedException | ParseException |
                                           InvalidAppointmentTimeException e) {
                                    System.out.println(e.getMessage());
                                  }

                                  break;
                                }

                                // ------------------------------- Retrieve All Appointment Records -------------------------------
                                case 5: {
                                  List<Appointment> appointments = appointmentService.retrieveAllAppointmentDetails();

                                  if (appointments == null) System.out.println(menu.dbConErrorMsg());
                                  else {
                                    if (appointments.isEmpty())
                                      System.out.println("Error: No records found. Please register your records to proceed.");
                                    else {
                                      menu.allAppointmentDetails();
                                      for (Appointment app : appointments) {
                                        Outpatient out = outpatientService.retrieveOutpatientDetailsById(app.getPatientId());
                                        if (out == null) {
                                          System.out.println("Error: No record found");
                                          break;
                                        }
                                        System.out.printf(
                                                "│ %-18S │ %-18S │ %-18S │ %-15s │ %-18s │ %-18s │ %-18s │ %-18s │\n",
                                                app.getAppointmentId(), out.getPatientName(),
                                                out.getPhoneNumber(), app.getDoctorId(), app.getSpecialization(),
                                                app.getAppointmentDate(), app.getAppointmentTime(),
                                                app.getAppointmentMode());
                                      }
                                      System.out.println("+" + "—".repeat(160) + "+");
                                    }
                                  }

                                  break;
                                }

                                // ------------------------------- Cancel Appointment -------------------------------
                                case 6: {
                                  System.out.println("[1] to cancel by ID | [2] to cancel by Name and Phone");
                                  int option = Integer.parseInt(sc.nextLine());

                                  try {
                                    if (option == 1) {
                                      String patientId = util.checkOutpatientIdIsPresent(sc, true);
                                      if (patientId.startsWith("APL/OUP/")) {
                                        if (outpatientService.isUserExists(patientId)) {
                                          int isAppointmentCanceled = appointmentService.cancelAppointmentByPatientId(patientId);

                                          if (isAppointmentCanceled > 0)
                                            System.out.println("Success: Appointment cancelled");
                                          else if (isAppointmentCanceled == 0)
                                            System.out.println("Error: " + patientId + " has not booked any appointment");
                                        } else menu.idNotFound("Patient", patientId);
                                      } else menu.invalidId();
                                    } else if (option == 2) {
                                      String patientNameAndPhone = util.checkOutpatientIdIsPresent(sc, false);
                                      if (!patientNameAndPhone.isEmpty()) {
                                        String name = patientNameAndPhone.split(",")[0];
                                        String phone = patientNameAndPhone.split(",")[1];
                                        if (outpatientService.isUserExists(name, phone)) {
                                          int isAppointmentCanceled = appointmentService.cancelAppointmentByPatientNameAndPhoneNo(name, phone);

                                          if (isAppointmentCanceled > 0)
                                            System.out.println("Success: Appointment cancelled");
                                          else if (isAppointmentCanceled == 0)
                                            System.out.println("Error: Deletion unsuccessful");
                                        } else System.out.println("Error: Please register as Outpatient");
                                      }
                                    }
                                  } catch (DBConnectionFailedException e) {
                                    System.out.println(e.getMessage());
                                  }

                                  break;
                                }

                                // ------------------------------- Switch to Previous Menu -------------------------------
                                case 7: {
                                  break appointmentMenu;
                                }

                                default: {
                                  System.out.println("Error: Invalid Option");
                                  break;
                                }
                              }
                            } catch (NumberFormatException e) {
                              System.out.println("Error: Please provide valid option.");
                            }
                          }

                          break;
                        }

                        // ------------------------------- Retrieve All Outpatient Details -------------------------------
                        case 5: {
                          List<Outpatient> outpatients = outpatientService.retrieveAllOutpatientDetails();

                          if (outpatients == null) {
                            System.out.println(menu.dbConErrorMsg());
                          } else {
                            if (outpatients.isEmpty())
                              System.out.println(
                                      "Error: No records found. Please register your records to proceed.");
                            else {
                              menu.allOutpatientDetails();
                              for (Outpatient out : outpatients) {
                                System.out.printf(
                                        "│ %-12S │ %-18S │ %-12s │ %-3s │ %-10s │ %-15s │ %-20s │ %-12.2f │ %-15s │ %-16.2f │\n",
                                        out.getPatientId(), out.getPatientName(),
                                        out.getPhoneNumber(), out.getAge(),
                                        out.getGender(), out.getMedicalHistory(),
                                        out.getPreferredSpecialist(),
                                        out.getMedicineFee(), out.getPatientType(),
                                        out.getRegistration_fees());
                              }
                              System.out.println("+" + "—".repeat(162) + "+");
                            }
                          }

                          break;
                        }

                        // ------------------------------- Delete Outpatient Details -------------------------------
                        case 6: {
                          String patientId = util.checkOutpatientIdIsPresent(sc, true);

                          if (patientId.startsWith("APL/OUP/")) {
                            if (outpatientService.isUserExists(patientId)) {
                              int isRemoved = outpatientService.deleteOutpatientDetailsById(patientId);

                              if (isRemoved > 0)
                                System.out.println("The Patient record with ID " + patientId + " has been successfully removed.");
                              else if (isRemoved == -1)
                                System.out.println(menu.dbConErrorMsg());
                            } else menu.idNotFound("Patient", patientId);
                          } else menu.invalidId();

                          break;
                        }

                        // ------------------------------- Switch to Previous Menu -------------------------------
                        case 7: {
                          break outpatientMenu;
                        }

                        default: {
                          System.out.println("Error: Invalid Option");
                        }
                      }
                    } catch (NumberFormatException | DBConnectionFailedException e) {
                      System.out.println("Error: Please provide valid option.");
                    }
                  }
                  break;
                }

                // ------------------------------- Billing and Payments -------------------------------
                case 3: {
                  paymentMenu:
                  while (true) {
                    try {
                      menu.paymentMenu(); // PAYMENT MENU
                      System.out.print("Enter your choice -> ");
                      int paymentChoice = Integer.parseInt(sc.nextLine());

                      switch (paymentChoice) {
                        // ------------------------------- Add Payment Details -------------------------------
                        case 1: {
                          System.out.println("Please enter the Payment details in the following format:");
                          menu.paymentInputFormat();

                          String paymentDetails = sc.nextLine().trim();

                          int isAdded = paymentService.add(paymentDetails);

                          if (isAdded > 0)
                            System.out.println(isAdded + " Payment record(s) have been successfully registered.");

                          break;
                        }

                        // ------------------------------- Retrieve Payment Details -------------------------------
                        case 2: {
                          try {
                            System.out.println("Please enter Patient's Id [APL/INP/100] | [APL/OUP/100]: ");
                            String patientId = sc.nextLine().trim();

                            if (!(patientId.startsWith("APL/INP/") || patientId.startsWith("APL/OUP/"))) {
                              System.out.println("Error: Invalid patient id");
                              break;
                            }
                            List<Payment> payment = paymentService.retrievePaymentDetailsByPatientId(patientId);

                            if (payment != null) {
                              if (payment.isEmpty()) {
                                System.out.println("Error: No payment found");
                              } else {
                                menu.allPaymentDetails();
                                for (Payment pay : payment)
                                  System.out.printf("│ %-15s │ %-15s │ %-15s │ %-15s │ %-15s │ %-15s │ %-15s │\n",
                                          pay.getPaymentId(), pay.getInPatientId(), pay.getOutPatientId(), pay.getPatientName(),
                                          pay.getPaymentDate(), pay.getPaymentMode(), pay.getTotalBill());
                                System.out.println("+" + "—".repeat(125) + "+");
                              }
                            } else System.out.println("Error: Patient Id not found");
                          } catch (DBConnectionFailedException e) {
                            System.out.println(e.getMessage());
                          }
                          break;
                        }

                        // ------------------------------- Retrieve All Payment Details -------------------------------
                        case 3: {
                          List<Payment> paymentList = paymentService.retrieveAllPaymentDetails();
                          if (paymentList.isEmpty()) {
                            System.out.println("Error: No records found. Please register your records to proceed.");
                          } else {
                            menu.allPaymentDetails();
                            for (Payment pay : paymentList) {
                              System.out.printf("│ %-15s │ %-15s │ %-15s │ %-15s │ %-15s │ %-15s │ %-15s │\n",
                                      pay.getPaymentId(), pay.getInPatientId(), pay.getOutPatientId(), pay.getPatientName(),
                                      pay.getPaymentDate(), pay.getPaymentMode(), pay.getTotalBill());
                            }
                            System.out.println("+" + "—".repeat(125) + "+");
                          }

                          break;
                        }

                        // ------------------------------- Return to Previous Menu -------------------------------
                        case 4: {
                          break paymentMenu;
                        }

                        default: {
                          System.out.println("Error: Invalid Option");
                        }
                      }
                    } catch (NumberFormatException e) {
                      System.out.println("Error: Please provide valid option.");
                    }
                  }
                  break;
                }

                // ------------------------------- Go Back to Previous Menu -------------------------------
                case 4: {
                  break patientMenu;
                }
              }
            }
            break;
          }

          // ------------------------------- EXIT -------------------------------
          case 3: {
            System.out.println("Thank you for visiting our hospital");
            break mainMenu;
          }
        }
      } catch (NumberFormatException e) {
        System.out.println("Error: Invalid Input!");
      }
    }
  }
}
