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
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bookmarks")
@Log
public class BookmarkController {

  @Autowired BookmarkRepository bookmarkRepository;

  @GetMapping
  ResponseEntity<List<Bookmark>> fetchBookmarks() {
    List<Bookmark> bookmarkList = new ArrayList<>();
    bookmarkRepository.findAll().iterator().forEachRemaining(bookmarkList::add);
    log.info("fetched bookmarks: " + bookmarkList);
    return ResponseEntity.ok(bookmarkList);
  }

  @GetMapping("/{bookmarkId}")
  ResponseEntity<Bookmark> fetchBookmarkById(@PathVariable String bookmarkId) {
    log.info("fetching bookmark: " + bookmarkId);
    return bookmarkRepository
        .findById(bookmarkId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
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
  ResponseEntity<Bookmark> updateCompleteBookmark(
      @PathVariable String bookmarkId, @RequestBody Bookmark bookmarkPayload) {
    log.info("payload received: " + bookmarkPayload);

    Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(bookmarkId);

    if (bookmarkOptional.isEmpty()) return ResponseEntity.notFound().build();

    Bookmark bookmark = bookmarkOptional.get();
    bookmark.setUrl(bookmarkPayload.getUrl());
    bookmark.setTitle(bookmarkPayload.getTitle());
    bookmark.setDescription(bookmarkPayload.getDescription());

    log.info("updating bookmark: " + bookmarkId);
    return ResponseEntity.ok().body(bookmarkRepository.save(bookmark));
  }

  @PatchMapping(
      value = "/{bookmarkId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Bookmark> partiallyUpdateBookmark(
      @PathVariable String bookmarkId, @RequestBody Bookmark bookmarkPayload) {
    log.info("payload received: " + bookmarkPayload);

    Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(bookmarkId);

    if (bookmarkOptional.isEmpty()) return ResponseEntity.notFound().build();

    Bookmark bookmark = bookmarkOptional.get();
    Optional.ofNullable(bookmarkPayload.getUrl()).ifPresent(bookmark::setUrl);
    Optional.ofNullable(bookmarkPayload.getTitle()).ifPresent(bookmark::setTitle);
    Optional.ofNullable(bookmarkPayload.getDescription()).ifPresent(bookmark::setDescription);

    log.info("patching bookmark: " + bookmarkId);
    return ResponseEntity.ok().body(bookmarkRepository.save(bookmark));
  }

  @DeleteMapping("/{bookmarkId}")
  ResponseEntity<String> deleteBookmark(@PathVariable String bookmarkId) {
    log.info("deleting bookmark: " + bookmarkId);
    bookmarkRepository.deleteById(bookmarkId);
    return ResponseEntity.noContent().build();
  }
}
