package com.example.messagedeliveryservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import java.util.List;

@Configuration
@EnableReactiveCassandraRepositories
public class CassandraConfig extends AbstractReactiveCassandraConfiguration {

  @Value("${spring.data.cassandra.keyspace-name}")
  private String keySpace;

  @Value("${spring.data.cassandra.port}")
  private Integer port;

  @Value("${spring.data.cassandra.local-datacenter}")
  private String localDatacenter;

  @Value("${spring.data.cassandra.base-packages}")
  private String basePackages;

  @Override
  public String[] getEntityBasePackages() {
    return new String[]{basePackages};
  }

  @Override
  protected String getLocalDataCenter() {
    return localDatacenter;
  }

  @Override
  protected String getKeyspaceName() {
    return keySpace;
  }

  @Override
  protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
    return List.of(CreateKeyspaceSpecification.createKeyspace(keySpace)
        .ifNotExists(true));
  }

  @Override
  protected int getPort() {
    return port;
  }

  @Override
  public SchemaAction getSchemaAction() {
    return SchemaAction.RECREATE_DROP_UNUSED;
  }

  @Override
  protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
    return List.of(DropKeyspaceSpecification.dropKeyspace(keySpace));
  }


}