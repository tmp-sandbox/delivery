package tmpsandbox.microarch.ddd.delivery.adapters.in.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.UnknownFieldSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import queues.basket.BasketEventsProto;
import tmpsandbox.microarch.ddd.delivery.core.application.command.order.CreateNewOrderCommand;
import tmpsandbox.microarch.ddd.delivery.core.application.command.order.CreateNewOrderCommandHandler;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketConsumer {
    private final CreateNewOrderCommandHandler handler;

    @KafkaListener(topics = "baskets.events")
    public void listen(byte[] message) {
        log.info("Received message {}", message);
        CreateNewOrderCommand newOrder = createNewOrder(message);
        log.info("Create command {}", newOrder);

        handler.handle(newOrder);
    }

    private CreateNewOrderCommand createNewOrder(byte[] message) {
        try {
            var event = BasketEventsProto.BasketConfirmedIntegrationEvent.parseFrom(message);
            log.info("Received event {}", event);

            String street = String.format("%s, %s, %s, %s, %s",
                event.getAddress().getCountry(),
                event.getAddress().getCountry(),
                event.getAddress().getCountry(),
                event.getAddress().getCountry(),
                event.getAddress().getCountry()
            );

            return new CreateNewOrderCommand(UUID.fromString(event.getBasketId()), street, event.getVolume());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse protobuf message", e);
        }
    }
}
