package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;


import org.junit.jupiter.api.Test;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class StoragePlaceTest {
    private static final Name BACKPACK = Name.create("рюкзак").getValue();
    private static final Volume BACKPACK_CAPACITY = Volume.create(1).getValue();

    @Test
    public void shouldCreateStoragePlace_whenDataCorrectWithoutOrder() {
        // Given, When:
        var storagePlace = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY).getValue();

        // Then:
        assertAll(
                () -> assertThat(storagePlace.getName()).isEqualTo(BACKPACK),
                () -> assertThat(storagePlace.getTotalVolume()).isEqualTo(BACKPACK_CAPACITY),
                () -> assertThat(storagePlace.getStatus()).isEqualTo(Status.EMPTY)
        );
    }

    @Test
    public void shouldCreateStoragePlace_whenDataCorrectWithOrder() {
        // Given:
        var orderId = UUID.randomUUID();

        // When:
        var storagePlace = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY, orderId).getValue();

        // Then:
        assertAll(
                () -> assertThat(storagePlace.getName()).isEqualTo(BACKPACK),
                () -> assertThat(storagePlace.getTotalVolume()).isEqualTo(BACKPACK_CAPACITY),
                () -> assertThat(storagePlace.getStatus()).isEqualTo(Status.BUSY),
                () -> assertThat(storagePlace.clear()).isEqualTo(orderId)
        );
    }

    @Test
    public void shouldNotBeEquals_whenCompareStoragePlace() {
        // Given:
        var first = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY).getValue();
        var second = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY).getValue();

        // When:
        boolean isEqual = first.equals(second);

        // Then:
        assertThat(isEqual).isFalse();
    }

    @Test
    public void shouldCatchError_whenNameIsNull() {
        // Given, When, Then:
        assertThatThrownBy(() -> StoragePlace.create(null, BACKPACK_CAPACITY).getValue())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("name must not be null");
    }

    @Test
    public void shouldCatchError_whenTotalVolumeIsNull() {
        // Given, When, Then:
        assertThatThrownBy(() -> StoragePlace.create(BACKPACK, null).getValue())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("totalVolume must not be null");
    }

    @Test
    public void shouldReturnOrderAndResetState_whenGetOrder() {
        // Given:
        var expectedOrderId = UUID.randomUUID();
        var storagePlace = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY, expectedOrderId).getValue();

        // When:
        UUID orderId = storagePlace.clear();

        // Then:
        assertAll(
                () -> assertThat(orderId).isEqualTo(expectedOrderId),
                () -> assertThat(storagePlace.getStatus()).isEqualTo(Status.EMPTY),
                () -> assertThat(storagePlace.clear()).isNull()
        );
    }

    @Test
    public void shouldReturnFalse_whenStoreIsEmpty() {
        // Given:
        var storagePlace = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY).getValue();

        // When:
        boolean isOccupied = storagePlace.isOccupied();

        // Then:
        assertThat(isOccupied).isFalse();
    }

    @Test
    public void shouldReturnTrue_whenStoreIsBusy() {
        // Given:
        var storagePlace = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY, UUID.randomUUID()).getValue();

        // When:
        boolean isOccupied = storagePlace.isOccupied();

        // Then:
        assertThat(isOccupied).isTrue();
    }

    @Test
    public void shouldStoreOrder_whenCallStore() {
        // Given:
        var storagePlace = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY).getValue();
        var order = Order.create(UUID.randomUUID(), Location.create(1, 1).getValue(), BACKPACK_CAPACITY).getValue();

        // When
        storagePlace.storeOrder(order);

        // Then:
        assertThat(storagePlace.clear()).isEqualTo(order.getId());
    }

    @Test
    public void shouldNotStoreOrder_whenStoreBusy() {
        // Given:
        var storagePlace = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY, UUID.randomUUID()).getValue();
        var order = Order.create(UUID.randomUUID(), Location.create(1, 1).getValue(), BACKPACK_CAPACITY).getValue();

        // When, Then:
        assertThatThrownBy(() -> storagePlace.storeOrder(order))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot store order while the storage place is busy");
    }

    @Test
    public void shouldNotStoreOrder_whenStoreLess() {
        // Given:
        var storagePlace = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY).getValue();
        var order = Order.create(UUID.randomUUID(), Location.create(1, 1).getValue(), Volume.create(2).getValue()).getValue();

        // When, Then:
        assertThatThrownBy(() -> storagePlace.storeOrder(order))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot store order while the storage place is over the total volume");
    }

}