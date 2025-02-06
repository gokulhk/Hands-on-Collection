package com.example.restapi.services;

import com.example.restapi.entities.Folder;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log
public class FolderService {

  public List<Folder> fetchFolders() {
    return new ArrayList<>();
  }

  public Optional<Folder> fetchFolderById(String folderId) {
    return Optional.empty();
  }

  public Folder addFolder(Folder folderPayload) {
    return new Folder();
  }

  public Optional<Folder> updateCompleteFolder(String folderId, Folder folderPayload) {
    return Optional.empty();
  }

  public Optional<Folder> partiallyUpdateFolder(
      @PathVariable String folderId, @RequestBody Folder folderPayload) {

    return Optional.empty();
  }

  public void deleteFolder(String folderId) {
    log.info("deleting folder: " + folderId);
  }
}
