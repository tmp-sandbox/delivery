package tmpsandbox.microarch.ddd.delivery.core.domain.model.order.event;

import libs.ddd.DomainEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.UUID;

@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@Getter
public class OrderCompletedEvent extends DomainEvent {
    private final UUID orderId;
    private final UUID courierId;

    public OrderCompletedEvent(Order order) {
        super(order);

        this.orderId = order.getId();
        this.courierId = order.getCourierId();
    }
}
