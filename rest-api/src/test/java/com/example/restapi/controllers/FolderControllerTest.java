package com.example.restapi.controllers;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.entities.Folder;
import com.example.restapi.response.CompleteFolder;
import com.example.restapi.services.FolderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FolderController.class)
class FolderControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private FolderService folderService;

  @Test
  void fetchFolders_shouldReturnListOfFolders() throws Exception {
    when(folderService.fetchFolders()).thenReturn(getSampleFolders());

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/folders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("FolderName_1"))
        .andExpect(jsonPath("$[1].name").value("FolderName_2"));
  }

  @Test
  void fetchFolderById_shouldReturnCompleteFolder() throws Exception {
    CompleteFolder completeFolder =
        CompleteFolder.builder()
            .folderId("1234")
            .bookmarks(
                List.of(
                    new Bookmark(
                        "1",
                        "Title1",
                        "Description1",
                        "http://example.com/1",
                        Instant.now(),
                        Instant.now())))
            .build();

    when(folderService.fetchFolderById("1")).thenReturn(Optional.of(completeFolder));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/folders/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bookmarks[0].title").value("Title1"));
  }

  @Test
  void addFolder_shouldCreateAndReturnFolder() throws Exception {
    when(folderService.addFolder(any(Folder.class))).thenReturn(getSampleFolders().getFirst());

    String requestPayload =
        """
                {
                    "name": "FolderName",
                    "bookmarkIds": [ "bookmarkId_4" ]
                }
                """;

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/folders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("FolderName_1"));
  }

  @Test
  void updateCompleteFolder_shouldUpdateAndReturnFolder() throws Exception {
    Folder updatedFolder =
        new Folder("1", "UpdatedName", List.of("bookmarkId_4"), Instant.now(), Instant.now());

    String requestPayload =
        """
                {
                    "name": "UpdatedName",
                    "bookmarkIds": ["bookmarkId_4"]
                }
                """;

    when(folderService.updateCompleteFolder(eq("1"), any(Folder.class)))
        .thenReturn(Optional.of(updatedFolder));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/v1/folders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("UpdatedName"));
  }

  @Test
  void partiallyUpdateFolder_shouldUpdateAndReturnFolder() throws Exception {
    Folder updatedFolder =
        new Folder("1", "UpdatedName", List.of("bookmarkId_1"), Instant.now(), Instant.now());

    String requestPayload =
        """
                {
                    "name": "UpdatedName"
                }
                """;

    when(folderService.partiallyUpdateFolder(eq("1"), any(Folder.class)))
        .thenReturn(Optional.of(updatedFolder));

    mockMvc
        .perform(
            MockMvcRequestBuilders.patch("/api/v1/folders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("UpdatedName"));
  }

  @Test
  void deleteFolder_shouldDeleteFolder() throws Exception {
    Mockito.doNothing().when(folderService).deleteFolder("1");

    mockMvc
        .perform(MockMvcRequestBuilders.delete("/api/v1/folders/1"))
        .andExpect(status().isNoContent());
  }

  private List<Folder> getSampleFolders() {
    Folder folderOne =
        new Folder("1", "FolderName_1", List.of("bookmarkId_1"), Instant.now(), Instant.now());
    Folder folderTwo =
        new Folder("2", "FolderName_2", List.of("bookmarkId_2"), Instant.now(), Instant.now());
    return Arrays.asList(folderOne, folderTwo);
  }
}
