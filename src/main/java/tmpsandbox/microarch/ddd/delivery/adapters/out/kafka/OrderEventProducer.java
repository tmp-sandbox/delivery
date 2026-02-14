package tmpsandbox.microarch.ddd.delivery.adapters.out.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import queues.order.OrderEventsProto;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.event.OrderCompletedEvent;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.event.OrderCreatedEvent;
import tmpsandbox.microarch.ddd.delivery.core.port.OrderClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer implements OrderClient {
    @Value("${kafka.orders.events-topic}")
    private String topic;

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Override
    public void orderCreated(OrderCreatedEvent event) {
        var integrationEvent = OrderEventsProto.OrderCreatedIntegrationEvent.newBuilder()
            .setOrderId(event.getOrderId().toString())
            .build();

        kafkaTemplate.send(topic, event.getOrderId().toString(), integrationEvent.toByteArray());
        log.debug("Send kafka message to: {}, payload: {}", topic, integrationEvent);
    }

    @Override
    public void orderCompleted(OrderCompletedEvent event) {
        var integrationEvent = OrderEventsProto.OrderCompletedIntegrationEvent.newBuilder()
            .setOrderId(event.getOrderId().toString())
            .setCourierId(event.getCourierId().toString())
            .build();

        kafkaTemplate.send(topic, event.getOrderId().toString(), integrationEvent.toByteArray());
        log.debug("Send kafka message to: {}, payload: {}", topic, integrationEvent);
    }
}
