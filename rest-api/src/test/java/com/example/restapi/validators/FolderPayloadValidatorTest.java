package com.example.restapi.validators;

import com.example.restapi.entities.Folder;
import com.example.restapi.validators.folder.FolderPayloadValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.Errors;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class FolderPayloadValidatorTest {

  @Autowired private FolderPayloadValidator folderPayloadValidator;

  @MockBean private Errors errors;

  @Test
  void folderValidator_ifRequiredFieldIsNull_shouldRejectThatField() {
    folderPayloadValidator.validate(new Folder(), errors);

    verify(errors).rejectValue("name", "missing_mandatory_field", "name is mandatory.");
    verify(errors)
        .rejectValue("bookmarkIds", "missing_mandatory_field", "bookmarkIds is mandatory.");
  }

  @Test
  void folderValidator_ifRequiredFieldIsEmpty_shouldRejectThatField() {
    folderPayloadValidator.validate(
        new Folder("1", "", Collections.emptyList(), Instant.now(), Instant.now()), errors);

    verify(errors).rejectValue("name", "missing_mandatory_field", "name is mandatory.");
    verify(errors)
        .rejectValue("bookmarkIds", "missing_mandatory_field", "bookmarkIds is mandatory.");
  }

  @Test
  void folderValidator_ifValidPayload_shouldNotRejectAnyField() {
    folderPayloadValidator.validate(
        new Folder("1", "Title1", List.of("sampleBookmarkId"), Instant.now(), Instant.now()),
        errors);

    verify(errors, times(0)).rejectValue(anyString(), anyString(), anyString());
  }
}
