package com.example.usersregistrationservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

import static org.springframework.data.cassandra.core.cql.Ordering.DESCENDING;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@PrimaryKeyClass
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserKey {

  @PrimaryKeyColumn(name = "id", ordinal = 1, ordering = DESCENDING)
  private UUID id;

  @PrimaryKeyColumn(name = "username", type = PARTITIONED)
  private String username;

}
