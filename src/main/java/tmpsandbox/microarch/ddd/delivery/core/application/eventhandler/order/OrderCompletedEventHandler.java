package tmpsandbox.microarch.ddd.delivery.core.application.eventhandler.order;

import libs.ddd.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.event.OrderCompletedEvent;
import tmpsandbox.microarch.ddd.delivery.core.port.OrderClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCompletedEventHandler implements EventHandler<OrderCompletedEvent> {
    private final OrderClient orderClient;

    @Override
    @EventListener
    public void handle(OrderCompletedEvent event) {
        log.debug("Start handle order completed event: {}", event);
        orderClient.orderCompleted(event);
    }
}
