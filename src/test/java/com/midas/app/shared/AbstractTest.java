package com.midas.app.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DirtiesContext
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration
@ActiveProfiles("test")
public abstract class AbstractTest {
  @Container
  public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:14-alpine");

  public static ComposeContainer environment =
      new ComposeContainer(new File("src/test/resources/compose-test.yml"))
          .withExposedService("temporal", 7233, Wait.forListeningPort());

  @DynamicPropertySource
  protected static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    environment.start();
  }

  @Autowired protected ObjectMapper objectMapper;

  @Autowired protected MockMvc mvc;
}
