package com.example.restapi.controllers;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.services.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
@Log
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    ResponseEntity<List<Bookmark>> fetchBookmarks() {
        return ResponseEntity.ok(bookmarkService.fetchBookmarks());
    }

    @GetMapping("/{bookmarkId}")
    ResponseEntity<Bookmark> fetchBookmarkById(@PathVariable String bookmarkId) {
        log.info("fetching bookmark: " + bookmarkId);
        return bookmarkService.fetchBookmarkById(bookmarkId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Bookmark> addBookmark(@RequestBody Bookmark bookmarkPayload) {
        log.info("payload received: " + bookmarkPayload);
        Bookmark bookmark = bookmarkService.addBookmark(bookmarkPayload);
        return ResponseEntity.created(URI.create("/" + bookmark.getId())).body(bookmark);
    }

    @PutMapping(value = "/{bookmarkId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Bookmark> updateCompleteBookmark(@PathVariable String bookmarkId,
                                                    @RequestBody Bookmark bookmarkPayload) {
        log.info("payload received: " + bookmarkPayload);
        return bookmarkService.updateCompleteBookmark(bookmarkId, bookmarkPayload)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(
            value = "/{bookmarkId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Bookmark> partiallyUpdateBookmark(@PathVariable String bookmarkId,
                                                     @RequestBody Bookmark bookmarkPayload) {
        log.info("payload received: " + bookmarkPayload);
        return bookmarkService.partiallyUpdateBookmark(bookmarkId, bookmarkPayload)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{bookmarkId}")
    ResponseEntity<String> deleteBookmark(@PathVariable String bookmarkId) {
        bookmarkService.deleteBookmark(bookmarkId);
        return ResponseEntity.noContent().build();
    }
}
