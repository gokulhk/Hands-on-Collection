package com.example.restapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Builder
public class Folder {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String name;

  @ElementCollection
  @ToString.Exclude
  private List<String> bookmarkIds;

  @CreationTimestamp private Instant creationTimestamp;

  @UpdateTimestamp private Instant updatedTimestamp;
}
