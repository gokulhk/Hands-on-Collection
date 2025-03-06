package com.example.restapi.specifications;

import com.example.restapi.entities.Bookmark;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class BookmarkSpecification {
  public static Specification<Bookmark> hasTitleContaining(String searchTerm) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(
            criteriaBuilder.lower(root.get("title")), "%" + searchTerm.toLowerCase() + "%");
  }

  public static Specification<Bookmark> hasUrlContaining(String searchTerm) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(
            criteriaBuilder.lower(root.get("url")), "%" + searchTerm.toLowerCase() + "%");
  }

  public static Specification<Bookmark> hasDescriptionContaining(String searchTerm) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(
            criteriaBuilder.lower(root.get("description")), "%" + searchTerm.toLowerCase() + "%");
  }

  public static Specification<Bookmark> matchesOneOfIds(List<String> ids) {
    return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), ids));
  }
}
