package com.management;

import com.client.Menu;
import com.exception.DBConnectionFailedException;
import com.model.Inpatient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InpatientManagement {
  private final Menu menu = new Menu();

  // Add Inpatient List to DB
  public int addInpatientListToDB(List<Inpatient> inpatientList) {
    String query = "INSERT INTO Inpatient VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {

      int rowCount = 0;

      for (Inpatient inpatient : inpatientList) {
        try {
          setInpatientParameters(ps, inpatient);
          rowCount += ps.executeUpdate();
        } catch (SQLException e) {
          System.out.println("Error adding inpatient " + inpatient.getPatientName() + ": " + e.getMessage());
        }
      }

      return rowCount;
    } catch (SQLException e) {
      System.out.println("Database error: " + e.getMessage());
      return -1;
    }
  }

  // Set Inpatient Parameters for PreparedStatement
  private void setInpatientParameters(PreparedStatement ps, Inpatient inpatient) throws SQLException {
    ps.setString(1, inpatient.getPatientId());
    ps.setString(2, inpatient.getPatientName());
    ps.setString(3, inpatient.getPhoneNumber());
    ps.setInt(4, inpatient.getAge());
    ps.setString(5, inpatient.getGender());
    ps.setString(6, inpatient.getMedicalHistory());
    ps.setString(7, inpatient.getPreferredSpecialist());
    ps.setDouble(8, inpatient.getMedicineFee());
    ps.setString(9, inpatient.getPatientType());
    ps.setDouble(10, inpatient.getAdmissionFees());
    ps.setString(11, inpatient.getTreatment());
    ps.setString(12, inpatient.getRoomType());
    ps.setString(13, inpatient.getWantFood());
  }

  // Update Phone Number
  public int updateInpatientPhoneNumByNameAndPhone(String name, String phNum, String newPh) {
    String query = "UPDATE Inpatient SET PHONE_NUMBER=? WHERE PATIENT_NAME=? AND PHONE_NUMBER=?";
    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, newPh);
      ps.setString(2, name);
      ps.setString(3, phNum);
      return ps.executeUpdate();
    } catch (SQLException e) {
      return -1;
    }
  }

  // Other Update Methods
  public int updateInpatientRoomType(String patientId, String room) {
    return updateInpatientField(patientId, "ROOM_TYPE", room);
  }

  public int updateFoodPreferrence(String patientId, String preference) {
    return updateInpatientField(patientId, "WANT_FOOD", preference);
  }

  private int updateInpatientField(String patientId, String field, String value) {
    String query = "UPDATE Inpatient SET " + field + " = ? WHERE PATIENT_ID = ?";
    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, value);
      ps.setString(2, patientId);
      return ps.executeUpdate();
    } catch (SQLException e) {
      return -1;
    }
  }

  // Retrieve Inpatient by ID
  public Inpatient retrieveInpatientDetailByPatientId(String patientId) throws DBConnectionFailedException {
    String query = "SELECT * FROM Inpatient WHERE PATIENT_ID = ?";
    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, patientId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return mapToInpatient(rs);
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new DBConnectionFailedException(menu.dbConErrorMsg());
    }
  }

  // Retrieve Inpatient by Name and Phone Number
  public Inpatient retrieveInpatientDetailByNameAndPhone(String name, String phone) throws DBConnectionFailedException {
    String query = "SELECT * FROM Inpatient WHERE PATIENT_NAME = ? AND PHONE_NUMBER = ?";
    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, name);
      ps.setString(2, phone);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return mapToInpatient(rs);
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new DBConnectionFailedException(menu.dbConErrorMsg());
    }
  }

  // Map ResultSet to Inpatient
  private Inpatient mapToInpatient(ResultSet rs) throws SQLException {
    return new Inpatient(
            rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4),
            rs.getString(5), rs.getString(6), rs.getString(7), rs.getDouble(8),
            rs.getString(9), rs.getDouble(10), rs.getString(11), rs.getString(12), rs.getString(13)
    );
  }

  // Retrieve All Inpatients
  public List<Inpatient> retrieveAllInpatientDetails() {
    String query = "SELECT * FROM Inpatient ORDER BY PATIENT_ID";
    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

      List<Inpatient> list = new ArrayList<>();
      while (rs.next()) {
        list.add(mapToInpatient(rs));
      }
      return list;
    } catch (SQLException e) {
      return new ArrayList<>();
    }
  }

  // Delete Inpatient by ID
  public int deleteInpatientDetails(String patientId) {
    String query = "DELETE FROM Inpatient WHERE PATIENT_ID = ?";
    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, patientId);
      return ps.executeUpdate();
    } catch (SQLException e) {
      return -1;
    }
  }

  // Check if User Exists
  public boolean isUserExists(String patientId) {
    String query = "SELECT COUNT(*) FROM Inpatient WHERE PATIENT_ID = ?";
    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, patientId);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() && rs.getInt(1) > 0;
      }
    } catch (SQLException e) {
      return false;
    }
  }

  public boolean isUserExists(String name, String phone) throws DBConnectionFailedException {
    String query = "SELECT COUNT(*) FROM Inpatient WHERE PATIENT_NAME = ? AND PHONE_NUMBER = ?";
    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, name);
      ps.setString(2, phone);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() && rs.getInt(1) > 0;
      }
    } catch (SQLException e) {
      throw new DBConnectionFailedException(menu.dbConErrorMsg());
    }
  }

  public String getLastId() {
    String query = "SELECT * FROM Inpatient ORDER BY PATIENT_ID DESC LIMIT 1";
    try (Connection con = DBConnectionManager.establishConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query)) {
      if (rs.next()) {
        return rs.getString("PATIENT_ID");
      }
      return null;
    } catch (SQLException e) {
      System.out.println("Error retrieving last INPATIENT ID: " + e.getMessage());
    }
    return null;
  }
}
