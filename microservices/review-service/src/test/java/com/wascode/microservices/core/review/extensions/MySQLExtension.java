package com.wascode.microservices.core.review.extensions;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MySQLContainer;

public class MySQLExtension implements BeforeAllCallback {
  private final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:5.7.32");

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    mySQLContainer.start();

    System.setProperty("spring.datasource.url", mySQLContainer.getJdbcUrl());
    System.setProperty("spring.datasource.username", mySQLContainer.getUsername());
    System.setProperty("spring.datasource.password", mySQLContainer.getPassword());
  }
}
