package tmpsandbox.microarch.ddd.delivery.core.application.command.order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import queues.order.OrderEventsProto;
import tmpsandbox.microarch.ddd.delivery.BaseIT;
import tmpsandbox.microarch.ddd.delivery.adapters.out.postgres.OrderJpaRepository;
import tmpsandbox.microarch.ddd.delivery.adapters.out.postgres.outbox.OutboxJpaRepository;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;

class CreateNewOrderCommandCommandHandlerTestIT extends BaseIT {
    @Autowired
    private CreateNewOrderCommandHandler handler;
    @Autowired
    private OrderJpaRepository orderRepository;
    @Autowired
    private Consumer<String, byte[]> ordersEventsConsumer;
    @Autowired
    private OutboxJpaRepository outboxJpaRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void setup() {
        orderRepository.deleteAll();
        outboxJpaRepository.deleteAll();
    }

    @Test
    void shouldCreateNewOrder_whenCallHandle() {
        // Given:
        var command = new CreateNewOrderCommand(UUID.randomUUID(), "street", 5);

        // When:
        var result = handler.handle(command);

        // Then:
        Order createdOrder = orderRepository.findAll().getFirst();
        var consumerRecord = awaitOrderEvent();

        assertAll(
            () -> assertThat(result.isFailure()).isFalse(),
            () -> assertThat(createdOrder).isNotNull(),
            () -> assertThat(createdOrder.getVolume().getValue()).isEqualTo(5),
            () -> assertThat(createdOrder.getLocation()).isNotNull(),
            () -> assertThat(consumerRecord.getOrderId()).isEqualTo(createdOrder.getId().toString())
        );
    }

    @Test
    void shouldCreateNewOrderAndOutboxInOneTransaction_whenHandle() {
        // Given:
        var command = new CreateNewOrderCommand(UUID.randomUUID(), "street", 5);

        // When:
        var result = handler.handle(command);

        // Then:
        String outbox = getXminByTable("outbox");
        String orders = getXminByTable("orders");

        assertThat(outbox).isEqualTo(orders);
    }

    private String getXminByTable(String tableName) {
        return (String) em.createNativeQuery("""
                select xmin::text
                from %s
                limit 1
                """.formatted(tableName))
            .getSingleResult();
    }

    @SneakyThrows
    private OrderEventsProto.OrderCreatedIntegrationEvent awaitOrderEvent() {
        ConsumerRecord<String, byte[]> until = await()
            .pollInterval(Duration.ofMillis(500))
            .atMost(Duration.ofSeconds(5))
            .until(() -> {
                var records = ordersEventsConsumer.poll(Duration.ofMillis(500));

                return records.iterator().hasNext()
                    ? records.iterator().next()
                    : null;
            }, Objects::nonNull);

        return OrderEventsProto.OrderCreatedIntegrationEvent.parseFrom(until.value());
    }
}