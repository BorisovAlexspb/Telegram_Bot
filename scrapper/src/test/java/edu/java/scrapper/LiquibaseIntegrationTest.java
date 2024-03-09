package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.Assert.assertTrue;

@Testcontainers
public class LiquibaseIntegrationTest extends IntegrationTest {
    @Test
    public void migrationTest() {
        assertTrue(POSTGRES.isRunning());
    }
}
