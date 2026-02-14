package tmpsandbox.microarch.ddd.delivery.core.domain.service;

import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDispatcher {
    Optional<Courier> dispatch(Order order, List<Courier> couriers);
}
