package com.example.restapi.controllers;

import com.example.restapi.services.BookmarkService;
import com.example.restapi.validators.bookmark.BookmarkPayloadValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.example.restapi.entities.Bookmark;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookmarkController.class)
class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookmarkService bookmarkService;

    @MockBean
    private BookmarkPayloadValidator bookmarkPayloadValidator;

  @MockBean HttpServletRequest httpServletRequest;

    @Test
    void fetchBookmarks_shouldReturnListOfBookmarks() throws Exception {
        when(bookmarkService.fetchBookmarks(httpServletRequest)).thenReturn(getSampleBookmarks());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookmarks")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value("1")).andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    void fetchBookmarkById_shouldReturnBookmark() throws Exception {
        when(bookmarkService.fetchBookmarkById("1")).thenReturn(Optional.of(getSampleBookmarks().getFirst()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookmarks/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void addBookmark_shouldCreateAndReturnBookmark() throws Exception {
        when(bookmarkService.addBookmark(any(Bookmark.class))).thenReturn(getSampleBookmarks().getFirst());

        String requestPayload = """
                {
                    "id": "1",
                    "title": "Title1",
                    "description": "Description1",
                    "url": "http://example.com/1"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookmarks").contentType(MediaType.APPLICATION_JSON).content(requestPayload)).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void updateCompleteBookmark_shouldUpdateAndReturnBookmark() throws Exception {
        Bookmark updatedBookmark = new Bookmark("1", "UpdatedTitle", "UpdatedDescription", "http://example.com/updated", Instant.now(), Instant.now());

        String requestPayload = """
                {
                    "id": "1",
                    "title": "UpdatedTitle",
                    "description": "UpdatedDescription",
                    "url": "http://example.com/updated"
                }
                """;

        when(bookmarkService.updateCompleteBookmark(eq("1"), any(Bookmark.class))).thenReturn(Optional.of(updatedBookmark));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookmarks/1").contentType(MediaType.APPLICATION_JSON).content(requestPayload)).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("UpdatedTitle"));
    }

    @Test
    void partiallyUpdateBookmark_shouldUpdateAndReturnBookmark() throws Exception {
        Bookmark updatedBookmark = new Bookmark("1", "UpdatedTitle", "Description1", "http://example.com/1", Instant.now(), Instant.now());

        String requestPayload = """
                {
                    "title": "UpdatedTitle"
                }
                """;

        when(bookmarkService.partiallyUpdateBookmark(eq("1"), any(Bookmark.class))).thenReturn(Optional.of(updatedBookmark));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/bookmarks/1").contentType(MediaType.APPLICATION_JSON).content(requestPayload)).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("UpdatedTitle"));
    }

    @Test
    void deleteBookmark_shouldDeleteBookmark() throws Exception {
        Mockito.doNothing().when(bookmarkService).deleteBookmark("1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/bookmarks/1")).andExpect(status().isNoContent());
    }

    private List<Bookmark> getSampleBookmarks() {
        Bookmark bookmark = new Bookmark("1", "Title1", "Description1", "http://example.com/1", Instant.now(), Instant.now());
        Bookmark bookmark2 = new Bookmark("2", "Title2", "Description2", "http://example.com/2", Instant.now(), Instant.now());
        return Arrays.asList(bookmark, bookmark2);
    }
}
