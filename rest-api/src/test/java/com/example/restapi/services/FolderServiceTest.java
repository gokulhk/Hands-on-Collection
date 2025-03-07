package com.example.restapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import com.example.restapi.entities.Folder;
import com.example.restapi.helpers.FolderHelper;
import com.example.restapi.repositories.FolderRepository;
import com.example.restapi.response.CompleteFolder;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class FolderServiceTest {
  @Autowired private FolderService folderService;

  @MockBean FolderHelper folderHelper;

  @MockBean private HttpServletRequest httpServletRequest;

  @MockBean private FolderRepository folderRepository;

  @Test
  void fetchFolders_shouldReturnListOfFolders() {
    when(folderHelper.constructPaginationConfig(any(HttpServletRequest.class)))
        .thenReturn(PageRequest.of(0, 1));
    when(folderRepository.findAll(any(PageRequest.class)))
        .thenReturn((new PageImpl<>(getSampleFolders())));

    List<Folder> folders = folderService.fetchFolders(httpServletRequest);

    verify(folderRepository).findAll(any(PageRequest.class));

    assertEquals(2, folders.size());
    assertEquals("1", folders.get(0).getId());
    assertEquals("2", folders.get(1).getId());
  }

  @Test
  void fetchFolderById_shouldReturnFolder() {
    when(folderRepository.findById("1")).thenReturn(Optional.of(getSampleFolders().getFirst()));

    Optional<CompleteFolder> completeFolder = folderService.fetchFolderById("1");

    Mockito.verify(folderRepository).findById("1");

    assertTrue(completeFolder.isPresent());
    assertEquals("1", completeFolder.get().getFolderId());
  }

  @Test
  void addFolder_shouldCreateAndReturnFolder() {
    when(folderRepository.save(any(Folder.class))).thenReturn(getSampleFolders().getFirst());

    Folder folder = folderService.addFolder(new Folder());

    verify(folderRepository).save(any(Folder.class));

    assertEquals("1", folder.getId());
  }

  @Test
  void updateCompleteFolder_ifFolderIdDoesNotExists_ReturnEmptyOptional() {
    when(folderRepository.findById("1")).thenReturn(Optional.empty());

    Optional<Folder> folderOptional = folderService.updateCompleteFolder("1", new Folder());

    verify(folderRepository).findById("1");

    assertTrue(folderOptional.isEmpty());
  }

  @Test
  void updateCompleteFolder_shouldUpdateAndReturnFolder() {
    Folder existingFolder = getSampleFolders().getFirst();
    Folder updatedFolder =
        new Folder("1", "UpdatedFolderName", List.of("bookmarkId_1"), Instant.now(), Instant.now());

    when(folderRepository.findById("1")).thenReturn(Optional.of(existingFolder));
    when(folderRepository.save(any(Folder.class))).thenReturn(updatedFolder);

    Optional<Folder> folderOptional = folderService.updateCompleteFolder("1", updatedFolder);

    verify(folderRepository).findById("1");
    verify(folderRepository).save(any(Folder.class));

    assertTrue(folderOptional.isPresent());
    assertEquals("UpdatedFolderName", folderOptional.get().getName());
  }

  @Test
  void partiallyUpdateFolder_ifFolderIdDoesNotExists_ReturnEmptyOptional() {
    when(folderRepository.findById("1")).thenReturn(Optional.empty());

    Optional<Folder> folderOptional = folderService.partiallyUpdateFolder("1", new Folder());

    verify(folderRepository).findById("1");

    assertTrue(folderOptional.isEmpty());
  }

  @Test
  void partiallyUpdateFolder_shouldUpdateAndReturnFolder() {
    Folder existingFolder = getSampleFolders().getFirst();
    Folder updatedFolder =
        new Folder("1", "UpdatedFolderName", List.of("bookmarkId_1"), Instant.now(), Instant.now());

    when(folderRepository.findById("1")).thenReturn(Optional.of(existingFolder));
    when(folderRepository.save(any(Folder.class))).thenReturn(updatedFolder);

    Optional<Folder> folderOptional = folderService.partiallyUpdateFolder("1", new Folder());

    verify(folderRepository).findById("1");
    verify(folderRepository).save(any(Folder.class));

    assertTrue(folderOptional.isPresent());
    assertEquals("UpdatedFolderName", folderOptional.get().getName());
  }

  @Test
  void deleteFolder_shouldDeleteFolder() {
    doNothing().when(folderRepository).deleteById("1");

    folderService.deleteFolder("1");

    verify(folderRepository).deleteById("1");
  }

  private List<Folder> getSampleFolders() {
    Folder folderOne =
        new Folder("1", "FolderName_1", List.of("bookmarkId_1"), Instant.now(), Instant.now());
    Folder folderTwo =
        new Folder("2", "FolderName_2", List.of("bookmarkId_2"), Instant.now(), Instant.now());
    return Arrays.asList(folderOne, folderTwo);
  }
}
