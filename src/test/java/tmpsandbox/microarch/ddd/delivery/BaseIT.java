package tmpsandbox.microarch.ddd.delivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.wait.strategy.Wait.forListeningPort;

@SpringBootTest(
    properties = {
//        "logging.level.root=DEBUG"
    }
)
@Testcontainers
@Import(KafkaTestConfiguration.class)
@Slf4j
public abstract class BaseIT {
    private static final DockerImageName GEO_NAME = DockerImageName.parse("registry.gitlab.com/microarch-ru/ddd-in-practice/microservices/generic/geo:latest");
    private static final DockerImageName POSTGRES_NAME = DockerImageName.parse("postgres:15.3");
    private static final DockerImageName KAFKA_NAME = DockerImageName.parse("apache/kafka:3.7.0");

    @Container
    protected static final GenericContainer<?> GEO = new GenericContainer<>(GEO_NAME)
        .withExposedPorts(5004, 8084)
        .waitingFor(forListeningPort());

    @Container
    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(POSTGRES_NAME)
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @Container
    protected static final KafkaContainer KAFKA = new KafkaContainer(KAFKA_NAME);

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);

        registry.add("grpc.client.geo.address", () ->
            "static://" + GEO.getHost() + ":" + GEO.getMappedPort(5004)
        );
        registry.add("grpc.client.geo.negotiationType", () -> "PLAINTEXT");

        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
        registry.add("kafka.orders.events-topic", () -> "orders.events");
        registry.add("schedule.order.move-delay", () -> "500");
        registry.add("schedule.order.dispatch-delay", () -> "5000");
    }
}
