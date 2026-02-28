package tmpsandbox.microarch.ddd.delivery.adapters.in.kafka;

import com.google.protobuf.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import queues.basket.BasketEventsProto;
import tmpsandbox.microarch.ddd.delivery.core.application.command.order.CreateNewOrderCommand;
import tmpsandbox.microarch.ddd.delivery.core.application.command.order.CreateNewOrderCommandHandler;

import java.util.UUID;

import static tmpsandbox.microarch.ddd.delivery.common.util.ProtobufMessageParser.parseEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketConsumer {
    private final CreateNewOrderCommandHandler handler;

    @KafkaListener(topics = "baskets.events")
    public void listen(byte[] message) {
        log.info("Received message {}", message);

        var event = parseEvent(message, BasketEventsProto.BasketConfirmedIntegrationEvent.parser());
        var address = event.getAddress();

        var resultCreateNewCommand = CreateNewOrderCommand.create(
            UUID.fromString(event.getBasketId()),
            address.getCountry(),
            address.getCity(),
            address.getStreet(),
            address.getHouse(),
            address.getApartment(),
            event.getVolume()
        );

        if (resultCreateNewCommand.isFailure()) {
            log.error("Failed create CreateNewOrderCommand: {}", resultCreateNewCommand.getError());
            return;
        }

        var newOrderCommand = resultCreateNewCommand.getValue();
        log.info("Create command {}", newOrderCommand);

        handler.handle(newOrderCommand);
    }
}
