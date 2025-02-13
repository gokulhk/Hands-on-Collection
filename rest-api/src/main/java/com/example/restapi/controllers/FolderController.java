package com.example.restapi.controllers;

import com.example.restapi.entities.Folder;
import com.example.restapi.response.CompleteFolder;
import com.example.restapi.services.FolderService;
import com.example.restapi.util.CommonUtils;
import com.example.restapi.validators.folder.FolderPayloadValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
@Log
public class FolderController {

  private final FolderService folderService;

  private final FolderPayloadValidator folderPayloadValidator;

  @GetMapping
  ResponseEntity<List<Folder>> fetchFolders() {
    return ResponseEntity.ok(folderService.fetchFolders());
  }

  @GetMapping("/{folderId}")
  ResponseEntity<CompleteFolder> fetchFolderById(@PathVariable String folderId) {
    log.info("fetching folder: " + folderId);
    return folderService
        .fetchFolderById(folderId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  ResponseEntity<?> addFolder(@RequestBody Folder folderPayload, BindingResult validationResult) {
    log.info("payload received: " + folderPayload);

    folderPayloadValidator.validate(folderPayload, validationResult);
    if (validationResult.hasErrors()) {
      return ResponseEntity.badRequest()
          .body(CommonUtils.constructValidationErrorResponse(validationResult));
    }

    Folder folder = folderService.addFolder(folderPayload);
    return ResponseEntity.created(URI.create("/" + folder.getId())).body(folder);
  }

  @PutMapping(value = "/{folderId}")
  ResponseEntity<?> updateCompleteFolder(
      @PathVariable String folderId,
      @RequestBody Folder folderPayload,
      BindingResult validationResult) {
    log.info("payload received: " + folderPayload);

    folderPayloadValidator.validate(folderPayload, validationResult);
    if (validationResult.hasErrors()) {
      return ResponseEntity.badRequest()
          .body(CommonUtils.constructValidationErrorResponse(validationResult));
    }

    return folderService
        .updateCompleteFolder(folderId, folderPayload)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PatchMapping(value = "/{folderId}")
  ResponseEntity<Folder> partiallyUpdateFolder(
      @PathVariable String folderId, @RequestBody Folder folderPayload) {
    log.info("payload received: " + folderPayload);
    return folderService
        .partiallyUpdateFolder(folderId, folderPayload)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{folderId}")
  ResponseEntity<String> deleteFolder(@PathVariable String folderId) {
    folderService.deleteFolder(folderId);
    return ResponseEntity.noContent().build();
  }
}
