package com.example.rest_api.controllers;

import com.example.rest_api.entities.Bookmark;
import com.example.rest_api.entities.Folder;
import com.example.rest_api.repositories.FolderRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/folders")
@Log
public class FolderController {

  @Autowired FolderRepository folderRepository;

  @GetMapping
  ResponseEntity<List<Folder>> fetchFolders() {
    List<Folder> folderList = new ArrayList<>();
    folderRepository.findAll().iterator().forEachRemaining(folderList::add);
    log.info("fetched folders: " + folderList);
    return ResponseEntity.ok(folderList);
  }

  @GetMapping("/{folderId}")
  ResponseEntity<Folder> fetchFolderById(@PathVariable String folderId) {
    return ResponseEntity.ok(new Folder());
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Folder> createFolder(@RequestBody Bookmark folderPayload) {
    return ResponseEntity.created(URI.create("/{folderId}")).body(new Folder());
  }

  @PutMapping(
      value = "/{folderId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Folder> updateCompleteFolder(@RequestBody Bookmark folderPayload) {
    return ResponseEntity.ok(new Folder());
  }

  @PatchMapping(
      value = "/{folderId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Folder> partiallyUpdateFolder(@RequestBody Bookmark folderPayload) {
    return ResponseEntity.ok(new Folder());
  }

  @DeleteMapping("/{folderId}")
  ResponseEntity<String> deleteFolder(@PathVariable String folderId) {
    return ResponseEntity.noContent().build();
  }
}
