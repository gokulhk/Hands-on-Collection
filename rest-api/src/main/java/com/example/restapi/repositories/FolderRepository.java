package com.example.restapi.repositories;

import com.example.restapi.entities.Bookmark;
import com.example.restapi.entities.Folder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository
    extends CrudRepository<Folder, String>,
        PagingAndSortingRepository<Folder, String>,
        JpaSpecificationExecutor<Folder> {
  Page<Folder> findByNameContainingIgnoreCase(Pageable pageable, String searchTerm);
}
