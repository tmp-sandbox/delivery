package tmpsandbox.microarch.ddd.delivery.core.domain.service;

import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.List;

public interface OrderDispatcher {
    Courier dispatch(Order order, List<Courier> couriers);
}
