package com.example.restapi.util;

import lombok.extern.java.Log;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Log
public class CommonUtils {

  public static Map<String, String> getBindingResultErrorsAsKeyValuePairs(
      BindingResult bindingResult) {
    try {
      Map<String, String> errorResult = new HashMap<>();
      bindingResult
          .getFieldErrors()
          .forEach(
              fieldError -> {
                if (errorResult.containsKey(fieldError.getField())) {
                  errorResult.put(
                      fieldError.getField(),
                      errorResult.get(fieldError.getField())
                          + ","
                          + fieldError.getDefaultMessage());
                } else {
                  errorResult.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
              });
      return errorResult;
    } catch (Exception e) {
      log.log(Level.SEVERE, "exception converting bindingResult errors to json: ", e);
    }
    return new HashMap<>();
  }
}
