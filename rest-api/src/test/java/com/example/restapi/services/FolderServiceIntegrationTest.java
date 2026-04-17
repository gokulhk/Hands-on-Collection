package com.example.restapi.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.restapi.entities.Folder;
import com.example.restapi.repositories.FolderRepository;
import com.example.restapi.response.CompleteFolder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class FolderServiceIntegrationTest {
  @Autowired private FolderService folderService;

  @MockBean private BookmarkService bookmarkService;

  @Autowired FolderRepository folderRepository;

  @Container
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

  @DynamicPropertySource
  static void registerPgProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    registry.add("spring.jpa.show-sql", () -> true);
    registry.add(
        "spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
    registry.add("spring.jpa.properties.hibernate.dialect.storage_engine", () -> "postgresql");
  }

  @BeforeEach
  void beforeEach() {
    System.out.println("postgreSQLContainer id: " + postgreSQLContainer.getContainerId());
    folderRepository.deleteAll();
  }

  @Test
  void addFolder_shouldCreateAndReturnFolder() {
    assertNotNull(folderService.addFolder(getSampleFolder()).getId());
  }

  @Test
  void fetchFolders_shouldReturnListOfFolders() {
    Folder createdFolder = folderRepository.save(getSampleFolder());
    List<Folder> folders = folderService.fetchFolders("Planet", null, null, PageRequest.of(0, 10));

    assertEquals(1, folders.size());
    assertEquals(createdFolder.getId(), folders.get(0).getId());
  }

  @Test
  void fetchFolderById_shouldReturnFolder() {
    Folder createdFolder = folderRepository.save(getSampleFolder());
    when(bookmarkService.fetchBookmarkByIds(anyList())).thenReturn(Collections.emptyList());

    Optional<CompleteFolder> completeFolder = folderService.fetchFolderById(createdFolder.getId());

    assertTrue(completeFolder.isPresent());
    assertEquals(createdFolder.getId(), completeFolder.get().getFolderId());
  }

  @Test
  void updateCompleteFolder_ifFolderIdDoesNotExists_ReturnEmptyOptional() {
    assertTrue(folderService.updateCompleteFolder("2", new Folder()).isEmpty());
  }

  @Test
  void updateCompleteFolder_shouldUpdateAndReturnFolder() {
    Folder createdFolder = folderRepository.save(getSampleFolder());

    createdFolder.setName("UpdatedFolderName");
    createdFolder.setBookmarkIds(List.of("bookmarkId_1"));

    Optional<Folder> folderOptional =
        folderService.updateCompleteFolder(createdFolder.getId(), createdFolder);

    assertTrue(folderOptional.isPresent());
    assertEquals("UpdatedFolderName", folderOptional.get().getName());
  }

  @Test
  void partiallyUpdateFolder_ifFolderIdDoesNotExists_ReturnEmptyOptional() {
    assertTrue(folderService.partiallyUpdateFolder("1", new Folder()).isEmpty());
  }

  @Test
  void partiallyUpdateFolder_shouldUpdateAndReturnFolder() {
    Folder createdFolder = folderRepository.save(getSampleFolder());

    createdFolder.setName("UpdatedFolderName");

    Optional<Folder> folderOptional =
        folderService.partiallyUpdateFolder(createdFolder.getId(), createdFolder);

    assertTrue(folderOptional.isPresent());
    assertEquals("UpdatedFolderName", folderOptional.get().getName());
  }

  @Test
  void deleteFolder_shouldDeleteFolder() {
    Folder createdFolder = folderRepository.save(getSampleFolder());
    assertTrue(folderService.fetchFolderById(createdFolder.getId()).isPresent());

    folderService.deleteFolder(createdFolder.getId());
    assertTrue(folderService.fetchFolderById(createdFolder.getId()).isEmpty());
  }

  private Folder getSampleFolder() {
    return Folder.builder().name("Planet Collection").bookmarkIds(Collections.emptyList()).build();
  }
}
