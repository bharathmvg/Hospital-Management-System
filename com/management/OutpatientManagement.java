package com.management;

import com.client.Menu;
import com.exception.DBConnectionFailedException;
import com.model.Outpatient;
import com.util.ApplicationUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OutpatientManagement {
  Menu menu = new Menu();

  public int addOutpatientListToDB(List<Outpatient> outpatientList) {
    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement("insert into Outpatient values (?,?,?,?,?,?,?,?,?,?)")) {
      int row = 0;

      String name = "", phone = "";
      for (int i = 0; i < outpatientList.size(); i++) {
        try {
          Outpatient outpatient = outpatientList.get(i);
          ps.setString(1, outpatient.getPatientId());
          ps.setString(2, outpatient.getPatientName().toUpperCase());
          name = outpatient.getPatientName().toUpperCase();
          ps.setLong(3, Long.parseLong(outpatient.getPhoneNumber()));
          phone = outpatient.getPhoneNumber();
          ps.setInt(4, outpatient.getAge());
          ps.setString(5, outpatient.getGender().toUpperCase());
          ps.setString(6, outpatient.getMedicalHistory().toUpperCase());
          ps.setString(7, outpatient.getPreferredSpecialist().toUpperCase());
          ps.setDouble(8, outpatient.getMedicineFee());
          ps.setString(9, "Outpatient".toUpperCase());
          ps.setDouble(10, outpatient.getRegistration_fees());
          row += ps.executeUpdate();
        } catch (SQLException e) {
          System.out.println("Record " + (i + 1) + ": Patient with name " + name + " and phone number " + phone + " already exists");
        }
      }

      return row;
    } catch (SQLException e) {
      return -1;
    } catch (NullPointerException e) {
      System.out.println(e.getMessage());
      return -2;
    }
  }

  public int updateOutpatientPhoneNumByNameAndPhone(String patientName, String phNum, String newPh) {
    String query = "update Outpatient set PHONE_NUMBER=? where PATIENT_NAME=? and PHONE_NUMBER=?";

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, newPh);
      ps.setString(2, patientName);
      ps.setString(3, phNum);

      return ps.executeUpdate();
    } catch (SQLException e) {
      return -1;
    }
  }

  public Outpatient retrieveOutpatientDetailsById(String patientId) {
    String query = "select * from Outpatient where PATIENT_ID=?";

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, patientId);

      try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        String id = rs.getString(1);
        String name = rs.getString(2);
        String phNum = rs.getString(3);
        int age = rs.getInt(4);
        String gender = rs.getString(5);
        String medHistory = rs.getString(6);
        String prefSpcl = rs.getString(7);
        double medFee = rs.getDouble(8);
        String patientType = rs.getString(9);
        double regFee = rs.getDouble(10);

        return new Outpatient(id, name, phNum, age, gender, medHistory, prefSpcl, medFee, patientType, regFee);
      }
    } catch (SQLException e) {
//      throw new DBConnectionFailedException("Error: No Patient ID found");
      return null;
    }
  }

  public Outpatient retrieveOutpatientDetailByNameAndPhone(String patientName, String phone) throws DBConnectionFailedException {
    String query = "select * from Outpatient where lower(PATIENT_NAME) = lower(?) and PHONE_NUMBER = ?";

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, patientName);
      ps.setString(2, phone);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String id = rs.getString(1);
          String name = rs.getString(2);
          String phNum = rs.getString(3);
          int age = rs.getInt(4);
          String gender = rs.getString(5);
          String medHistory = rs.getString(6);
          String prefSpcl = rs.getString(7);
          double medFee = rs.getDouble(8);
          String patientType = rs.getString(9);
          double regFee = rs.getDouble(10);

          return new Outpatient(id, name, phNum, age, gender, medHistory, prefSpcl, medFee, patientType, regFee);
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new DBConnectionFailedException(menu.dbConErrorMsg());
    }
  }

  public List<Outpatient> retrieveAllOutpatientDetails() {
    String query = "select * from Outpatient order by PATIENT_ID";

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
      List<Outpatient> list = new ArrayList<>();

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String id = rs.getString(1);
          String name = rs.getString(2);
          String phNum = rs.getString(3);
          int age = rs.getInt(4);
          String gender = rs.getString(5);
          String medHistory = rs.getString(6);
          String prefSpcl = rs.getString(7);
          double medFee = rs.getDouble(8);
          String patientType = rs.getString(9);
          double regFee = rs.getDouble(10);

          Outpatient outpatient = new Outpatient(id, name, phNum, age, gender, medHistory, prefSpcl, medFee, patientType, regFee);
          list.add(outpatient);
        }
        return list;
      }
    } catch (SQLException e) {
      return null;
    }
  }

  public int deleteOutpatientDetailsById(String patientId) {
    String query = "delete from Outpatient where PATIENT_ID=?";

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, patientId);
      return ps.executeUpdate();
    } catch (SQLException e) {
      return -1;
    }
  }

  public String getLastId() {
    String query = "select * from Outpatient order by PATIENT_ID desc limit 1";
    try (Connection con = DBConnectionManager.establishConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query)) {

      if (rs.next()) {
        return rs.getString("PATIENT_ID");
      }

      return null;
    } catch (SQLException e) {
      e.getStackTrace();
    }

    return null;
  }

  public boolean isUserExists(String patientId) {
    String query = "select * from Outpatient where PATIENT_ID=?";

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

  public boolean isUserExists(String name, String phone) throws DBConnectionFailedException {
    String query = "select * from Outpatient where lower(PATIENT_NAME) = lower(?) and PHONE_NUMBER = ?";

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, name);
      ps.setString(2, phone);

      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new DBConnectionFailedException(menu.dbConErrorMsg());
    }
  }
}

