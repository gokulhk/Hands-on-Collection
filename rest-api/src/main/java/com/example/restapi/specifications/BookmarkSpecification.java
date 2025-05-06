package com.example.restapi.specifications;

import com.example.restapi.entities.Bookmark;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookmarkSpecification {

  public Specification<Bookmark> filterBy(
      String title, String description, LocalDate fromDate, LocalDate toDate) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Strings.isNotEmpty(title)) {
        ParameterExpression<String> titleParam =
            criteriaBuilder.parameter(String.class, "titlePattern");
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), titleParam));
      }

      if (Strings.isNotEmpty(description)) {
        ParameterExpression<String> descParam =
            criteriaBuilder.parameter(String.class, "descriptionPattern");
        predicates.add(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), descParam));
      }
      if (fromDate != null) {
        predicates.add(
            criteriaBuilder.greaterThanOrEqualTo(root.get("creationTimestamp"), fromDate));
      }
      if (toDate != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creationTimestamp"), toDate));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
