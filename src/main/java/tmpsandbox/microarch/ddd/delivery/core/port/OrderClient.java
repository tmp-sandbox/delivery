package tmpsandbox.microarch.ddd.delivery.core.port;

import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.event.OrderCompletedEvent;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.event.OrderCreatedEvent;

public interface OrderClient {
    void orderCreated(OrderCreatedEvent event);
    void orderCompleted(OrderCompletedEvent event);
}
