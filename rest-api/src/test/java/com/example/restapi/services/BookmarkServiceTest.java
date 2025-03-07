package com.example.restapi.services;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.helpers.BookmarkHelper;
import com.example.restapi.repositories.BookmarkRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookmarkServiceTest {
  @Autowired BookmarkService bookmarkService;

  @MockBean BookmarkHelper bookmarkHelper;
  @MockBean private BookmarkRepository bookmarkRepository;
  @MockBean HttpServletRequest httpServletRequest;

  @Test
  void fetchBookmarks_shouldReturnListOfBookmarks() {
    when(bookmarkHelper.constructPaginationConfig(any(HttpServletRequest.class)))
        .thenReturn(PageRequest.of(0, 1));
    when(bookmarkHelper.constructQuerySpecification(any(HttpServletRequest.class)))
        .thenReturn(Specification.where(null));

    when(bookmarkRepository.findAll(any(), any(PageRequest.class)))
        .thenReturn((new PageImpl<>(getSampleBookmarks())));

    List<Bookmark> bookmark = bookmarkService.fetchBookmarks(httpServletRequest);

    verify(bookmarkRepository).findAll(any(), any(PageRequest.class));

    assertEquals(2, bookmark.size());
    assertEquals("1", bookmark.get(0).getId());
    assertEquals("2", bookmark.get(1).getId());
  }

    @Test
    void fetchBookmarkById_shouldReturnBookmark() {
        when(bookmarkRepository.findById("1"))
                .thenReturn(Optional.of(getSampleBookmarks().getFirst()));

        Optional<Bookmark> bookmark = bookmarkService.fetchBookmarkById("1");

        Mockito.verify(bookmarkRepository).findById("1");

        assertTrue(bookmark.isPresent());
        assertEquals("1", bookmark.get().getId());
    }

    @Test
    void addBookmark_shouldCreateAndReturnBookmark() {
        when(bookmarkRepository.save(any(Bookmark.class)))
                .thenReturn(getSampleBookmarks().getFirst());

        Bookmark bookmark = bookmarkService.addBookmark(new Bookmark());

        verify(bookmarkRepository).save(any(Bookmark.class));

        assertEquals("1", bookmark.getId());
    }

    @Test
    void updateCompleteBookmark_ifBookmarkIdDoesNotExists_ReturnEmptyOptional() {
        when(bookmarkRepository.findById("1")).thenReturn(Optional.empty());

        Optional<Bookmark> bookmarkOptional = bookmarkService.updateCompleteBookmark("1", new Bookmark());

        verify(bookmarkRepository).findById("1");

        assertTrue(bookmarkOptional.isEmpty());
    }
    @Test
    void updateCompleteBookmark_shouldUpdateAndReturnBookmark() {
        Bookmark existingBookmark = getSampleBookmarks().getFirst();
        Bookmark updatedBookmark =
                new Bookmark(
                        "1",
                        "UpdatedTitle",
                        "UpdatedDescription",
                        "http://example.com/updated",
                        Instant.now(),
                        Instant.now());

        when(bookmarkRepository.findById("1")).thenReturn(Optional.of(existingBookmark));
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(updatedBookmark);

        Optional<Bookmark> bookmarkOptional = bookmarkService.updateCompleteBookmark("1", new Bookmark());

        verify(bookmarkRepository).findById("1");
        verify(bookmarkRepository).save(any(Bookmark.class));


        assertTrue(bookmarkOptional.isPresent());
        assertEquals("UpdatedTitle", bookmarkOptional.get().getTitle());
    }

    @Test
    void partiallyUpdateBookmark_ifBookmarkIdDoesNotExists_ReturnEmptyOptional() {
        when(bookmarkRepository.findById("1")).thenReturn(Optional.empty());

        Optional<Bookmark> bookmarkOptional = bookmarkService.partiallyUpdateBookmark("1", new Bookmark());

        verify(bookmarkRepository).findById("1");

        assertTrue(bookmarkOptional.isEmpty());
    }

    @Test
    void partiallyUpdateBookmark_shouldUpdateAndReturnBookmark() {
        Bookmark existingBookmark = getSampleBookmarks().getFirst();
        Bookmark updatedBookmark =
                new Bookmark(
                        "1",
                        "UpdatedTitle",
                        "UpdatedDescription",
                        "http://example.com/updated",
                        Instant.now(),
                        Instant.now());

        when(bookmarkRepository.findById("1")).thenReturn(Optional.of(existingBookmark));
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(updatedBookmark);

        Optional<Bookmark> bookmarkOptional = bookmarkService.partiallyUpdateBookmark("1", new Bookmark());

        verify(bookmarkRepository).findById("1");
        verify(bookmarkRepository).save(any(Bookmark.class));


        assertTrue(bookmarkOptional.isPresent());
        assertEquals("UpdatedTitle", bookmarkOptional.get().getTitle());
    }

    @Test
    void deleteBookmark_shouldDeleteBookmark() {
        doNothing().when(bookmarkRepository).deleteById("1");

        bookmarkService.deleteBookmark("1");

        verify(bookmarkRepository).deleteById("1");
    }

    private List<Bookmark> getSampleBookmarks() {
        Bookmark bookmark =
                new Bookmark(
                        "1", "Title1", "Description1", "http://example.com/1", Instant.now(), Instant.now());
        Bookmark bookmark2 =
                new Bookmark(
                        "2", "Title2", "Description2", "http://example.com/2", Instant.now(), Instant.now());
        return Arrays.asList(bookmark, bookmark2);
    }
}
