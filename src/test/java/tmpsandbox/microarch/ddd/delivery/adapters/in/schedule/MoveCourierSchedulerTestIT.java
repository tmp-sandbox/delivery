package tmpsandbox.microarch.ddd.delivery.adapters.in.schedule;

import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import queues.order.OrderEventsProto;
import tmpsandbox.microarch.ddd.delivery.BaseIT;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Name;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Speed;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.OrderStatus;
import tmpsandbox.microarch.ddd.delivery.core.port.CourierRepository;
import tmpsandbox.microarch.ddd.delivery.core.port.OrderRepository;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveCourierSchedulerTestIT extends BaseIT {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private Consumer<String, byte[]> ordersEventsConsumer;

    @Test
    public void shouldCorrectHandleMove_whenCourierMovedToOrder() {
        // Given:
        var courier = Courier.create(Name.create("Courier").getValue(), Speed.create(5).getValue(), Location.create(1, 1).getValue()).getValue();
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(1).getValue()).getValue();

        courier.takeOrder(order);
        order.assign(courier);

        courierRepository.save(courier);
        orderRepository.save(order);

        // When:
        var orderCompletedIntegrationEvent = awaitOrderEvent();

        // Then:
        Order orderCompleted = orderRepository.findById(order.getId()).get();

        assertAll(
            () ->  assertThat(orderCompletedIntegrationEvent.getOrderId()).isEqualTo(order.getId().toString()),
            () ->  assertThat(orderCompletedIntegrationEvent.getCourierId()).isEqualTo(courier.getId().toString()),
            () ->  assertThat(orderCompleted.getStatus()).isEqualTo(OrderStatus.COMPLETED)
        );
    }

    @SneakyThrows
    private OrderEventsProto.OrderCompletedIntegrationEvent awaitOrderEvent() {
        ConsumerRecord<String, byte[]> until = await()
            .pollInterval(Duration.ofMillis(500))
            .atMost(Duration.ofSeconds(5))
            .until(() -> {
                var records = ordersEventsConsumer.poll(Duration.ofMillis(500));

                return records.iterator().hasNext()
                    ? records.iterator().next()
                    : null;
            }, Objects::nonNull);

        return OrderEventsProto.OrderCompletedIntegrationEvent.parseFrom(until.value());
    }
}