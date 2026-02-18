package tmpsandbox.microarch.ddd.delivery.core.domain.service;

import libs.errs.Error;
import libs.errs.Result;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Name;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Speed;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Status;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class OrderDispatcherImplTest {
    private final OrderDispatcher orderDispatcher = new OrderDispatcherImpl();

    private static final Volume VOLUME = Volume.create(1).getValue();
    private static final Location START_LOCATION = Location.create(1, 1).getValue();
    private UUID orderId;


    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
    }

    public static Stream<Arguments> provideDispatchData() {
        return Stream.of(
            Arguments.of(
                Order.create(UUID.randomUUID(), START_LOCATION, VOLUME).getValue(),
                List.of(
                    Pair.of(
                        Courier.create(Name.create("Courier").getValue(), Speed.create(1).getValue(), Location.create(1, 1).getValue()).getValue(),
                        true
                    )
                )
            ),
            Arguments.of(
                Order.create(UUID.randomUUID(), START_LOCATION, VOLUME).getValue(),
                List.of(
                    Pair.of(
                        Courier.create(Name.create("Courier").getValue(), Speed.create(1).getValue(), Location.create(1, 1).getValue()).getValue(),
                        true
                    ),
                    Pair.of(
                        Courier.create(Name.create("Courier").getValue(), Speed.create(1).getValue(), Location.create(2, 2).getValue()).getValue(),
                        false
                    )
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideDispatchData")
    public void shouldReturnReadyCourier_whenDispatchOrders(Order order, List<Pair<Courier, Boolean>> couriersWithResult) {
        // Given:
        Courier winner = couriersWithResult.stream()
            .filter(pair -> pair.getRight().equals(true))
            .findFirst().get().getLeft();

        List<Courier> couriers = couriersWithResult.stream()
            .map(Pair::getLeft)
            .toList();

        // When:
        Courier dispatchedCourier = orderDispatcher.dispatch(order, couriers);

        // Then:
        assertAll(
            () -> assertThat(dispatchedCourier).isEqualTo(winner),
            () -> assertThat(order.getStatus()).isEqualTo(Status.ASSIGNED)
        );
    }

    @Test
    public void shouldCatchException_whenOrderIncorrectStatus() {
        // Given:
        var order = Order.create(orderId, START_LOCATION, VOLUME).getValue();

        var courier = Courier.create(
            Name.create("Courier").getValue(),
            Speed.create(1).getValue(),
            Location.create(1, 1).getValue()
        ).getValue();

        order.assign(courier);

        // Then, When:
        assertThatThrownBy(() -> orderDispatcher.dispatch(order, List.of()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Order status is not CREATED");

    }

    @Test
    public void shouldNotDispatch_whenCouriersEmpty() {
        // Given:
        var order = Order.create(orderId, START_LOCATION, VOLUME).getValue();

        // Then:
        Courier dispatch = orderDispatcher.dispatch(order, List.of());

        // When:
        assertThat(dispatch).isNull();
    }


    @Test
    public void shouldTakeOrder_whenCourierHaveStoragePlace() {
        // Given:
        var courier = Courier.create(
            Name.create("Courier").getValue(),
            Speed.create(1).getValue(),
            Location.create(1, 1).getValue()
        ).getValue();

        var order = Order.create(orderId, START_LOCATION, Volume.create(11).getValue()).getValue();

        Result<Boolean, Error> booleanErrorResult = courier.canTakeOrder(order);
        // When, Then:
        assertThatThrownBy(() -> orderDispatcher.dispatch(order, List.of(courier)))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Cannot get value from a failed result");
    }
}