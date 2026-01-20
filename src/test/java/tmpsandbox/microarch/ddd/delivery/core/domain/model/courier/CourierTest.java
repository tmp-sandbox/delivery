package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CourierTest {
    private static final Name BAG = Name.create("Сумка").getValue();
    private static final Volume BAG_CAPACITY = Volume.create(10).getValue();
    private static final Location START_LOCATION = Location.create(1, 1).getValue();
    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
    }

    @Test
    public void shouldCreateCourier() {
        // Given:
        Name name = Name.create("Courier").getValue();
        Speed speed = Speed.create(1).getValue();
        Location location = Location.create(1, 1).getValue();

        // When:
        var courier = Courier.create(name, speed, location).getValue();

        // Then:
        assertAll(
                () -> assertThat(courier.getName()).isEqualTo(name),
                () -> assertThat(courier.getSpeed()).isEqualTo(speed),
                () -> assertThat(courier.getLocation()).isEqualTo(location),
                () -> assertThat(courier.getStoragePlaces()).anyMatch(storagePlace ->
                        storagePlace.getName().equals(BAG) &&
                                storagePlace.getTotalVolume().equals(BAG_CAPACITY))
        );
    }

    @Test
    public void shouldAddStoragePlace() {
        // Given:
        var courier = getCourier();

        String nameStorage = "storage_test";
        int volume = 100;

        // When:
        courier.addStoragePlace(nameStorage, volume);

        // Then:
        assertThat(courier.getStoragePlaces()).anyMatch(storagePlace ->
                storagePlace.getName().equals(Name.create(nameStorage).getValue()) &&
                        storagePlace.getTotalVolume().equals(Volume.create(volume).getValue()));
    }

    private static Courier getCourier() {
        Name name = Name.create("Courier").getValue();
        Speed speed = Speed.create(1).getValue();
        Location location = Location.create(1, 1).getValue();

        return Courier.create(name, speed, location).getValue();
    }

    @Test
    public void shouldCanTakeOrder() {
        // Given:
        Courier courier = getCourier();
        var order = Order.create(orderId, START_LOCATION, BAG_CAPACITY).getValue();

        // When:
        Boolean canTakeOrder = courier.canTakeOrder(order).getValue();

        // Then:
        assertThat(canTakeOrder).isTrue();
    }

    @Test
    public void shouldCompleteOrder() {
        // Given:
        Courier courier = getCourier();
        var order = Order.create(orderId, START_LOCATION, BAG_CAPACITY).getValue();
        courier.takeOrder(order);

        // When:
        var result = courier.completeOrder(order);

        // Then:
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void shouldCalculateTimeToLocation() {
        // Given:
        Name name = Name.create("Courier").getValue();
        Speed speed = Speed.create(2).getValue();
        Location location = Location.create(1, 1).getValue();

        Courier courier = Courier.create(name, speed, location).getValue();


        // When:
        Double timeToLocation = courier.calculateTimeToLocation(Location.create(1, 10).getValue()).getValue();

        // Then:
        assertThat(timeToLocation).isEqualTo(5.0);
    }
}

