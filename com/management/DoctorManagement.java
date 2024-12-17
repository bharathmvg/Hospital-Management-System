package com.management;

import com.client.Menu;
import com.exception.DBConnectionFailedException;
import com.model.Doctor;
import com.util.ApplicationUtil;

import java.sql.*;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class DoctorManagement {
  ApplicationUtil util = new ApplicationUtil();

  public int addDoctorListToDB(List<Doctor> doctorList) {
    String query = "INSERT INTO Doctor VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      int row = 0;

      for (Doctor doctor : doctorList) {
        try {
          ps.setString(1, doctor.getDoctor_id());
          ps.setString(2, doctor.getDoctor_name().toUpperCase());
          ps.setDouble(3, doctor.getDoctor_fee());
          ps.setString(4, doctor.getSpecialization().toUpperCase());
          ps.setDate(5, Date.valueOf(util.dateToStringConversion(doctor.getAvailable_date())));
          ps.setTime(6, Time.valueOf(doctor.getAvailable_time()));

          row += ps.executeUpdate();
        } catch (SQLException | IllegalArgumentException e) {
          System.out.println("Error adding doctor: " + e.getMessage());
        }
      }

      return row;
    } catch (SQLException e) {
      System.out.println("Database error: " + e.getMessage());
      return -2;
    }
  }

  public int updateDocFeeById(String doctorId, double newFees) {
    String query = "UPDATE Doctor SET DOCTOR_FEE=? WHERE DOCTOR_ID=?";
    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setDouble(1, newFees);
      ps.setString(2, doctorId);

      return ps.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error updating doctor fee: " + e.getMessage());
      return -1;
    }
  }

  public int updateDocAvailableDateById(String doctorId, java.util.Date date) {
    String query = "UPDATE Doctor SET AVAILABLE_DATE=? WHERE DOCTOR_ID=?";
    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement psUpdate = connection.prepareStatement(query)) {
      Date newDate = Date.valueOf(util.dateToStringConversion(date));
      java.util.Date curDate = new java.util.Date();
      String curDateSQL = curDate.toInstant().toString().substring(0, 10);
      Date finalCurDate = Date.valueOf(curDateSQL);

      if (newDate.compareTo(finalCurDate) < 0) return -2;

      psUpdate.setDate(1, newDate);
      psUpdate.setString(2, doctorId);
      return psUpdate.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error updating doctor's available date: " + e.getMessage());
      return -1;
    }
  }

  public Doctor retrieveDocDetailsById(String doctorId) throws DBConnectionFailedException {
    String query = "SELECT * FROM Doctor WHERE DOCTOR_ID=?";
    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, doctorId);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToDoctor(rs);
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new DBConnectionFailedException("Database connection failed: " + e.getMessage());
    }
  }

  public List<Doctor> retrieveAllDocDetails() {
    String query = "SELECT * FROM Doctor ORDER BY DOCTOR_ID";
    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      List<Doctor> list = new ArrayList<>();

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          list.add(mapResultSetToDoctor(rs));
        }
      }
      return list;
    } catch (SQLException e) {
      System.out.println("Error retrieving all doctors: " + e.getMessage());
      return null;
    }
  }

  private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
    String id = rs.getString(1);
    String name = rs.getString(2);
    double fee = rs.getDouble(3);
    String spcl = rs.getString(4);
    Date date = rs.getDate(5);
    LocalTime time = rs.getTime(6).toLocalTime();

    return new Doctor(id, name, fee, spcl, date, time);
  }

  public List<Doctor> retrieveDocDetailsByName(String doctorName) {
    String query = "SELECT * FROM Doctor WHERE LOWER(Doctor_Name) LIKE LOWER(CONCAT('%', ?, '%'))";
    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, doctorName);

      List<Doctor> list = new ArrayList<>();
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          list.add(mapResultSetToDoctor(rs));
        }
      }
      return list;
    } catch (SQLException e) {
      System.out.println("Error retrieving doctor by name: " + e.getMessage());
      return null;
    }
  }

  public String getLastId() {
    String query = "SELECT * FROM doctor ORDER BY DOCTOR_ID DESC LIMIT 1";
    try (Connection con = DBConnectionManager.establishConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query)) {
      if (rs.next()) {
        return rs.getString("DOCTOR_ID");
      }
      return null;
    } catch (SQLException e) {
      System.out.println("Error retrieving last doctor ID: " + e.getMessage());
    }
    return null;
  }

  public boolean checkIfDocExistsById(String doctorId) {
    String query = "SELECT * FROM Doctor WHERE DOCTOR_ID=?";
    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, doctorId);

      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      System.out.println("Error checking if doctor exists by ID: " + e.getMessage());
    }
    return false;
  }
}
