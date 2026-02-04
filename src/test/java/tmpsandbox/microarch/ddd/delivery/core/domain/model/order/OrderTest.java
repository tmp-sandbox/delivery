package tmpsandbox.microarch.ddd.delivery.core.domain.model.order;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Name;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Speed;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {
    private static final Volume BACKPACK_CAPACITY = Volume.create(1).getValue();
    private static final Location START_LOCATION = Location.create(1, 1).getValue();
    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
    }

    @Test
    public void shouldCreateOrderWithoutCourier_whenCorrect() {
        // Given, When:
        var order = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY).getValue();

        // Then:
        assertAll(
                () -> assertThat(order.getId()).isEqualTo(orderId),
                () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED),
                () -> assertThat(order.getVolume()).isEqualTo(BACKPACK_CAPACITY),
                () -> assertThat(order.getLocation()).isEqualTo(START_LOCATION)
        );
    }

    @Test
    public void shouldCreateOrderWithCourier_whenCorrect() {
        // Given:
        UUID courierId = UUID.randomUUID();

        // When:
        var order = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY, courierId).getValue();

        // Then:
        assertAll(
                () -> assertThat(order.getId()).isEqualTo(orderId),
                () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED),
                () -> assertThat(order.getVolume()).isEqualTo(BACKPACK_CAPACITY),
                () -> assertThat(order.getLocation()).isEqualTo(START_LOCATION),
                () -> assertThat(order.getCourierId()).isEqualTo(courierId)

        );
    }

    @Test
    public void shouldCreateSameOrders_whenOrderIdIsEquals() {
        // Given, When:
        var first = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY).getValue();
        var second = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY).getValue();

        // Then:
        assertThat(first).isEqualTo(second);
    }

    @Test
    public void shouldCreateDifferentOrders_whenOrderIdIsNotEquals() {
        // Given:
        UUID firstId = UUID.randomUUID();
        UUID secondId = UUID.randomUUID();

        // When:
        var first = Order.create(firstId, START_LOCATION, BACKPACK_CAPACITY).getValue();
        var second = Order.create(secondId, START_LOCATION, BACKPACK_CAPACITY).getValue();

        // Then:
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void shouldCompleteOrder_whenOk() {
        // Given:
        var order = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY).getValue();
        var courier = Courier.create(
                Name.create("Courier").getValue(),
                Speed.create(1).getValue(),
                Location.create(1, 1).getValue()
        ).getValue();

        order.assign(courier);

        // When:
        order.complete();

        // Then:
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    public void shouldThrowError_whenOrderNotAssigned() {
        // Given:
        var order = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY).getValue();

        // When, Then:
        assertThatThrownBy(() -> order.complete())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("You can only complete a previously assigned order");
    }

    @Test
    public void shouldThrowError_whenCourierIsNull() {
        // Given:
        var order = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY).getValue();

        // When, Then:
        assertThatThrownBy(() -> order.assign(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("courier must not be null");
    }

    @Test
    public void shouldAssignCourier_whenCourierExists() {
        // Given:
        var order = Order.create(orderId, START_LOCATION, BACKPACK_CAPACITY).getValue();
        var courier = Courier.create(
                Name.create("Courier").getValue(),
                Speed.create(1).getValue(),
                Location.create(1, 1).getValue()
        ).getValue();

        // When:
        order.assign(courier);

        // Then:
        assertAll(
                () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.ASSIGNED),
                () -> assertThat(order.getCourierId()).isEqualTo(courier.getId())

        );
    }
}