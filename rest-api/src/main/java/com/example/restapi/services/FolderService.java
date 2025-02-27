package com.example.restapi.services;

import com.example.restapi.entities.Folder;

import com.example.restapi.repositories.FolderRepository;
import com.example.restapi.response.CompleteFolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class FolderService {

  private final FolderRepository folderRepository;
  private final BookmarkService bookmarkService;

  public List<Folder> fetchFolders() {
    List<Folder> folderList = new ArrayList<>();
    folderRepository.findAll().iterator().forEachRemaining(folderList::add);
    log.info("fetched folders: " + folderList);
    return folderList;
  }

  public Optional<CompleteFolder> fetchFolderById(String folderId) {
    log.info("fetching folderId: " + folderId);

    Optional<Folder> folderOptional = folderRepository.findById(folderId);

    return folderOptional.map(
        folder ->
            CompleteFolder.builder()
                .folderId(folderId)
                .bookmarks(bookmarkService.fetchBookmarkByIds(folder.getBookmarkIds()))
                .build());
  }

  public Folder addFolder(Folder folderPayload) {
    Folder addedFolder = folderRepository.save(folderPayload);
    log.info("added folder: " + addedFolder);
    return addedFolder;
  }

  public Optional<Folder> updateCompleteFolder(String folderId, Folder folderPayload) {
    Optional<Folder> folderOptional = folderRepository.findById(folderId);

    if (folderOptional.isEmpty()) return folderOptional;

    Folder folder = folderOptional.get();
    folder.setName(folderPayload.getName());
    folder.setBookmarkIds(
        folderPayload.getBookmarkIds().stream()
            .distinct()
            .collect(Collectors.toCollection(ArrayList::new)));

    log.info("updating folder: " + folderId);
    return Optional.of(folderRepository.save(folder));
  }

  public Optional<Folder> partiallyUpdateFolder(
      @PathVariable String folderId, @RequestBody Folder folderPayload) {
    Optional<Folder> folderOptional = folderRepository.findById(folderId);

    if (folderOptional.isEmpty()) return folderOptional;

    Folder folder = folderOptional.get();
    Optional.ofNullable(folderPayload.getName()).ifPresent(folder::setName);
    Optional.ofNullable(folderPayload.getBookmarkIds())
        .ifPresent(
            bookmarkIds ->
                folder
                    .getBookmarkIds()
                    .addAll(
                        bookmarkIds.stream()
                            .distinct()
                            .filter(bookmarkId -> !folder.getBookmarkIds().contains(bookmarkId))
                            .toList()));

    log.info("patching folder: " + folderId);
    return Optional.of(folderRepository.save(folder));
  }

  public void deleteFolder(String folderId) {
    log.info("deleting folder: " + folderId);
    folderRepository.deleteById(folderId);
  }
}
