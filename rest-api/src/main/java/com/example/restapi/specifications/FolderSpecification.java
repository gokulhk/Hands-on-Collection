package com.example.restapi.specifications;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.entities.Folder;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class FolderSpecification {

  public Specification<Folder> filterBy(String name, LocalDate fromDate, LocalDate toDate) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Strings.isNotEmpty(name)) {
        predicates.add(
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
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
