package tmpsandbox.microarch.ddd.delivery.adapters.out.postgres;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Status;
import tmpsandbox.microarch.ddd.delivery.core.ports.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        return orderJpaRepository.findFirstByStatus(Status.CREATED);
    }

    @Override
    public List<Order> findAllAssigned() {
        return orderJpaRepository.findAllByStatus(Status.ASSIGNED);
    }
}
