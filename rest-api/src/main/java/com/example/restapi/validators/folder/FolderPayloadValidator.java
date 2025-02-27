package com.example.restapi.validators.folder;

import com.example.restapi.entities.Folder;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class FolderPayloadValidator implements Validator {
  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return Folder.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(@NonNull Object target, @NonNull Errors errors) {

    Folder folder = (Folder) target;
    if (Objects.isNull(folder.getName()) || folder.getName().isBlank())
      errors.rejectValue("name", "missing_mandatory_field", "name is mandatory.");

    if (Objects.isNull(folder.getBookmarkIds()) || folder.getBookmarkIds().isEmpty())
      errors.rejectValue("bookmarkIds", "missing_mandatory_field", "bookmarkIds is mandatory.");
  }
}
