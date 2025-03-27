package com.example.restapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.helpers.BookmarkHelper;
import com.example.restapi.repositories.BookmarkRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookmarkServiceIntegrationTest {

  @Autowired BookmarkService bookmarkService;

  @MockBean BookmarkHelper bookmarkHelper;
  @MockBean HttpServletRequest httpServletRequest;
  @Autowired BookmarkRepository bookmarkRepository;

  static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>("postgres:15")
          .withDatabaseName("bookmark_db")
          .withUsername("testUser1234")
          .withPassword("test1234#");

  static {
    postgreSQLContainer.start();
  }

  @Test
  void addBookmark_shouldCreateAndReturnBookmark() {
    Bookmark bookmarkToCreate =
        new Bookmark(
            "1", "Title1", "Description1", "http://example.com/1", Instant.now(), Instant.now());

    Bookmark bookmark = bookmarkService.addBookmark(bookmarkToCreate);

    assertEquals("1", bookmark.getId());
  }

  @Test
  void fetchBookmarks_shouldReturnListOfBookmarks() {
    when(bookmarkHelper.constructPaginationConfig(any(HttpServletRequest.class)))
        .thenReturn(PageRequest.of(0, 1));
    when(bookmarkHelper.constructQuerySpecification(any(HttpServletRequest.class)))
        .thenReturn(Specification.where(null));

    List<Bookmark> bookmark = bookmarkService.fetchBookmarks(httpServletRequest);

    assertEquals(2, bookmark.size());
    assertEquals("1", bookmark.get(0).getId());
  }

  @Test
  void fetchBookmarkById_shouldReturnBookmark() {
    Optional<Bookmark> bookmark = bookmarkService.fetchBookmarkById("1");

    assertTrue(bookmark.isPresent());
    assertEquals("1", bookmark.get().getId());
  }

  @Test
  void updateCompleteBookmark_ifBookmarkIdDoesNotExists_ReturnEmptyOptional() {
    Optional<Bookmark> bookmarkOptional =
        bookmarkService.updateCompleteBookmark("2", new Bookmark());

    assertTrue(bookmarkOptional.isEmpty());
  }

  @Test
  void updateCompleteBookmark_shouldUpdateAndReturnBookmark() {
    Bookmark updatedBookmark =
        new Bookmark(
            "1",
            "UpdatedTitle",
            "UpdatedDescription",
            "http://example.com/updated",
            Instant.now(),
            Instant.now());

    Optional<Bookmark> bookmarkOptional =
        bookmarkService.updateCompleteBookmark("1", updatedBookmark);

    assertTrue(bookmarkOptional.isPresent());
    assertEquals("UpdatedTitle", bookmarkOptional.get().getTitle());
  }

  @Test
  void partiallyUpdateBookmark_ifBookmarkIdDoesNotExists_ReturnEmptyOptional() {
    Optional<Bookmark> bookmarkOptional =
        bookmarkService.partiallyUpdateBookmark("2", new Bookmark());

    assertTrue(bookmarkOptional.isEmpty());
  }

  @Test
  void partiallyUpdateBookmark_shouldUpdateAndReturnBookmark() {
    Bookmark updatedBookmark =
        new Bookmark(
            "1",
            "UpdatedTitle",
            "UpdatedDescription",
            "http://example.com/updated",
            Instant.now(),
            Instant.now());

    Optional<Bookmark> bookmarkOptional =
        bookmarkService.partiallyUpdateBookmark("1", updatedBookmark);

    assertTrue(bookmarkOptional.isPresent());
    assertEquals("UpdatedTitle", bookmarkOptional.get().getTitle());
  }

  @Test
  void deleteBookmark_shouldDeleteBookmark() {
    bookmarkService.deleteBookmark("1");

    assertTrue(bookmarkService.fetchBookmarkById("1").isEmpty());
  }
}
