package com.example.gateway.exception;

public class AccessExpiredException extends RuntimeException {

  public AccessExpiredException(String message) {
    super(message);
  }

}
