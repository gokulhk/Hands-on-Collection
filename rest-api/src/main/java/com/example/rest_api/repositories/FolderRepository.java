package com.example.rest_api.repositories;

import com.example.rest_api.entities.Folder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends CrudRepository<Folder, String> {}
