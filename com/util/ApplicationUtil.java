package com.util;

import com.exception.*;
import com.management.*;
import com.model.Treatment;
import com.service.TreatmentService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.*;

public class ApplicationUtil {
  public List<String> splitRecord(String[] detail) {
    return Arrays.asList(detail);
  }

  public Date strToDateConversion(String date) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    sdf.setLenient(false);
    return sdf.parse(date);
  }

  public String dateToStringConversion(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(date);
  }

  public String capitalize(String str) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    str = str.trim();
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }

  public LocalTime checkAppointmentTimeLimit(String time) throws InvalidAppointmentTimeException {
    String[] timeComponents = time.split(":");
    int hour = Integer.parseInt(timeComponents[0]);
    int minute = Integer.parseInt(timeComponents[1]);

    if (hour >= 10 && hour <= 20 && minute >= 0 && minute <= 59)
      return LocalTime.of(hour, minute);

    throw new InvalidAppointmentTimeException("Error: Please enter time between 10:00 and 20:00");
  }

  public static boolean validateName(String name) throws InvalidNameException {
    Pattern pattern = Pattern.compile("(\\d|[!@#$%^&*()~`{}]|^$|\\s{2,})");
    Matcher matcher = pattern.matcher(name);

    if (matcher.find()) throw new InvalidNameException("Invalid name. Please provide valid name");
    return true;
  }

  public boolean validateId(String id) {
    if (id.length() < 11) {
      return false;
    } else {
      String id1 = id.substring(0, 8);

      return id1.equals("APL/DOC/") || id1.equals("APL/INP/") || id1.equals("APL/OUP/") ||
              id1.equals("APL/APP/") || id1.equals("APL/ALL/") || id1.equals("APL/PAY/");
    }
  }

  public static boolean validatePhoneNum(String phNum) throws InvalidPhoneNumberException {
    if (phNum.length() == 10) {
      String pattern = "^[6-9][0-9]{9}$";
      if (phNum.matches(pattern)) {
        return true;
      }
    }

    throw new InvalidPhoneNumberException("Error: Invalid Phone Number. Please enter valid phone number");
  }

  public void validRoomType(String room) throws InvalidRoomTypeException {
    String[] validRoomTypes = {
            "general ward",
            "semi-private",
            "private",
            "deluxe",
            "suite",
            "icu",
            "hdu",
            "maternity",
            "pediatric",
            "isolation"
    };

    List<String> isRoom = Arrays.asList(validRoomTypes);

    if (isRoom.contains(room.toLowerCase())) return;

    throw new InvalidRoomTypeException("Error: Invalid room type. Please enter a valid room type from the list.");
  }

  public void validRoomType(String room, int i) throws InvalidRoomTypeException {
    String[] validRoomTypes = {
            "general ward",
            "semi-private",
            "private",
            "deluxe",
            "suite",
            "icu",
            "hdu",
            "maternity",
            "pediatric",
            "isolation"
    };

    List<String> isRoom = Arrays.asList(validRoomTypes);

    if (isRoom.contains(room.toLowerCase())) return;

    throw new InvalidRoomTypeException("Record "+ (i + 1) +": Invalid room type. Check the list of rooms available by entering option 4 in menu.");
  }

  public static String idConversion(String id, String prefix) {
    if (!checkPrefix(id) && id.matches("^[0-9]{3,}$"))
      return prefix + id;
    return id;
  }

  public static boolean checkPrefix(String id) {
//    Pattern pattern = Pattern.compile("^(APL/DOC/|apl/doc/|APL/INP/|apl/inp/|APL/OUP/|apl/oup/|APL/ALL/|apl/all/|APL/APP/|apl/app)");
    Pattern pattern = Pattern.compile("^(APL/DOC/|APL/INP/|APL/OUP/|APL/ALL/|APL/APP/|APL/PAY/)");
    Matcher matcher = pattern.matcher(id);

    return matcher.find();
  }

  public int generateDoctorId() {
    DoctorManagement obj = new DoctorManagement();
    String lastId = obj.getLastId(); // APL/DOC/102

    if (lastId != null) {
      Pattern pattern = Pattern.compile("[0-9]+");
      Matcher matcher = pattern.matcher(lastId);

      if (matcher.find())
        return Integer.parseInt(matcher.group());

    }

    return 100;
  }

  public int generateInpatientId() {
    InpatientManagement obj = new InpatientManagement();
    String lastId = obj.getLastId();

    if (lastId != null) {
      Pattern pattern = Pattern.compile("[0-9]+");
      Matcher matcher = pattern.matcher(lastId);
      int id = 0;

      if (matcher.find())
        id = Integer.parseInt(matcher.group());

      return id;
    }

    return 100;
  }

  public int generateOutpatientId() {
    OutpatientManagement obj = new OutpatientManagement();
    String lastId = obj.getLastId();

    if (lastId != null) {
      Pattern pattern = Pattern.compile("[0-9]+");
      Matcher matcher = pattern.matcher(lastId);
      int id = 0;

      if (matcher.find())
        id = Integer.parseInt(matcher.group());

      return id;
    }

    return 100;
  }

  public int generateAppointmentId() {
    AppointmentManagement obj = new AppointmentManagement();
    String lastId = obj.getLastId();

    if (lastId != null) {
      Pattern pattern = Pattern.compile("[0-9]+");
      Matcher matcher = pattern.matcher(lastId);
      int id = 0;

      if (matcher.find())
        id = Integer.parseInt(matcher.group());

      return id;
    }

    return 100;
  }

  public int generateAllocationId() {
    AllocationManagement obj = new AllocationManagement();
    String lastId = obj.getLastId();

    if (lastId != null) {
      Pattern pattern = Pattern.compile("[0-9]+");
      Matcher matcher = pattern.matcher(lastId);
      int id = 0;

      if (matcher.find())
        id = Integer.parseInt(matcher.group());

      return id;
    }

    return 100;
  }

  public int generatePaymentId() {
    PaymentManagement obj = new PaymentManagement();
    String lastId = obj.getLastId();

    if (lastId != null) {
      Pattern pattern = Pattern.compile("[0-9]+");
      Matcher matcher = pattern.matcher(lastId);
      int id = 0;

      if (matcher.find())
        id = Integer.parseInt(matcher.group());

      return id;
    }

    return 100;
  }

  public String checkDoctorIdIsPresent(Scanner sc) {
    System.out.print("Please enter the Doctor ID (e.g., APL/DOC/100): ");
    String doctorId = sc.nextLine().toUpperCase();

    doctorId = idConversion(doctorId, "APL/DOC/");

    if (doctorId.startsWith("APL/DOC/")) return doctorId;
    else return "";
  }

  public String checkInpatientIdIsPresent(Scanner sc, boolean id) {
    if (id) {
      System.out.print("Please enter the Inpatient ID (e.g., APL/INP/100): ");
      String inpatientId = sc.nextLine().toUpperCase();

      inpatientId = idConversion(inpatientId, "APL/INP/");

      if (inpatientId.startsWith("APL/INP/")) return inpatientId;
    } else {
      try {
        System.out.print("Please enter Patient's name: ");
        String name = sc.nextLine().trim();
        validateName(name);

        System.out.print("Please enter Patient's Contact Detail: ");
        String phone = sc.nextLine().trim();
        validatePhoneNum(phone);

        return name + "," + phone;
      } catch (InvalidNameException | InvalidPhoneNumberException e) {
        System.out.println(e.getMessage());
      }
    }
    return "";
  }

  public String checkOutpatientIdIsPresent(Scanner sc, boolean id) {
    if (id) {
      System.out.print("Please enter the Outpatient ID (e.g., APL/OUP/100): ");
      String outpatientId = sc.nextLine().toUpperCase();

      outpatientId = idConversion(outpatientId, "APL/OUP/");

      return outpatientId;
    } else {
      try {
        System.out.print("Please enter Patient's name: ");
        String name = sc.nextLine().trim();
        validateName(name);

        System.out.print("Please enter Patient's Contact Detail: ");
        String phone = sc.nextLine().trim();
        validatePhoneNum(phone);

        return name + "," + phone;
      } catch (InvalidNameException | InvalidPhoneNumberException e) {
        System.out.println(e.getMessage());
      }
    }
    return "";
  }

  public Map<String, Integer> roomPrice() {
    Map<String, Integer> roomPrices = new HashMap<>();
    roomPrices.put("General Ward", 100);
    roomPrices.put("Semi-Private", 270);
    roomPrices.put("Private", 500);
    roomPrices.put("Deluxe", 800);
    roomPrices.put("Suite", 1500);
    roomPrices.put("ICU", 2500);
    roomPrices.put("HDU", 2500);
    roomPrices.put("Maternity", 1200);
    roomPrices.put("Pediatric", 1000);
    roomPrices.put("Isolation", 1800);

    return roomPrices;
  }
}


