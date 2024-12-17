package com.exception;

public class InvalidAppointmentTimeException extends Exception {
  public InvalidAppointmentTimeException(String message) {
    super(message);
  }
}
