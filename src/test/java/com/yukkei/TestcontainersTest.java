package com.yukkei;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestcontainersTest extends AbstractTestcontainers{

    @Test
    void canStartPostgresDB() {
        assertThat(postgresDBContainer.isRunning()).isTrue();
        assertThat(postgresDBContainer.isCreated()).isTrue();
    }


}
