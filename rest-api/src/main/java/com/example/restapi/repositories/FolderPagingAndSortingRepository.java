package com.example.restapi.repositories;

import com.example.restapi.entities.Folder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderPagingAndSortingRepository
    extends PagingAndSortingRepository<Folder, String> {
  Page<Folder> findByNameContainingIgnoreCase(Pageable pageable, String searchTerm);
}
