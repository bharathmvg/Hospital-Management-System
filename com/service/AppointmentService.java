package com.service;

import com.client.Menu;
import com.exception.DBConnectionFailedException;
import com.management.AppointmentManagement;
import com.model.*;
import com.util.ApplicationUtil;

import java.text.ParseException;
import java.time.*;
import java.util.*;

public class AppointmentService {
  ApplicationUtil util = new ApplicationUtil();
  AppointmentManagement appointmentManagement = new AppointmentManagement();
  OutpatientService outpatientService = new OutpatientService();
  DoctorService doctorService = new DoctorService();
  Menu menu = new Menu();

  public int add(String[] appointmentArray) {
    List<String> strList = util.splitRecord(appointmentArray);
    List<Appointment> appointmentList = build(strList);
    return addAppointmentListToDB(appointmentList);
  }

  public List<Appointment> build(List<String> appointmentList) {
    List<Appointment> appList = new ArrayList<>();
    int lastId = util.generateAppointmentId();

    for (int i = 0; i < appointmentList.size(); i++) {
      try {
        String[] detail = appointmentList.get(i).split(":");
        String patientName = detail[0].trim();
        String phoneNumber = detail[1];
        Outpatient outpatient = outpatientService.retrieveOutpatientDetailByNameAndPhone(patientName, phoneNumber);
        if (outpatient == null) {
          System.out.printf("Record %d: Patient record not found. Please Register as Outpatient to book appointment\n", i + 1);
          continue;
        }

        String specialization = util.capitalize(detail[2].trim());

        String patientId = outpatient.getPatientId();

        String doctorId = detail[3].trim();
        doctorId = ApplicationUtil.idConversion(doctorId, "APL/DOC/");

        if (doctorService.retrieveDocDetailsById(doctorId) == null) {
//          System.out.println("Hii");
          System.out.println("Record " + (i + 1) + " : No doctor with id " + doctorId + " found in the system.");
          continue;
        }

        java.util.Date date = util.strToDateConversion(detail[4]);
        LocalTime time = LocalTime.of(Integer.parseInt(detail[5]), Integer.parseInt(detail[6]));
        String mode = detail[7];
        String appointmentId = "APL/APP/" + ++lastId;

        Appointment appointment = new Appointment(appointmentId, patientId, doctorId, specialization, date, time, mode);
        appList.add(appointment);
      } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
        menu.missingDataId(i);
      } catch (ParseException e) {
        System.out.println("Record " + (i + 1) + ": Please specify the date in the correct format (dd/MM/yy).");
      } catch (DateTimeException e) {
        System.out.println("Record " + (i + 1) + ": Please specify correct time.");
      } catch (DBConnectionFailedException e) {
        System.out.println(e.getMessage());
      }
    }

    return appList;
  }

  public int addAppointmentListToDB(List<Appointment> appointmentList) {
    return appointmentManagement.addAppointmentListToDB(appointmentList);
  }

  public List<Doctor> retrieveDoctorDetailsBasedOnSpecialization(String specialization) {
    return appointmentManagement.retrieveDoctorDetailsBasedOnSpecialization(specialization);
  }

  public List<String> retrieveAppointmentDetailsByPatientId(String patientId) throws DBConnectionFailedException {
    return appointmentManagement.retrieveAppointmentDetailsByPatientId(patientId);
  }

  public List<String> retrieveAppointmentDetailsByPatientNameAndPhoneNo(String patientId, String phone) throws DBConnectionFailedException {
    return appointmentManagement.retrieveAppointmentDetailsByPatientNameAndPhoneNo(patientId, phone);
  }

  public List<Appointment> retrieveAllAppointmentDetails() {
    return appointmentManagement.retrieveAllAppointmentDetails();
  }

  public int updateAppointmentDateAndTimeByPatientId(String patientId, java.util.Date date, LocalTime time) {
    return appointmentManagement.updateAppointmentDateAndTimeByPatientId(patientId, date, time);
  }

  public int cancelAppointmentByPatientId(String appointmentId) {
    return appointmentManagement.cancelAppointmentByPatientId(appointmentId);
  }

  public int cancelAppointmentByPatientNameAndPhoneNo(String name, String phone) {
    return appointmentManagement.cancelAppointmentByPatientNameAndPhoneNo(name, phone);
  }

  public int updateAppointmentDateAndTimeByPatientNameAndPhoneNo(String name, String phone, java.util.Date date, LocalTime time) {
    return appointmentManagement.updateAppointmentDateAndTimeByPatientNameAndPhoneNo(name, phone, date, time);
  }

  public boolean checkIfUserExists(String patientId) {
    return appointmentManagement.checkIfUserExists(patientId);
  }

//  public boolean isUserExists(String name, String phone) throws DBConnectionFailedException {
//    String query = "select * from Inpatient where lower(PATIENT_NAME) like lower(name) and PHONE_NUMBER = ?";
//
//    try (Connection con = DBConnectionManager.establishConnection();
//         PreparedStatement ps = con.prepareStatement(query)) {
//
//      ps.setString(1, name);
//      ps.setString(2, phone);
//
//      try (ResultSet rs = ps.executeQuery()) {
//        return rs.next();
//      }
//    } catch (SQLException e) {
//      throw new DBConnectionFailedException(menu.dbConErrorMsg());
//    }
//  }
}
