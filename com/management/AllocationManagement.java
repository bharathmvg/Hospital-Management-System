//package com.management;

//import com.model.Allocation;
//
//import java.util.List;
//
//public class AllocationManagement {
//  public int addAllocationListToDB(List<Allocation> allocationList) {
//    return 0;
//  }
//
//  public List<String> retrieveAllocationDetailsByPatientId(String inpatientId) {
//    return null;
//  }
//
//  public List<String> retrieveAllocationDetailsByPatientNameAndPhone(String name, String phone) {
//    return null;
//  }
//
//  public List<Allocation> retrieveAllAllocationDetails() {
//    return null;
//  }
//
//  public int deleteAllocationDetailsByPatientId(String allocationId) {
//    return 0;
//  }
//
//  public int deleteAllocationDetailsByPatientNameAndPhone(String name, String phone) {
//    return 0;
//  }
//
//  public String getLastId() {
//    return null;
//  }
//
//  public boolean isUserExists(String patientId) {
//    return false;
//  }
//}

package com.management;

import com.client.Menu;
import com.exception.DBConnectionFailedException;
import com.model.Allocation;
import com.model.Outpatient;
import com.util.ApplicationUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AllocationManagement {
  Menu menu = new Menu();
  ApplicationUtil util = new ApplicationUtil();

  public int addAllocationListToDB(Allocation allocation) {
    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement("insert into Allocation values (?,?,?,?,?,?,?,?,?)")) {

      Date admissionDate = Date.valueOf(util.dateToStringConversion(allocation.getAdmissionDate()));
      Date dischargeDate = Date.valueOf(util.dateToStringConversion(allocation.getDischargeDate()));

      ps.setString(1, allocation.getAllocationId());
      ps.setString(2, allocation.getPatientId());
      ps.setInt(3, allocation.getRoomNumber());
      ps.setInt(4, allocation.getNoOfDaysAdmitted());
      ps.setDate(5, admissionDate);
      ps.setDate(6, dischargeDate);
      ps.setString(7, allocation.getTreatment());
      ps.setString(8, allocation.getRoomType());
      ps.setString(9, allocation.getWantFood());

      return ps.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Database error: " + e.getMessage());
      return -1;
    }
  }

  public Allocation retrieveAllocationDetailsByPatientId(String patient_id) throws DBConnectionFailedException {
    String query = "select * from Allocation where patient_id=?";

    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {

      ps.setString(1, patient_id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String allocation_id = rs.getString(1);
          String patient_Id = rs.getString(2);
          int room_no = rs.getInt(3);
          int days = rs.getInt(4);
          java.util.Date add_date = rs.getDate(5);
          java.util.Date dis_date = rs.getDate(6);
          String treatment = rs.getString(7);
          String room_type = rs.getString(8);
          String food = rs.getString(9);

          return new Allocation(allocation_id, patient_Id, room_no, days, add_date, dis_date, treatment, room_type, food);
        } else return null;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw new DBConnectionFailedException(menu.dbConErrorMsg());
    }
  }

//  public Allocation retrieveAllocationDetailsByNameAndPhone(String name, String phone) throws DBConnectionFailedException {
//    String query = """
//            select * from allocation a
//            inner join inpatient i
//            on a.patient_id = i.patient_id
//            where lower(patient_name) = lower(?) and phone_number = ?
//            """;
//
//    try (Connection con = DBConnectionManager.establishConnection();
//         PreparedStatement ps = con.prepareStatement(query)) {
//
//      ps.setString(1, name);
//      ps.setString(2, phone);
//
//      try (ResultSet rs = ps.executeQuery()) {
//        if (rs.next()) {
//          String allocation_id = rs.getString(1);
//          String patient_Id = rs.getString(2);
//          int room_no = rs.getInt(3);
//          int days = rs.getInt(4);
//          java.util.Date add_date = rs.getDate(5);
//          java.util.Date dis_date = rs.getDate(6);
//          String treatment = rs.getString(7);
//          String room_type = rs.getString(8);
//          String food = rs.getString(9);
//
//          return new Allocation(allocation_id, patient_Id, room_no, days, add_date, dis_date, treatment, room_type, food);
//        } else return null;
//      }
//    } catch (SQLException e) {
//      throw new DBConnectionFailedException(menu.dbConErrorMsg());
//    }
//  }

  public List<Allocation> retrieveAllAllocationDetails() {
    String query = "select * from allocation order by patient_id";

    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      List<Allocation> list = new ArrayList<>();

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String allocation_id = rs.getString(1);
          String patient_Id = rs.getString(2);
          int room_no = rs.getInt(3);
          int days = rs.getInt(4);
          java.util.Date add_date = rs.getDate(5);
          java.util.Date dis_date = rs.getDate(6);
          String treatment = rs.getString(7);
          String room_type = rs.getString(8);
          String food = rs.getString(9);
          Allocation alloc = new Allocation(allocation_id, patient_Id, room_no, days, add_date, dis_date, treatment, room_type, food);
          list.add(alloc);
        }
        return list;
      }
    } catch (SQLException e) {
      return null;
    }
  }

  public int deleteAllocationDetails(String patient_id) {
    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement("delete from Allocation where patient_id=?")) {

      ps.setString(1, patient_id);
      return ps.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return -1;
    }
  }

  public String getLastId() {
    String query = "select * from Allocation order by allocation_id desc limit 1";
    try (Connection con = DBConnectionManager.establishConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query)) {

      rs.next();
      return rs.getString("allocation_id");

    } catch (SQLException e) {
      e.getStackTrace();
    }
    return null;
  }
}
