package com.service;

import com.client.Menu;
import com.exception.DBConnectionFailedException;
import com.exception.InvalidDateProvided;
import com.exception.InvalidNameException;
import com.exception.InvalidUserInputLength;
import com.management.DoctorManagement;
import com.model.Doctor;
import com.util.ApplicationUtil;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.*;

public class DoctorService {
  private final ApplicationUtil util = new ApplicationUtil();
  private final DoctorManagement doctorManagement = new DoctorManagement();

  public int add(String[] doctorArray) {
    List<String> strList = util.splitRecord(doctorArray);
    List<Doctor> doctorList = build(strList);
    return addDoctorListToDB(doctorList);
  }

  private List<Doctor> build(List<String> doctorList) {
    List<Doctor> docList = new ArrayList<>();
    int lastId = util.generateDoctorId();

    for (int i = 0; i < doctorList.size(); i++) {
      try {
        String[] detail = doctorList.get(i).split(":");
        if (detail.length != 6) throw new InvalidUserInputLength("All required fields must be filled in.");

        Doctor doctor = createDoctor(detail, ++lastId);
        docList.add(doctor);
      } catch (Exception e) {
        System.out.println("Error in record " + (i + 1) + ": " + e.getMessage());
      }
    }

    return docList;
  }

  private Doctor createDoctor(String[] detail, int lastId) throws InvalidDateProvided, InvalidUserInputLength, InvalidNameException {
    ApplicationUtil.validateName(detail[0].trim());
    String doctorName = formatDoctorName(detail[0].trim());
    double doctorFee = parseDoctorFee(detail[1]);
    ApplicationUtil.validateName(detail[2].trim());
    String specialization = util.capitalize(detail[2].trim());
    Date availableDate = parseAvailableDate(detail[3].trim());
    LocalTime availableTime = parseAvailableTime(detail[4].trim(), detail[5].trim());
    String doctorId = "APL/DOC/" + lastId;

    return new Doctor(doctorId, doctorName, doctorFee, specialization, availableDate, availableTime);
  }

  private String formatDoctorName(String name) {
    return "Dr. " + util.capitalize(name.trim());
  }

  private double parseDoctorFee(String feeStr) throws InvalidUserInputLength {
    try {
      double fees = Double.parseDouble(feeStr);
      if(fees <= 99) {
        throw new InvalidUserInputLength("Minimum fees: 100");
      }
      return fees;
    } catch (NumberFormatException e) {
      throw new InvalidUserInputLength("Invalid fee format.");
    }
  }

  private Date parseAvailableDate(String dateStr) throws InvalidDateProvided {
    try {
      Date date = util.strToDateConversion(dateStr);
      if (date.before(new Date())) {
        throw new InvalidDateProvided("The available date must be a future date (later than today).");
      }
      return date;
    } catch (ParseException e) {
      throw new InvalidDateProvided("Invalid date format. Please provide the date in the correct format (dd/MM/yyyy).");
    }
  }

  private LocalTime parseAvailableTime(String hourStr, String minuteStr) throws InvalidUserInputLength {
    try {
      return LocalTime.of(Integer.parseInt(hourStr), Integer.parseInt(minuteStr));
    } catch (DateTimeException e) {
      throw new InvalidUserInputLength("Invalid time format.");
    }
  }

  public int addDoctorListToDB(List<Doctor> doctorList) {
    return doctorManagement.addDoctorListToDB(doctorList);
  }

  public int updateDocFeeById(String doctorId, double newFees) {
    return doctorManagement.updateDocFeeById(doctorId, newFees);
  }

  public int updateDocAvailableDateById(String doctorId, String date) throws ParseException {
    Date newDate = util.strToDateConversion(date);
    return doctorManagement.updateDocAvailableDateById(doctorId, newDate);
  }

  public List<Doctor> retrieveDocDetailsByName(String doctorName) {
    return doctorManagement.retrieveDocDetailsByName(doctorName);
  }

  public Doctor retrieveDocDetailsById(String doctorId) throws DBConnectionFailedException {
    return doctorManagement.retrieveDocDetailsById(doctorId);
  }

  public List<Doctor> retrieveAllDocDetails() {
    return doctorManagement.retrieveAllDocDetails();
  }

  public boolean checkIfDocExistsById(String doctorId) {
    return doctorManagement.checkIfDocExistsById(doctorId);
  }
}
