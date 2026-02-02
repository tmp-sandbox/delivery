package tmpsandbox.microarch.ddd.delivery.adapters.out.postgres;

import libs.ddd.BaseEntity;
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
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Status;
import tmpsandbox.microarch.ddd.delivery.core.ports.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderRepositoryImplTestIT extends BaseIT {
    private static final Volume BACKPACK_CAPACITY = Volume.create(1).getValue();
    private static final Location START_LOCATION = Location.create(1, 1).getValue();

    @Autowired
    private OrderRepository orderRepositoryImpl;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();

        orderJpaRepository.deleteAll();
    }

    @Test
    void shouldCreateNewOrder_whenSave() {
        // Given:
        var order = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY).getValue();

        // When:
        orderRepositoryImpl.save(order);
        Order orderSaved = orderRepositoryImpl.findById(orderId).get();

        // Then:
        assertAll(
            () -> assertThat(orderSaved).isEqualTo(order),
            () -> assertThat(orderSaved.getLocation()).isEqualTo(START_LOCATION),
            () -> assertThat(orderSaved.getVolume()).isEqualTo(BACKPACK_CAPACITY),
            () -> assertThat(orderSaved.getStatus()).isEqualTo(Status.CREATED)
        );
    }

    @Test
    void shouldCreateNewOrderWithCourier_whenSave() {
        // Given:
        UUID courierId = UUID.randomUUID();
        var order = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY, courierId).getValue();

        // When:
        orderRepositoryImpl.save(order);
        Order orderSaved = orderRepositoryImpl.findById(orderId).get();

        // Then:
        assertAll(
            () -> assertThat(orderSaved).isEqualTo(order),
            () -> assertThat(orderSaved.getLocation()).isEqualTo(START_LOCATION),
            () -> assertThat(orderSaved.getVolume()).isEqualTo(BACKPACK_CAPACITY),
            () -> assertThat(orderSaved.getStatus()).isEqualTo(Status.CREATED),
            () -> assertThat(orderSaved.getCourierId()).isEqualTo(courierId)
        );
    }

    @Test
    void shouldFindCreatedOrder_whenCallFindStatusCreated() {
        // Given:
        UUID courierId = UUID.randomUUID();
        var orderFirst = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY, courierId).getValue();
        var orderSecond = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY, courierId).getValue();

        orderRepositoryImpl.save(orderFirst);
        orderRepositoryImpl.save(orderSecond);

        // When:
        Order orderSaved = orderRepositoryImpl.findByStatusCreated().get();

        // Then:
        assertAll(
            () -> assertThat(orderSaved).isNotNull()
        );
    }

    @Test
    void shouldFindAllAssignedOrders_whenCallFindAllAssigned() {
        // Given:
        var orderFirst = Order.create(UUID.randomUUID(), START_LOCATION, BACKPACK_CAPACITY).getValue();
        var orderSecond = Order.create(UUID.randomUUID(), START_LOCATION, BACKPACK_CAPACITY).getValue();

        var courier = Courier.create(
            Name.create("Courier").getValue(),
            Speed.create(1).getValue(),
            Location.create(1, 1).getValue()
        ).getValue();


        orderFirst.assign(courier);
        orderSecond.assign(courier);

        orderRepositoryImpl.save(orderFirst);
        orderRepositoryImpl.save(orderSecond);

        // When:
        List<Order> allAssigned = orderRepositoryImpl.findAllAssigned();

        // Then:
        assertThat(allAssigned)
            .hasSize(2)
            .allSatisfy(order -> assertAll(
                () -> assertThat(order.getStatus()).isEqualTo(Status.ASSIGNED),
                () -> assertThat(order.getCourierId()).isEqualTo(courier.getId())
            ));

        assertThat(allAssigned)
            .extracting(BaseEntity::getId)
            .containsExactlyInAnyOrder(orderFirst.getId(), orderSecond.getId());
    }

    @Test
    void shouldReturnOrdersByIds_whenCallFindByIds() {
        // Given:
        UUID orderId2 = UUID.randomUUID();
        UUID orderId4 = UUID.randomUUID();
        UUID orderId5 = UUID.randomUUID();

        var order1 = Order.create(UUID.randomUUID(), START_LOCATION, BACKPACK_CAPACITY).getValue();
        var order2 = Order.create(orderId2, START_LOCATION, BACKPACK_CAPACITY).getValue();
        var order3 = Order.create(UUID.randomUUID(), START_LOCATION, BACKPACK_CAPACITY).getValue();
        var order4 = Order.create(orderId4, START_LOCATION, BACKPACK_CAPACITY).getValue();
        var order5 = Order.create(orderId5, START_LOCATION, BACKPACK_CAPACITY).getValue();

        orderRepositoryImpl.save(order1);
        orderRepositoryImpl.save(order2);
        orderRepositoryImpl.save(order3);
        orderRepositoryImpl.save(order4);
        orderRepositoryImpl.save(order5);

        // When:
        Map<UUID, Order> allByIds = orderRepositoryImpl.findAllByIds(List.of(orderId2, orderId4, orderId5));

        // Then:
        assertThat(allByIds.keySet()).containsExactlyInAnyOrder(orderId2, orderId4, orderId5);
    }
}