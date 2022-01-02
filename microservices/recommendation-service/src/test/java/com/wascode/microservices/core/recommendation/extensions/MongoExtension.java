package com.wascode.microservices.core.recommendation.extensions;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MongoDBContainer;

public class MongoExtension implements BeforeAllCallback {
  private final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    mongoDBContainer.start();

    String DB_HOST = mongoDBContainer.getContainerIpAddress();
    Integer DB_PORT = mongoDBContainer.getMappedPort(27017);
    String DB_NAME = "test";

    System.setProperty("spring.data.mongodb.host", DB_HOST);
    System.setProperty("spring.data.mongodb.port", String.valueOf(DB_PORT));
    System.setProperty("spring.data.mongodb.database", DB_NAME);
  }
}
