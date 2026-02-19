package tmpsandbox.microarch.ddd.delivery.core.ports;

import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    void save(Order order);

    Optional<Order> findById(UUID id);

    Optional<Order> findByStatusCreated();

    List<Order> findAllAssigned();

    Map<UUID, Order> findAllByIds(List<UUID> ids);
}
