package com.service;

import com.client.Menu;
import com.exception.*;
import com.management.InpatientManagement;
import com.management.OutpatientManagement;
import com.model.Inpatient;
import com.model.Outpatient;
import com.util.ApplicationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutpatientService {
  ApplicationUtil util = new ApplicationUtil();
  Menu menu = new Menu();
  OutpatientManagement outpatientManagement = new OutpatientManagement();

  public int add(String[] outpatientArray) {
    List<String> strList = util.splitRecord(outpatientArray);
    List<Outpatient> outpatientList = build(strList);
    return addDoctorListToDB(outpatientList);
  }

  public List<Outpatient> build(List<String> details) {
    List<Outpatient> outpatientList = new ArrayList<>();
    int lastId = util.generateOutpatientId();

//        John Doe:9876543210:45:Male:Diabetes:Endocrinologist:500.0:1500
    for (int i = 0; i < details.size(); i++) {
      try {
        String[] detail = details.get(i).split(":");
        if (detail.length == 8) {
          String name = detail[0].trim();
          ApplicationUtil.validateName(name);
          String phNum = detail[1].trim();
          ApplicationUtil.validatePhoneNum(phNum);

          int age = Integer.parseInt(detail[2].trim());
          if(age <= 0) {
            System.out.println("Record " + (i + 1) +": Invalid Age");
            continue;
          }

          String gender = detail[3].trim();
          String medHistory = detail[4].trim();
          String prefSpcl = detail[5].trim();

          double medFee = Double.parseDouble(detail[6].trim());
          if(medFee <= 0) {
            System.out.println("Record " + (i + 1) +": Invalid Medicine Fee");
            continue;
          }

          String patientType = "Outpatient".trim();
          double regFee = Double.parseDouble(detail[7].trim());
          if(regFee < 100) {
            System.out.println("Record " + (i + 1) +": Minimum amount of Registration Fee is Rs.100");
            continue;
          }
          String id = "APL/OUP/" + ++lastId;

          outpatientList.add(new Outpatient(id, name, phNum, age, gender, medHistory, prefSpcl, medFee, patientType, regFee));
        } else throw new InvalidUserInputLength("Record " + (i + 1) + ": Please provide all the required data.");
      } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
        menu.missingDataId(i);
      } catch (InvalidPhoneNumberException | InvalidUserInputLength |
               InvalidNameException e) {
        System.out.println(e.getMessage());
      }
    }

    return outpatientList;
  }

  public int addDoctorListToDB(List<Outpatient> outpatientList) {
    return outpatientManagement.addOutpatientListToDB(outpatientList);
  }

  public int updateOutpatientPhoneNumByNameAndPhone(String patientName, String phNum, String newPh) {
    return outpatientManagement.updateOutpatientPhoneNumByNameAndPhone(patientName, phNum, newPh);
  }

  public Outpatient retrieveOutpatientDetailsById(String patientId) throws DBConnectionFailedException {
    return outpatientManagement.retrieveOutpatientDetailsById(patientId);
  }

  public Outpatient retrieveOutpatientDetailByNameAndPhone(String name, String phone) throws DBConnectionFailedException {
    return outpatientManagement.retrieveOutpatientDetailByNameAndPhone(name, phone);
  }

  public List<Outpatient> retrieveAllOutpatientDetails() {
    return outpatientManagement.retrieveAllOutpatientDetails();
  }

  public int deleteOutpatientDetailsById(String patientId) {
    return outpatientManagement.deleteOutpatientDetailsById(patientId);
  }

  public boolean isUserExists(String outpatientId) {
    return outpatientManagement.isUserExists(outpatientId);
  }

  public boolean isUserExists(String name, String phone) throws DBConnectionFailedException {
    return outpatientManagement.isUserExists(name, phone);
  }
}
