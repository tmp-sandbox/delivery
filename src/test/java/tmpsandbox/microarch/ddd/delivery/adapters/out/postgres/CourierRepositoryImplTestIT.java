package tmpsandbox.microarch.ddd.delivery.adapters.out.postgres;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tmpsandbox.microarch.ddd.delivery.core.domain.BaseIT;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Name;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Speed;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.ports.CourierRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CourierRepositoryImplTestIT extends BaseIT {
    private static final Volume BAG_CAPACITY = Volume.create(10).getValue();
    private static final Location START_LOCATION = Location.create(1, 1).getValue();

    @Autowired
    private CourierRepository courierRepositoryImpl;

    @Autowired
    private CourierJpaRepository courierJpaRepository;

    @BeforeEach
    public void setUp() {
        courierJpaRepository.deleteAll();
    }

    @Test
    void shouldCreateNewCourier_whenSave() {
        // Given:
        Name name = Name.create("Courier").getValue();
        Speed speed = Speed.create(1).getValue();
        Location location = Location.create(1, 1).getValue();

        var courier = Courier.create(name, speed, location).getValue();

        // When:
        courierRepositoryImpl.save(courier);
        Courier courierSaved = courierRepositoryImpl.findById(courier.getId()).get();

        // Then:
        assertAll(
            () -> assertThat(courier.getName()).isEqualTo(name),
            () -> assertThat(courier.getSpeed()).isEqualTo(speed),
            () -> assertThat(courier.getLocation()).isEqualTo(location),
            () -> assertThat(courier).isEqualTo(courierSaved)
        );
    }

    @Test
    void shouldFindFreeCouriers_whenFreeCouriersExists() {
        // Given:
        courierRepositoryImpl.save(createFreeCourier());
        courierRepositoryImpl.save(createFreeCourier());

        courierRepositoryImpl.save(createBusyCourier());

        // When:
        List<Courier> freeCouriers = courierRepositoryImpl.findFree();

        // Then:
        assertThat(freeCouriers).hasSize(2);
    }

    private static Courier createFreeCourier() {
        Name name = Name.create("Courier").getValue();
        Speed speed = Speed.create(1).getValue();
        Location location = Location.create(1, 1).getValue();

        return Courier.create(name, speed, location).getValue();
    }

    private static Courier createBusyCourier() {
        var order = Order.create(UUID.randomUUID(), START_LOCATION, BAG_CAPACITY).getValue();

        Name name = Name.create("Courier").getValue();
        Speed speed = Speed.create(1).getValue();
        Location location = Location.create(1, 1).getValue();

        var courier = Courier.create(name, speed, location).getValue();
        courier.takeOrder(order);

        return courier;
    }
}