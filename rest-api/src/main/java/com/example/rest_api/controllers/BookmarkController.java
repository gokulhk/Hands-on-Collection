package com.example.rest_api.controllers;

import com.example.rest_api.entities.Bookmark;
import com.example.rest_api.repositories.BookmarkRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/bookmarks")
@Log
public class BookmarkController {

  @Autowired BookmarkRepository bookmarkRepository;

  @GetMapping(value = {"", "/{bookmarkId}"})
  ResponseEntity<List<Bookmark>> fetchBookmarks(@PathVariable(required = false) String bookmarkId) {
    List<Bookmark> bookmarkList = new ArrayList<>();

    if (Objects.nonNull(bookmarkId)) {
      log.info("fetching bookmark: " + bookmarkId);
      bookmarkRepository.findById(bookmarkId).ifPresent(bookmarkList::add);
    } else {
      bookmarkRepository.findAll().iterator().forEachRemaining(bookmarkList::add);
    }

    log.info("fetched bookmarks: " + bookmarkList);
    return ResponseEntity.ok(bookmarkList);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Bookmark> addBookmark(@RequestBody Bookmark bookmarkPayload) {
    log.info("payload received: " + bookmarkPayload);

    Bookmark addedBookmark = bookmarkRepository.save(bookmarkPayload);
    log.info("added bookmark: " + addedBookmark);

    return ResponseEntity.created(URI.create("/" + addedBookmark.getId())).body(addedBookmark);
  }

  @PutMapping(
      value = "/{bookmarkId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> updateCompleteBookmark(@PathVariable String bookmarkId) {
    log.info("updating bookmark: " + bookmarkId);
    return ResponseEntity.ok("Updated bookmark id " + bookmarkId + ". Update bookmark info.");
  }

  @PatchMapping(
      value = "/{bookmarkId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> partiallyUpdateBookmark(@PathVariable String bookmarkId) {
    log.info("patching bookmark: " + bookmarkId);
    return ResponseEntity.ok("Updated bookmark id " + bookmarkId + ". Update bookmark info.");
  }

  @DeleteMapping("/{bookmarkId}")
  ResponseEntity<String> deleteBookmark(@PathVariable String bookmarkId) {
    log.info("deleting bookmark: " + bookmarkId);
    bookmarkRepository.deleteById(bookmarkId);
    return ResponseEntity.noContent().build();
  }
}
