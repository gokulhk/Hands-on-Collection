package com.example.restapi.controllers;

import com.example.restapi.entities.Folder;
import com.example.restapi.services.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
@Log
public class FolderController {

  private final FolderService folderService;

  @GetMapping
  ResponseEntity<List<Folder>> fetchFolders() {
    return ResponseEntity.ok(folderService.fetchFolders());
  }

  @GetMapping("/{folderId}")
  ResponseEntity<Folder> fetchFolderById(@PathVariable String folderId) {
    log.info("fetching folder: " + folderId);
    return folderService
        .fetchFolderById(folderId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  ResponseEntity<Folder> addFolder(@RequestBody Folder folderPayload) {
    log.info("payload received: " + folderPayload);
    Folder folder = folderService.addFolder(folderPayload);
    return ResponseEntity.created(URI.create("/" + folder.getId())).body(folder);
  }

  @PutMapping(value = "/{folderId}")
  ResponseEntity<Folder> updateCompleteFolder(
      @PathVariable String folderId, @RequestBody Folder folderPayload) {
    log.info("payload received: " + folderPayload);
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
