package tmpsandbox.microarch.ddd.delivery.adapters.out.postgres;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.port.CourierRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class CourierRepositoryImpl implements CourierRepository {
    private final CourierJpaRepository courierJpaRepository;

    @Override
    public void save(Courier courier) {
        courierJpaRepository.save(courier);
    }

    @Override
    public Optional<Courier> findById(UUID id) {
        return courierJpaRepository.findById(id);
    }

    @Override
    public List<Courier> findFree() {
        return courierJpaRepository.findFree();
    }

    @Override
    public List<Courier> findBusy() {
        return courierJpaRepository.findBusy();
    }
}
