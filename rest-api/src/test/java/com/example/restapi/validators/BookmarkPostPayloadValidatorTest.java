package com.example.restapi.validators;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.validators.bookmark.BookmarkPostPayloadValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.Errors;

import java.time.Instant;

import static org.mockito.Mockito.*;

@SpringBootTest
class BookmarkPostPayloadValidatorTest {

  @Autowired private BookmarkPostPayloadValidator bookmarkPostPayloadValidator;

  @MockBean private Errors errors;

  @Test
  void bookmarkValidator_ifRequiredFieldMissing_shouldRejectThatField() {
    bookmarkPostPayloadValidator.validate(new Bookmark(), errors);

    verify(errors).rejectValue("title", "", "title is mandatory.");
    verify(errors).rejectValue("url", "", "url is mandatory.");
  }

  @Test
  void bookmarkValidator_ifValidPayload_shouldNotRejectAnyField() {
    bookmarkPostPayloadValidator.validate(
        new Bookmark(
            "1", "Title1", "Description1", "http://example.com/1", Instant.now(), Instant.now()),
        errors);

    verify(errors, times(0)).rejectValue(anyString(), anyString(), anyString());
  }
}
