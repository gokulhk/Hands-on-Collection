package com.example.restapi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.springframework.validation.BindingResult;

@Log
public class CommonUtils {

  private CommonUtils() {}

  public static Map<String, Object> constructValidationErrorResponse(BindingResult bindingResult) {
    try {
      Map<String, Object> errorResult = new HashMap<>();

      ArrayList<Map<String, String>> errorList = new ArrayList<>();
      bindingResult
          .getFieldErrors()
          .forEach(
              fieldError -> {
                Map<String, String> errorInfo = new HashMap<>();
                errorInfo.put("field", fieldError.getField());
                errorInfo.put("message", fieldError.getDefaultMessage());
                errorInfo.put("code", fieldError.getCode());
                errorList.add(errorInfo);
              });

      errorResult.put("message", "Validation failed");
      errorResult.put("errors", errorList);
      return errorResult;
    } catch (Exception e) {
      log.log(Level.SEVERE, "exception processing bindingResult errors: ", e);
    }
    return new HashMap<>();
  }
}
