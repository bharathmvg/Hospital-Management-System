package com.management;

import com.exception.DBConnectionFailedException;
import com.model.Inpatient;
import com.model.Outpatient;
import com.model.Patient;
import com.model.Payment;
import com.util.ApplicationUtil;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PaymentManagement {
  ApplicationUtil util = new ApplicationUtil();

  public int addPaymentToDB(Payment payment) {
    String query = "INSERT into payment values (?,?,?,?,?,?,?,?)";

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

      ps.setString(1, payment.getPaymentId());

      if (payment.getInPatientId() == null) {
        ps.setString(2, null);
        ps.setString(3, payment.getOutPatientId());
      } else {
        ps.setString(2, payment.getInPatientId());
        ps.setString(3, null);
      }

      ps.setString(4, util.capitalize(payment.getPatientName()));
      ps.setString(5, util.capitalize(payment.getPatientType()));
      ps.setDate(6, Date.valueOf(util.dateToStringConversion(payment.getPaymentDate())));
      ps.setString(7, util.capitalize(payment.getPaymentMode()));
      ps.setDouble(8, payment.getTotalBill());
      return ps.executeUpdate();
    } catch (SQLException | NullPointerException e) {
      System.out.println("Database Error: " + e.getMessage());
    }

    return 0;
  }


  public List<String> retrievePaymentDetailsByNameAndPhone(String name, String phone, String patientType) {
    String query;
    List<String> list = new ArrayList<>();

    if (patientType.equalsIgnoreCase("oup"))
      query = """
              select * from payment p
              inner join outpatient o
              on p.patient_id = o.patient_id
              where lower(o.patient_name) = lower('?') and phone_number = ?;
              """;
    else
      query = """
              select * from payment p
              inner join inpatient o
              on p.patient_id = o.patient_id
              where lower(o.patient_name) = lower('?') and phone_number = ?;
              """;

    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setString(1, name);
      ps.setString(2, phone);
      ResultSet rs = ps.executeQuery(query);

      if (rs.next()) {
        list.add(rs.getString("payment_id"));
        list.add(rs.getString("patient_name"));
        list.add(rs.getString("phone_number"));
        list.add(util.dateToStringConversion(rs.getDate("payment_date")));
        list.add(rs.getString("mode_of_payment"));
        list.add(rs.getString("bill_amount"));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
//      return null;
    }
    return list;
  }

//  public boolean isUserExists(String paymentId) {
//
//    String query = "SELECT patient_id from payment WHERE payment_id=?";
//    try (Connection con = DBConnectionManager.establishConnection();
//         PreparedStatement ps = con.prepareStatement(query)) {
//
//      ps.setString(1, paymentId);
//
//      try (ResultSet rs = ps.executeQuery()) {
//        return rs.next();
//      }
//
//    } catch (SQLException e) {
//      e.getStackTrace();
//    }
//    return false;
//  }

  public List<Payment> retrieveAllPaymentDetails() {
    String query = "SELECT * from payment order by payment_Id";

    try (Connection con = DBConnectionManager.establishConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
      List<Payment> paymentDetails = new ArrayList<>();

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String paymentId = rs.getString("payment_id");
          String inpatientId = rs.getString("inpatient_id");
          String outpatientId = rs.getString("outpatient_id");
          String patientName = rs.getString("patient_name");
          String patientType = rs.getString("patient_type");
          java.util.Date paymentDate = rs.getDate("payment_date");
          String paymentMode = rs.getString("mode_of_payment");
          double bill = rs.getDouble("bill_amount");

          paymentDetails.add(new Payment(paymentId, inpatientId, outpatientId, patientName, patientType, paymentDate, paymentMode, bill));
        }

        return paymentDetails;
      }

    } catch (SQLException e) {
      System.out.println("Database Error: " + e.getMessage());
      return null;
    }
  }

  public List<Payment> retrievePaymentDetailsByPatientId(String patientId) {
    String query = """
            SELECT *
            FROM payment p
            INNER JOIN %s o ON p.%s = o.patient_id
            WHERE p.%s = ? order by payment_id
            """;

    String patientRole = patientId.substring(4, 7).toLowerCase();
    String tableName = null, colName = null;
    List<Payment> paymentDetails = new ArrayList<>();

    if (patientRole.equalsIgnoreCase("oup")) {
      tableName = "outpatient";
      colName = "outpatient_id";
    } else {
      tableName = "inpatient";
      colName = "inpatient_id";
    }

    query = String.format(query, tableName, colName, colName);

    try (Connection connection = DBConnectionManager.establishConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {

      ps.setString(1, patientId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String paymentId = rs.getString("payment_id");
          String inpatientId = rs.getString("inpatient_id");
          String outpatientId = rs.getString("outpatient_id");
          String patientName = rs.getString("patient_name");
          String patientType = rs.getString("patient_type");
          java.util.Date paymentDate = rs.getDate("payment_date");
          String paymentMode = rs.getString("mode_of_payment");
          double bill = rs.getDouble("bill_amount");

          paymentDetails.add(new Payment(paymentId, inpatientId, outpatientId, patientName, patientType, paymentDate, paymentMode, bill));
        }
      }
      return paymentDetails;
    } catch (SQLException e) {
       System.out.println("Database Error: " + e.getMessage() + " " + tableName + " " + colName);
      return null;
    }
  }

  public String getLastId() {
    String query = "select * from payment order by payment_id desc limit 1";
    try (Connection con = DBConnectionManager.establishConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query)) {

      if (rs.next()) {
        return rs.getString("payment_id");
      }

      return null;
    } catch (SQLException e) {
      e.getStackTrace();
    }

    return null;
  }
}
