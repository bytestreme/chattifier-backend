package com.example.userssearchservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {

  @Id
  private String id;

  private String username;

  @JsonIgnore
  private String password;

  @JsonIgnore
  private List<String> roles;

}
