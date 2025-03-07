package com.example.restapi.helpers;

import static com.example.restapi.util.CommonUtils.*;
import static com.example.restapi.util.CommonUtils.getSortByParam;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class FolderHelper {
  public Pageable constructPaginationConfig(HttpServletRequest request) {
    return PageRequest.of(
        getOffsetParam(request, 0, 200, 0),
        getLimitParam(request, 1, 30, 10),
        Sort.by(getSortingOrder(request), getSortByParam(request).orElse("creationTimestamp")));
  }
}
