package com.example.restapi.repositories;

import com.example.restapi.entities.Folder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends CrudRepository<Folder, String> {}
