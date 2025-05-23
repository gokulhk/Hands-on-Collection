package com.example.restapi.controllers;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.services.BookmarkService;
import com.example.restapi.util.CommonUtils;
import com.example.restapi.validators.bookmark.BookmarkPayloadValidator;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
@Log
public class BookmarkController {

  private final BookmarkService bookmarkService;

  private final BookmarkPayloadValidator bookmarkPayloadValidator;

  @GetMapping
  ResponseEntity<List<Bookmark>> fetchBookmarks(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String description,
      @RequestParam(required = false) LocalDate fromDate,
      @RequestParam(required = false) LocalDate toDate,
      @PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
    return ResponseEntity.ok(
        bookmarkService.fetchBookmarks(title, description, fromDate, toDate, pageable));
  }

  @GetMapping("/{bookmarkId}")
  ResponseEntity<Bookmark> fetchBookmarkById(@PathVariable String bookmarkId) {
    log.info("fetching bookmark: " + bookmarkId);
    return bookmarkService
        .fetchBookmarkById(bookmarkId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  ResponseEntity<?> addBookmark(
      @RequestBody Bookmark bookmarkPayload, BindingResult validationResult) {
    log.info("payload received: " + bookmarkPayload);

    bookmarkPayloadValidator.validate(bookmarkPayload, validationResult);
    if (validationResult.hasErrors()) {
      return ResponseEntity.badRequest()
          .body(CommonUtils.constructValidationErrorResponse(validationResult));
    }

    Bookmark bookmark = bookmarkService.addBookmark(bookmarkPayload);
    return ResponseEntity.created(URI.create("/" + bookmark.getId())).body(bookmark);
  }

  @PutMapping(value = "/{bookmarkId}")
  ResponseEntity<?> updateCompleteBookmark(
      @PathVariable String bookmarkId,
      @RequestBody Bookmark bookmarkPayload,
      BindingResult validationResult) {
    log.info("payload received: " + bookmarkPayload);

    bookmarkPayloadValidator.validate(bookmarkPayload, validationResult);
    if (validationResult.hasErrors()) {
      return ResponseEntity.badRequest()
          .body(CommonUtils.constructValidationErrorResponse(validationResult));
    }

    return bookmarkService
        .updateCompleteBookmark(bookmarkId, bookmarkPayload)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PatchMapping(value = "/{bookmarkId}")
  ResponseEntity<Bookmark> partiallyUpdateBookmark(
      @PathVariable String bookmarkId, @RequestBody Bookmark bookmarkPayload) {
    log.info("payload received: " + bookmarkPayload);
    return bookmarkService
        .partiallyUpdateBookmark(bookmarkId, bookmarkPayload)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{bookmarkId}")
  ResponseEntity<String> deleteBookmark(@PathVariable String bookmarkId) {
    bookmarkService.deleteBookmark(bookmarkId);
    return ResponseEntity.noContent().build();
  }
}
