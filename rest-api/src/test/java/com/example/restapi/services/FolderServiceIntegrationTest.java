package com.example.restapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.restapi.entities.Folder;
import com.example.restapi.helpers.FolderHelper;
import com.example.restapi.response.CompleteFolder;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
class FolderServiceIntegrationTest {
  @Autowired private FolderService folderService;

  @MockBean FolderHelper folderHelper;

  @MockBean private HttpServletRequest httpServletRequest;

  static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>("postgres:15")
          .withDatabaseName("bookmark_db")
          .withUsername("testUser1234")
          .withPassword("test1234#");

  static {
    postgreSQLContainer.start();
  }

  @Test
  void addFolder_shouldCreateAndReturnFolder() {
    Folder folder = folderService.addFolder(new Folder());

    assertEquals("1", folder.getId());
  }

  @Test
  void fetchFolders_shouldReturnListOfFolders() {
    when(folderHelper.constructPaginationConfig(any(HttpServletRequest.class)))
        .thenReturn(PageRequest.of(0, 1));

    List<Folder> folders = folderService.fetchFolders(httpServletRequest);

    assertEquals(1, folders.size());
    assertEquals("1", folders.get(0).getId());
  }

  @Test
  void fetchFolderById_shouldReturnFolder() {
    Optional<CompleteFolder> completeFolder = folderService.fetchFolderById("1");

    assertTrue(completeFolder.isPresent());
    assertEquals("1", completeFolder.get().getFolderId());
  }

  @Test
  void updateCompleteFolder_ifFolderIdDoesNotExists_ReturnEmptyOptional() {
    assertTrue(folderService.updateCompleteFolder("2", new Folder()).isEmpty());
  }

  @Test
  void updateCompleteFolder_shouldUpdateAndReturnFolder() {
    Folder updatedFolder =
        new Folder("1", "UpdatedFolderName", List.of("bookmarkId_1"), Instant.now(), Instant.now());

    Optional<Folder> folderOptional = folderService.updateCompleteFolder("1", updatedFolder);

    assertTrue(folderOptional.isPresent());
    assertEquals("UpdatedFolderName", folderOptional.get().getName());
  }

  @Test
  void partiallyUpdateFolder_ifFolderIdDoesNotExists_ReturnEmptyOptional() {
    assertTrue(folderService.partiallyUpdateFolder("1", new Folder()).isEmpty());
  }

  @Test
  void partiallyUpdateFolder_shouldUpdateAndReturnFolder() {
    Folder updatedFolder =
        new Folder("1", "UpdatedFolderName", List.of("bookmarkId_1"), Instant.now(), Instant.now());

    Optional<Folder> folderOptional = folderService.partiallyUpdateFolder("1", new Folder());

    assertTrue(folderOptional.isPresent());
    assertEquals("UpdatedFolderName", folderOptional.get().getName());
  }

  @Test
  void deleteFolder_shouldDeleteFolder() {
    assertTrue(folderService.fetchFolderById("1").isPresent());

    folderService.deleteFolder("1");

    assertTrue(folderService.fetchFolderById("1").isEmpty());
  }
}
