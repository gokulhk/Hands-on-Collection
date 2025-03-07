package com.example.restapi.services;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.helpers.BookmarkHelper;
import com.example.restapi.repositories.BookmarkRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Log
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;

  private final BookmarkHelper bookmarkHelper;

  public List<Bookmark> fetchBookmarks(HttpServletRequest request) {
    List<Bookmark> bookmarks = new ArrayList<>();

    bookmarkRepository
        .findAll(
            bookmarkHelper.constructQuerySpecification(request),
            bookmarkHelper.constructPaginationConfig(request))
        .iterator()
        .forEachRemaining(bookmarks::add);

    log.info("fetched bookmarks: " + bookmarks);
    return bookmarks;
  }

    public Optional<Bookmark> fetchBookmarkById(String bookmarkId) {
        log.info("fetching bookmark id: " + bookmarkId);
        return bookmarkRepository.findById(bookmarkId);
    }

  public List<Bookmark> fetchBookmarkByIds(List<String> bookmarkIds) {
    log.info("fetching bookmark ids: " + bookmarkIds);
    return (List<Bookmark>) bookmarkRepository.findAllById(bookmarkIds);
  }

    public Bookmark addBookmark(Bookmark bookmarkPayload) {
        Bookmark addedBookmark = bookmarkRepository.save(bookmarkPayload);
        log.info("added bookmark: " + addedBookmark);
        return addedBookmark;
    }

    public Optional<Bookmark> updateCompleteBookmark(
            String bookmarkId, Bookmark bookmarkPayload) {
        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(bookmarkId);

        if (bookmarkOptional.isEmpty()) return bookmarkOptional;

        Bookmark bookmark = bookmarkOptional.get();
        bookmark.setUrl(bookmarkPayload.getUrl());
        bookmark.setTitle(bookmarkPayload.getTitle());
        bookmark.setDescription(bookmarkPayload.getDescription());

        log.info("updating bookmark: " + bookmarkId);
        return Optional.of(bookmarkRepository.save(bookmark));
    }

    public Optional<Bookmark> partiallyUpdateBookmark(
            @PathVariable String bookmarkId, @RequestBody Bookmark bookmarkPayload) {
        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(bookmarkId);

        if (bookmarkOptional.isEmpty()) return bookmarkOptional;

        Bookmark bookmark = bookmarkOptional.get();
        Optional.ofNullable(bookmarkPayload.getUrl()).ifPresent(bookmark::setUrl);
        Optional.ofNullable(bookmarkPayload.getTitle()).ifPresent(bookmark::setTitle);
        Optional.ofNullable(bookmarkPayload.getDescription()).ifPresent(bookmark::setDescription);

        log.info("patching bookmark: " + bookmarkId);
        return Optional.of(bookmarkRepository.save(bookmark));
    }

    public void deleteBookmark(String bookmarkId) {
        log.info("deleting bookmark: " + bookmarkId);
        bookmarkRepository.deleteById(bookmarkId);
    }

}
