package com.example.restapi.services;

import static org.junit.jupiter.api.Assertions.*;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.repositories.BookmarkRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class BookmarkServiceIntegrationTest {

  @Autowired BookmarkService bookmarkService;

  @Autowired BookmarkRepository bookmarkRepository;

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
    bookmarkRepository.deleteAll();
  }

  @Test
  void addBookmark_shouldCreateAndReturnBookmark() {
    Bookmark createdBookmark = bookmarkService.addBookmark(getSampleBookmark());

    assertNotNull(createdBookmark.getId());
  }

  @Test
  void fetchBookmarks_shouldReturnListOfBookmarks() {
    Bookmark createdBookmark = bookmarkService.addBookmark(getSampleBookmark());

    List<Bookmark> bookmarks =
        bookmarkService.fetchBookmarks("title", "desc", null, null, PageRequest.of(0, 10));

    assertEquals(1, bookmarks.size());
    assertEquals(createdBookmark.getId(), bookmarks.get(0).getId());
  }

  @Test
  void fetchBookmarkById_shouldReturnBookmark() {
    Bookmark createdBookmark = bookmarkService.addBookmark(getSampleBookmark());

    Optional<Bookmark> bookmark = bookmarkService.fetchBookmarkById(createdBookmark.getId());

    assertTrue(bookmark.isPresent());
    assertEquals(createdBookmark.getId(), bookmark.get().getId());
  }

  @Test
  void updateCompleteBookmark_ifBookmarkIdDoesNotExists_ReturnEmptyOptional() {
    Optional<Bookmark> bookmarkOptional =
        bookmarkService.updateCompleteBookmark("2", new Bookmark());

    assertTrue(bookmarkOptional.isEmpty());
  }

  @Test
  void updateCompleteBookmark_shouldUpdateAndReturnBookmark() {
    Bookmark createdBookmark = bookmarkService.addBookmark(getSampleBookmark());

    createdBookmark.setTitle("UpdatedTitle");
    createdBookmark.setDescription("UpdatedDescription");
    createdBookmark.setUrl("http://example.com/updated");

    Optional<Bookmark> bookmarkOptional =
        bookmarkService.updateCompleteBookmark(createdBookmark.getId(), createdBookmark);

    assertTrue(bookmarkOptional.isPresent());
    assertEquals("UpdatedTitle", bookmarkOptional.get().getTitle());
    assertEquals("UpdatedDescription", bookmarkOptional.get().getDescription());
    assertEquals("http://example.com/updated", bookmarkOptional.get().getUrl());
  }

  @Test
  void partiallyUpdateBookmark_ifBookmarkIdDoesNotExists_ReturnEmptyOptional() {
    Optional<Bookmark> bookmarkOptional =
        bookmarkService.partiallyUpdateBookmark("2", new Bookmark());

    assertTrue(bookmarkOptional.isEmpty());
  }

  @Test
  void partiallyUpdateBookmark_shouldUpdateAndReturnBookmark() {
    Bookmark createdBookmark = bookmarkService.addBookmark(getSampleBookmark());

    createdBookmark.setTitle("UpdatedTitle");
    createdBookmark.setUrl("http://example.com/updated");

    Optional<Bookmark> bookmarkOptional =
        bookmarkService.partiallyUpdateBookmark(createdBookmark.getId(), createdBookmark);

    assertTrue(bookmarkOptional.isPresent());
    assertEquals("UpdatedTitle", bookmarkOptional.get().getTitle());
    assertEquals(getSampleBookmark().getDescription(), bookmarkOptional.get().getDescription());
    assertEquals("http://example.com/updated", bookmarkOptional.get().getUrl());
  }

  @Test
  void deleteBookmark_shouldDeleteBookmark() {
    Bookmark createdBookmark = bookmarkService.addBookmark(getSampleBookmark());
    assertTrue(bookmarkService.fetchBookmarkById(createdBookmark.getId()).isPresent());

    bookmarkService.deleteBookmark(createdBookmark.getId());
    assertTrue(bookmarkService.fetchBookmarkById(createdBookmark.getId()).isEmpty());
  }

  private Bookmark getSampleBookmark() {
    return Bookmark.builder()
        .title("Title1")
        .description("Description1")
        .url("http://example.com/1")
        .build();
  }
}
