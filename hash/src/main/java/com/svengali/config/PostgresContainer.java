package com.svengali.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:13");

    private static PostgreSQLContainer<?> postgresContainer;

    static {
        Dotenv dotenv = Dotenv.configure()
                .directory("../")
                .load();

        postgresContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
                .withDatabaseName(("POSTGRES_HASH_DB"))
                .withUsername(("POSTGRES_HASH_USER"))
                .withPassword(("POSTGRES_HASH_PASSWORD"));
        postgresContainer.start();
    }


    @Override
    public void initialize(ConfigurableApplicationContext context) {
        TestPropertyValues.of(
                "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgresContainer.getUsername(),
                "spring.datasource.password=" + postgresContainer.getPassword(),
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.jpa.show-sql=true",
                "spring.jpa.properties.hibernate.format_sql=true"
        ).applyTo(context.getEnvironment());
    }

    public static PostgreSQLContainer<?> getPostgresContainer() {
        return postgresContainer;
    }
}
