package com.example.usersregistrationservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table("users")
public class User {

  @PrimaryKey
  private UserKey key;

  private String password;
  private List<String> roles;

}
