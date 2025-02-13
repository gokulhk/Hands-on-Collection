package com.example.restapi.validators.bookmark;

import com.example.restapi.entities.Bookmark;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class BookmarkPostPayloadValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Bookmark.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Bookmark bookmark = (Bookmark) target;
    if (Objects.isNull(bookmark.getTitle()) || bookmark.getTitle().isBlank())
      errors.rejectValue("title", "", "title is mandatory.");

    if (Objects.isNull(bookmark.getUrl()) || bookmark.getUrl().isBlank())
      errors.rejectValue("url", "", "url is mandatory.");
  }
}
