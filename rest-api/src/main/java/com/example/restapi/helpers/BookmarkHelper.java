package com.example.restapi.helpers;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.specifications.BookmarkSpecification;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.restapi.util.CommonUtils.*;
import static com.example.restapi.util.CommonUtils.getSortByParam;

@Component
public class BookmarkHelper {

  public Specification<Bookmark> constructQuerySpecification(HttpServletRequest request) {
    Optional<String> queryOptional = Optional.ofNullable(request.getParameter("q"));

    Specification<Bookmark> bookmarkSpecification = Specification.where(null);

    if (queryOptional.isEmpty()) return bookmarkSpecification;

    List<String> fieldsToSearch =
        Optional.ofNullable(request.getParameter("fields"))
            .map(
                val ->
                    Arrays.stream(val.split(","))
                        .filter(
                            field ->
                                "title".equalsIgnoreCase(field)
                                    || "url".equalsIgnoreCase(field)
                                    || "description".equalsIgnoreCase(field))
                        .toList())
            .orElse(Collections.emptyList());

    if (fieldsToSearch.contains("title") || fieldsToSearch.isEmpty())
      bookmarkSpecification =
          bookmarkSpecification.and(BookmarkSpecification.hasTitleContaining(queryOptional.get()));

    if (fieldsToSearch.contains("url"))
      bookmarkSpecification =
          bookmarkSpecification.or(BookmarkSpecification.hasUrlContaining(queryOptional.get()));

    if (fieldsToSearch.contains("description"))
      bookmarkSpecification =
          bookmarkSpecification.or(
              BookmarkSpecification.hasDescriptionContaining(queryOptional.get()));

    return bookmarkSpecification;
  }

  public Pageable constructPaginationConfig(HttpServletRequest request) {
    return PageRequest.of(
        getOffsetParam(request, 0, 200, 0),
        getLimitParam(request, 1, 30, 10),
        Sort.by(getSortingOrder(request), getSortByParam(request).orElse("creationTimestamp")));
  }
}
