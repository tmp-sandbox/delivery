package tmpsandbox.microarch.ddd.delivery.core.domain;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
    properties = {
        "logging.level.root=DEBUG"
    }
)
@Testcontainers
public abstract class BaseIT {
    @Container
    protected static final GenericContainer<?> GEO = new GenericContainer<>("registry.gitlab.com/microarch-ru/ddd-in-practice/microservices/generic/geo:latest")
        .withExposedPorts(5004, 8084)
        .waitingFor(org.testcontainers.containers.wait.strategy.Wait.forListeningPort());

    @Container
    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15.3")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);

        registry.add("grpc.client.geo.address", () ->
            "static://" + GEO.getHost() + ":" + GEO.getMappedPort(5004)
        );
        registry.add("grpc.client.geo.negotiationType", () -> "PLAINTEXT");
    }
}
