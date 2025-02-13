package com.example.restapi.validators;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.validators.bookmark.BookmarkPayloadValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.Errors;

import java.time.Instant;

import static org.mockito.Mockito.*;

@SpringBootTest
class BookmarkPayloadValidatorTest {

  @Autowired private BookmarkPayloadValidator bookmarkPayloadValidator;

  @MockBean private Errors errors;

  @Test
  void bookmarkValidator_ifRequiredFieldMissing_shouldRejectThatField() {
    bookmarkPayloadValidator.validate(new Bookmark(), errors);

    verify(errors).rejectValue("title", "missing_mandatory_field", "title is mandatory.");
    verify(errors).rejectValue("url", "missing_mandatory_field", "url is mandatory.");
  }

  @Test
  void bookmarkValidator_ifValidPayload_shouldNotRejectAnyField() {
    bookmarkPayloadValidator.validate(
        new Bookmark(
            "1", "Title1", "Description1", "http://example.com/1", Instant.now(), Instant.now()),
        errors);

    verify(errors, times(0)).rejectValue(anyString(), anyString(), anyString());
  }
}
