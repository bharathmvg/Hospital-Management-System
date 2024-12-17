package com.service;

import com.client.Menu;
import com.exception.*;
import com.management.AllocationManagement;
import com.management.DBConnectionManager;
import com.model.Allocation;
import com.model.Inpatient;
import com.util.ApplicationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AllocationService {
  ApplicationUtil util = new ApplicationUtil();
  AllocationManagement allocationManagement = new AllocationManagement();
  InpatientService inpatientService = new InpatientService();

  public int add(String allocationDetails) throws NullPointerException {
    Allocation allocation = build(allocationDetails);
    if(allocation != null) return addAllocationListToDB(allocation);
    return -2;
  }

  public Allocation build(String details) {
    int lastid = util.generateAllocationId();

      try {
        String[] detail = details.split(":");
        if (detail.length == 4) {
          String patientId = detail[0].trim();
          patientId = ApplicationUtil.idConversion(patientId, "APL/INP/");

          Inpatient inpatient = inpatientService.retrieveInpatientDetailByPatientId(patientId);
          if (inpatient == null) {
            System.out.println("Error: Patient record not found. Please Register as Inpatient for bed allocation.");
            return null;
          }

          int roomNumber = Integer.parseInt(detail[1].trim());
          if (roomNumber <= 0) {
            System.out.println("Error: Invalid room number.");
            return null;
          }

          Date admissionDate = util.strToDateConversion(detail[2].trim());
          Date dischargeDate = util.strToDateConversion(detail[3].trim());

          if (admissionDate.before(new Date())) {
            throw new InvalidDateProvided("Error: The admission date must be a future date (later than today).");
          }

          if(dischargeDate.before(admissionDate)) {
            throw new InvalidDateProvided("Error: The discharge date must should be later than admission date");
          }

          long differenceInMillis = dischargeDate.getTime() - admissionDate.getTime();
          int noOfDays = (int) TimeUnit.MILLISECONDS.toDays(differenceInMillis);

          String treatment = util.capitalize(inpatient.getTreatment());

          String roomType = util.capitalize(inpatient.getRoomType());
          util.validRoomType(roomType);

          String wantFood = util.capitalize(inpatient.getWantFood());

          String allocationId = "APL/ALL/" + ++lastid;
          return new Allocation(allocationId, patientId, roomNumber, noOfDays, admissionDate, dischargeDate, treatment, roomType, wantFood);
        } else throw new InvalidUserInputLength("Error: Please provide all the required data.");
      } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
        System.out.println("Error: Missing or incorrect data. Please verify and provide the required information.");
      } catch (InvalidRoomTypeException | InvalidUserInputLength | DBConnectionFailedException | ParseException |
               InvalidDateProvided e) {
        System.out.println(e.getMessage());
      }

    return null;
  }

  public int addAllocationListToDB(Allocation allocationDetails) throws NullPointerException {
    return allocationManagement.addAllocationListToDB(allocationDetails);
  }

  public Allocation retrieveAllocationDetailsByPatientId(String patientId) throws DBConnectionFailedException {
    return allocationManagement.retrieveAllocationDetailsByPatientId(patientId);
  }

  public List<Allocation> retrieveAllAllocationDetails() {
    return allocationManagement.retrieveAllAllocationDetails();
  }

  public int deleteAllocationDetails(String patientId) {
    return allocationManagement.deleteAllocationDetails(patientId);
  }
}

