package com.example.restapi.repositories;

import com.example.restapi.entities.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

@Repository
public interface BookmarkPagingAndSortingRepository
    extends PagingAndSortingRepository<Bookmark, String>, JpaSpecificationExecutor<Bookmark> {
  Page<Bookmark> findByTitleContainingIgnoreCase(Pageable pageable, String searchTerm);

  Page<Bookmark> findByUrlContainingIgnoreCase(Pageable pageable, String searchTerm);

  Page<Bookmark> findByDescriptionContainingIgnoreCase(Pageable pageable, String searchTerm);
}
