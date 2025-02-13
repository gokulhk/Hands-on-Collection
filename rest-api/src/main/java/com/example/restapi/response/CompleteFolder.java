package com.example.restapi.response;

import com.example.restapi.entities.Bookmark;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class CompleteFolder {
  private String folderId;
  private List<Bookmark> bookmarks;
}
