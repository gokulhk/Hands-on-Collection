package com.example.restapi.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

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

  public static Sort.Direction getSortingOrder(HttpServletRequest request) {
    return Optional.ofNullable(request.getParameter("sort"))
        .filter(val -> val.contains(":desc"))
        .map(val -> Sort.Direction.DESC)
        .orElse(Sort.Direction.ASC);
  }

  public static Optional<String> getSortByParam(HttpServletRequest request) {
    return Optional.ofNullable(request.getParameter("sort"))
        .filter(val -> !val.isBlank())
        .map(val -> val.split(":")[0]);
  }

  public static int getOffsetParam(
      HttpServletRequest request, int minAllowed, int maxAllowed, int defaultOffset) {
    return Optional.ofNullable(request.getParameter("offset"))
        .map(Integer::parseInt)
        .filter(val -> val >= minAllowed && val <= maxAllowed)
        .orElse(defaultOffset);
  }

  public static int getLimitParam(
      HttpServletRequest request, int minAllowed, int maxAllowed, int defaultLimit) {
    return Optional.ofNullable(request.getParameter("limit"))
        .map(Integer::parseInt)
        .filter(val -> val >= minAllowed && val <= maxAllowed)
        .orElse(defaultLimit);
  }
}
