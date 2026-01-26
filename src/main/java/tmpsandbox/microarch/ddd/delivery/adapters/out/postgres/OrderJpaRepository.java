package tmpsandbox.microarch.ddd.delivery.adapters.out.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findFirstByStatus(Status status);

    List<Order> findAllByStatus(Status status);
}
