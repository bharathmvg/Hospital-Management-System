package com.service;

import com.client.Menu;
import com.exception.*;
import com.management.InpatientManagement;
import com.model.Inpatient;
import com.model.Treatment;
import com.util.ApplicationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InpatientService {
  ApplicationUtil util = new ApplicationUtil();
  InpatientManagement inpatientManagement = new InpatientManagement();
  TreatmentService treatmentService = new TreatmentService();
  Menu menu = new Menu();

  public int add(String[] inpatientArray) {
      List<String> strList = util.splitRecord(inpatientArray);
      List<Inpatient> inpatientList = build(strList);
      return addInpatientListToDB(inpatientList);
  }

  public List<Inpatient> build(List<String> details) {
    List<Inpatient> inpatientList = new ArrayList<>();
    List<Treatment> treatmentList = treatmentService.getTreatmentList();
    int lastId = util.generateInpatientId();

    for (int i = 0; i < details.size(); i++) {
      try {
        String[] detail = details.get(i).split(":");
        if(detail.length == 11) {
          String name = util.capitalize(detail[0].trim());
          ApplicationUtil.validateName(name);

          String phNum = detail[1].trim();
          ApplicationUtil.validatePhoneNum(phNum);

          int age = Integer.parseInt(detail[2].trim());
          if(age <= 0) {
            System.out.println("Record " + (i + 1) +": Invalid Age");
            continue;
          }

          String gender = util.capitalize(detail[3].trim());
          String medHistory = util.capitalize(detail[4].trim());
          String prefSpcl = util.capitalize(detail[5].trim());

          double medFee = Double.parseDouble(detail[6].trim());
          if(medFee <= 0) {
            System.out.println("Record " + (i + 1) +": Invalid Medicine Fee");
            continue;
          }

          String patientType = "Inpatient";
          double admissionFee = Double.parseDouble(detail[7].trim());
          if(admissionFee < 100) {
            System.out.println("Record " + (i + 1) +": Minimum amount of Admission Fee is Rs.100");
            continue;
          }

          String treatment = util.capitalize(detail[8].trim());
          boolean isTreatmentPresent = false;
          for(Treatment temp : treatmentList) {
            if (temp.getName().equalsIgnoreCase(treatment)) {
              isTreatmentPresent = true;
              break;
            }
          }

          if(!isTreatmentPresent) {
            System.out.println("Record " + (i + 1) + ": Invalid Treatment");
            continue;
          }

          String roomType = util.capitalize(detail[9].trim());
          util.validRoomType(roomType, i);

          String wantFood = util.capitalize(detail[10].trim());
          String id = "APL/INP/" + ++lastId;

          inpatientList.add(new Inpatient(id, name, phNum, age, gender, medHistory, prefSpcl, medFee, patientType, admissionFee, treatment, roomType, wantFood));
        } else throw new InvalidUserInputLength("Record " + (i + 1) + ": Please provide all the required data.");
      } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
        menu.missingDataId(i);
      } catch(InvalidRoomTypeException | InvalidUserInputLength | InvalidNameException e) {
        System.out.println("Record " + (i + 1) + ": " + e.getMessage());
      } catch (InvalidPhoneNumberException e) {
        System.out.println("Record " + (i + 1) + ": Please provide all valid phone number.");
      }
    }

    return inpatientList;
  }

  public int addInpatientListToDB(List<Inpatient> inpatientList) {
    return inpatientManagement.addInpatientListToDB(inpatientList);
  }

  public int updateInpatientPhoneNumByNameAndPhone(String name, String phNum, String newPh) {
    return inpatientManagement.updateInpatientPhoneNumByNameAndPhone(name, phNum, newPh);
  }

  public int updateInpatientRoomType(String patientId, String room) {
    return inpatientManagement.updateInpatientRoomType(patientId, room);
  }

  public int updateFoodPreference(String patientId, String pref) {
    return inpatientManagement.updateFoodPreferrence(patientId, pref);
  }

  public Inpatient retrieveInpatientDetailByPatientId(String patientId) throws DBConnectionFailedException {
    return inpatientManagement.retrieveInpatientDetailByPatientId(patientId);
  }

  public Inpatient retrieveInpatientDetailByNameAndPhone(String name, String phone) throws DBConnectionFailedException {
    return inpatientManagement.retrieveInpatientDetailByNameAndPhone(name, phone);
  }

  public List<Inpatient> retrieveAllInpatientDetails() {
    return inpatientManagement.retrieveAllInpatientDetails();
  }

  public int deleteInpatientDetails(String patientId) {
    return inpatientManagement.deleteInpatientDetails(patientId);
  }

  public boolean isUserExists(String patientId) {
    return inpatientManagement.isUserExists(patientId);
  }

  public boolean isUserExists(String name, String phone) throws DBConnectionFailedException {
    return inpatientManagement.isUserExists(name, phone);
  }
}