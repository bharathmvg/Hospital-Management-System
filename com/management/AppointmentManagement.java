package com.management;

import com.client.Menu;
import com.exception.DBConnectionFailedException;
import com.model.Appointment;
import com.model.Doctor;
import com.model.Inpatient;
import com.model.Outpatient;
import com.service.OutpatientService;
import com.util.ApplicationUtil;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentManagement {
  ApplicationUtil util = new ApplicationUtil();
  Menu menu = new Menu();

  public int addAppointmentListToDB(List<Appointment> appointmentList) {
    String query = "insert into Appointment values (?,?,?,?,?,?,?)";

    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      int row = 0;

      for (int i = 0; i < appointmentList.size(); i++) {
        try {
          Appointment app = appointmentList.get(i);
          Date date = Date.valueOf(util.dateToStringConversion(app.getAppointmentDate()));
          Time time = Time.valueOf(app.getAppointmentTime());

          ps.setString(1, app.getAppointmentId());
          ps.setString(2, app.getPatientId());
          ps.setString(3, app.getDoctorId());
          ps.setString(4, app.getSpecialization());
          ps.setDate(5, date);
          ps.setTime(6, time);
          ps.setString(7, app.getAppointmentMode().toUpperCase());
          row += ps.executeUpdate();
        } catch (SQLException e) {
          System.out.println("Record " + (i + 1) + ": " + e.getMessage());
        }
      }

      return row;
    } catch (SQLException e) {
      System.out.println("Database Error: " + e.getMessage());
      return -1;
    }
  }

  public List<Doctor> retrieveDoctorDetailsBasedOnSpecialization(String specialization) {
    String query = "select * from Doctor where lower(SPECIALIZATION) like lower(concat('%',?,'%')) order by doctor_id";

    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      List<Doctor> list = new ArrayList<>();

      ps.setString(1, specialization);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String id = rs.getString(1);
          String name = rs.getString(2);
          double fee = rs.getDouble(3);
          String spcl = rs.getString(4);
          Date date = rs.getDate(5);
          LocalTime time = rs.getTime(6).toLocalTime();

          Doctor doc = new Doctor(id, name, fee, spcl, date, time);
          list.add(doc);
        }
        return list;
      }
    } catch (SQLException e) {
      System.out.println("Database Error: " + e.getMessage());
      return null;
    }
  }

  public List<String> retrieveAppointmentDetailsByPatientId(String patientId) throws DBConnectionFailedException {
    String query = """
            select patient_name, phone_number, doctor_id, appointment_date, appointment_time, mode_of_appointment
            from appointment a
            inner join outpatient o
            on a.patient_id = o.patient_id
            where a.patient_id = ?
            """;

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      List<String> list = new ArrayList<>();
      ps.setString(1, patientId);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          list.add(rs.getString(1));
          list.add(rs.getString(2));
          list.add(rs.getString(3));
          list.add(util.dateToStringConversion(rs.getDate(4)));
          list.add(rs.getTime(5).toString());
          list.add(rs.getString(6));

          return list;
        } else {
//          menu.idNotFound("Appointment");
          return null;
        }
      }
    } catch (SQLException e) {
//      System.out.println(e.getMessage());
      throw new DBConnectionFailedException(menu.dbConErrorMsg());
    }
  }

  public List<String> retrieveAppointmentDetailsByPatientNameAndPhoneNo(String name, String phone) throws DBConnectionFailedException {
    String query = """
            select patient_name, phone_number, doctor_id, appointment_date, appointment_time, mode_of_appointment
            from appointment a
            inner join outpatient o
            on a.patient_id = o.patient_id
            where lower(PATIENT_NAME) = lower(?) and PHONE_NUMBER = ?
            """;

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      List<String> list = new ArrayList<>();

      ps.setString(1, name);
      ps.setString(2, phone);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          list.add(rs.getString(1));
          list.add(rs.getString(2));
          list.add(rs.getString(3));
          list.add(util.dateToStringConversion(rs.getDate(4)));
          list.add(rs.getTime(5).toString());
          list.add(rs.getString(6));

          return list;
        } else return null;
      }
    } catch (SQLException e) {
      throw new DBConnectionFailedException(menu.dbConErrorMsg());
    }
  }

  public int updateAppointmentDateAndTimeByPatientId(String patientId, java.util.Date date, LocalTime time) {
    String query = "update Appointment set APPOINTMENT_DATE = ?, APPOINTMENT_TIME = ? where PATIENT_ID = ?";

    Date newDate = Date.valueOf(util.dateToStringConversion(date));

    java.util.Date curDate = new java.util.Date();
    String curDateSQL = curDate.toInstant().toString().substring(0, 10);
    Date finalCurDate = Date.valueOf(curDateSQL);

    if (newDate.compareTo(finalCurDate) < 0) return -1;

    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setDate(1, Date.valueOf(util.dateToStringConversion(date)));
      ps.setTime(2, Time.valueOf(time));
      ps.setString(3, patientId);
      return ps.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Database Error: " + e.getMessage());
      return -2;
    }
  }

  public int updateAppointmentDateAndTimeByPatientNameAndPhoneNo(String name, String phone, java.util.Date date, LocalTime time) {
    String query = """
            Update Appointment a inner join Outpatient o on a.patient_id = o.patient_id
            set a.APPOINTMENT_DATE = ?, a.APPOINTMENT_TIME = ?
            where o.patient_name = ? and o.phone_number = ?
            """;

    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setDate(1, Date.valueOf(util.dateToStringConversion(date)));
      ps.setTime(2, Time.valueOf(time));
      ps.setString(3, name);
      ps.setString(4, phone);
      return ps.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Database Error: " + e.getMessage());
      return -2;
    }
  }

  public int cancelAppointmentByPatientId(String patientId) {
    String query = "delete from Appointment where patient_id=?";

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, patientId);
      return ps.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Database Error: " + e.getMessage());
      return -1;
    }
  }

  public int cancelAppointmentByPatientNameAndPhoneNo(String name, String phone) {
    String getQuery = "select patient_id from outpatient where patient_name = ? and phone_number = ?";
    String delQuery = "delete from appointment where patient_id=?";
    String patientId = null;

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(getQuery);
         PreparedStatement ps1 = con.prepareStatement(delQuery)) {
      ps.setString(1, name);
      ps.setString(2, phone);

      try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        patientId = rs.getString("patient_id");
        ps1.setString(1, patientId);
        return ps1.executeUpdate();
      }
    } catch (SQLException e) {
      System.out.println("Database Error: " + e.getMessage());
      return -1;
    }
  }

  public List<Appointment> retrieveAllAppointmentDetails() {
    String query = "select * from Appointment order by Patient_id";

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
      List<Appointment> list = new ArrayList<>();

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String appointment_id = rs.getString(1);
          String patient_id = rs.getString(2);
          String doctor_id = rs.getString(3);
          String specialization = rs.getString(4);
          Date date = rs.getDate(5);
          LocalTime time = rs.getTime(6).toLocalTime();
          String mode = rs.getString(7);

          list.add(new Appointment(appointment_id, patient_id, doctor_id, specialization, date, time, mode));
        }
        return list;
      }
    } catch (SQLException e) {
      System.out.println("Database Error: " + e.getMessage());
      return null;
    }
  }

  public boolean checkIfUserExists(String patientId) {
    String query = "select * from Appointment where PATIENT_ID=?";

    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {

      ps.setString(1, patientId);

      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      e.getStackTrace();
    }

    return false;
  }

  public String getLastId() {
    String query = "select * from Appointment order by APPOINTMENT_ID desc limit 1";
    try (Connection con = DBConnectionManager.establishConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query)) {

      if (rs.next()) return rs.getString("APPOINTMENT_ID");

      return null;
    } catch (SQLException e) {
      e.getStackTrace();
    }

    return null;
  }
}
