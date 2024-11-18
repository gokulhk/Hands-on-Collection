package com.example.rest_api.repositories;

import com.example.rest_api.entities.Bookmark;
import org.springframework.data.repository.CrudRepository;

public interface BookmarkRepository extends CrudRepository<Bookmark, String> {}
