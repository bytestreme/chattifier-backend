package com.example.gateway.exception;

public class InvalidCredentialsException extends RuntimeException {


  public InvalidCredentialsException(String message) {
    super(message);
  }
}
