package com.example.usersregistrationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWrapper {

  private String message;
  private Object result;

  public static ResponseWrapper of(String message, Object result) {
    return new ResponseWrapper(message, result);
  }

}
