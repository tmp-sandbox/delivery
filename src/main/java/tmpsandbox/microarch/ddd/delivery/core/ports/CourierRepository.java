package tmpsandbox.microarch.ddd.delivery.core.ports;

import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourierRepository {
    void save(Courier courier);

    Optional<Courier> findById(UUID id);

    List<Courier> findFree();
}
