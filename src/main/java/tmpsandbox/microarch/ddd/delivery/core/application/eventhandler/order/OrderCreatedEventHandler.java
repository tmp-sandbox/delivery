package tmpsandbox.microarch.ddd.delivery.core.application.eventhandler.order;

import libs.ddd.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.event.OrderCreatedEvent;
import tmpsandbox.microarch.ddd.delivery.core.port.OrderClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventHandler implements EventHandler<OrderCreatedEvent> {
    private final OrderClient orderClient;

    @Override
    @EventListener
    public void handle(OrderCreatedEvent event) {
        log.debug("Start handle order created event: {}", event);
        orderClient.orderCreated(event);
    }
}
