package tmpsandbox.microarch.ddd.delivery.core.application.command;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tmpsandbox.microarch.ddd.delivery.adapters.out.postgres.CourierJpaRepository;
import tmpsandbox.microarch.ddd.delivery.adapters.out.postgres.OrderJpaRepository;
import tmpsandbox.microarch.ddd.delivery.BaseIT;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.CourierStatus;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Name;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Speed;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.OrderStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderDispatcherCommandHandlerTestIT extends BaseIT {
    private static final Volume BACKPACK_CAPACITY = Volume.create(1).getValue();
    private static final Location START_LOCATION = Location.create(1, 1).getValue();

    @Autowired
    private OrderDispatcherCommandHandler handler;
    @Autowired
    private CourierJpaRepository courierJpaRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    private UUID orderId;

    @BeforeEach
    public void init() {
        orderId = UUID.randomUUID();

        courierJpaRepository.deleteAll();
        orderJpaRepository.deleteAll();
    }

    @Test
    void shouldDispatch_whenOrderNotExist() {
        // Given, When:
        var result = handler.handle();

        // Then:
        assertThat(result.isFailure()).isFalse();
    }

    @Test
    @Transactional
    void shouldDispatch_whenAllExist() {
        // Given:
        Name name = Name.create("Courier").getValue();
        Speed speed = Speed.create(1).getValue();

        var courier = Courier.create(name, speed, START_LOCATION).getValue();
        courierJpaRepository.save(courier);

        var order = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY).getValue();
        orderJpaRepository.save(order);

        // When:
        var result = handler.handle();

        // Then:
        assertAll(
            () -> assertThat(result.isFailure()).isFalse(),
            () -> assertThat(courierJpaRepository.findById(courier.getId()).get().getStoragePlaces().getFirst().getStatus()).isEqualTo(CourierStatus.BUSY),
            () -> assertThat(orderJpaRepository.findById(order.getId()).get().getStatus()).isEqualTo(OrderStatus.ASSIGNED)
        );
    }
}