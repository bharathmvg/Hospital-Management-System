package com.management;

import java.sql.*;
import java.io.*;
import java.util.Properties;

public class DBConnectionManager {
  private static Connection con = null;

  public static Connection establishConnection() throws SQLException {
    try (FileReader fr = new FileReader("C:\\Users\\Bharath\\Documents\\HospitalManagementSystem\\database.properties")) {
      Properties p = new Properties();
      p.load(fr);
      String driver = p.getProperty("DB_DRIVER_CLASS");
      String url = p.getProperty("DB_URL");
      String user = p.getProperty("DB_USERNAME");
      String pass = p.getProperty("DB_PASSWORD");

      Class.forName(driver);
      return DriverManager.getConnection(url, user, pass);
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("An unexpected error occurred. Please try again later or contact the system administrator if the issue persists.");
    }

    return con;
  }
}


