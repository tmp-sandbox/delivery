package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;


import org.junit.jupiter.api.Test;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.kernel.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

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
    public void shouldReturnTrue_whenStoreIsEmpty() {
        // Given:
        var storagePlace = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY).getValue();

        // When:
        boolean isOccupied = storagePlace.isOccupied();

        // Then:
        assertThat(isOccupied).isTrue();
    }

    @Test
    public void shouldStoreOrder_whenCallStore() {
        // Given:
        var storagePlace = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY).getValue();
        var order = Order.create(BACKPACK_CAPACITY).getValue();

        // When
        storagePlace.storeOrder(order);

        // Then:
        assertThat(storagePlace.takeOrder()).isEqualTo(order.getId());
    }

    @Test
    public void shouldNotStoreOrder_whenStoreLess() {
        // Given:
        var storagePlace = StoragePlace.create(BACKPACK, BACKPACK_CAPACITY).getValue();
        var order = Order.create(Volume.create(2).getValue()).getValue();

        // When:
        var result = storagePlace.storeOrder(order);

        // Then:
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().getCode()).isEqualTo("volume");
        assertThat(result.getError().getMessage()).isEqualTo("Cannot store order while the storage place is over the total volume");
    }
}