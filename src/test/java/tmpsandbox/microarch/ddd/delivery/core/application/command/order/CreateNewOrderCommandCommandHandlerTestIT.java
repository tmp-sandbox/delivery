package tmpsandbox.microarch.ddd.delivery.core.application.command.order;

import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import queues.order.OrderEventsProto;
import tmpsandbox.microarch.ddd.delivery.adapters.out.postgres.OrderJpaRepository;
import tmpsandbox.microarch.ddd.delivery.BaseIT;
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

    @BeforeEach
    public void setup() {
        orderRepository.deleteAll();
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