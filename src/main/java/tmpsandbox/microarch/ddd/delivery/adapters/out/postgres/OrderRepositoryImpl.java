package tmpsandbox.microarch.ddd.delivery.adapters.out.postgres;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.OrderStatus;
import tmpsandbox.microarch.ddd.delivery.core.port.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public void save(Order order) {
        orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderJpaRepository.findById(id);
    }

    @Override
    public Optional<Order> findByStatusCreated() {
        return orderJpaRepository.findFirstByStatus(OrderStatus.CREATED);
    }

    @Override
    public List<Order> findAllAssigned() {
        return orderJpaRepository.findAllByStatus(OrderStatus.ASSIGNED);
    }

    @Override
    public Map<UUID, Order> findAllByIds(List<UUID> ids) {
        return orderJpaRepository.findAllById(ids).stream()
            .collect(Collectors.toMap(
                Order::getId,
                o -> o
            ));
    }
}
