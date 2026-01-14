package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;

import libs.ddd.BaseEntity;
import libs.errs.Except;
import libs.errs.Result;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.kernel.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.UUID;
import libs.errs.Error;


@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class StoragePlace extends BaseEntity<UUID> {
    private Name name;
    private Volume totalVolume;
    private UUID orderId;
    private Status status;

    private StoragePlace(Name name, Volume totalVolume) {
        super(UUID.randomUUID());
        this.name = name;
        this.totalVolume = totalVolume;
        this.status = Status.EMPTY;
    }

    private StoragePlace(Name name, Volume totalVolume, UUID orderId) {
        super(UUID.randomUUID());
        this.name = name;
        this.totalVolume = totalVolume;
        this.orderId = orderId;
        this.status = Status.BUSY;
    }


    public static Result<StoragePlace, Error> create(Name name, Volume totalVolume) {
        Except.againstNull(name, "name");
        Except.againstNull(totalVolume, "totalVolume");

        return Result.success(new StoragePlace(name, totalVolume));
    }

    public static Result<StoragePlace, Error> create(Name name, Volume totalVolume, UUID orderId) {
        Except.againstNull(name, "name");
        Except.againstNull(totalVolume, "totalVolume");
        Except.againstNull(orderId, "orderId");

        return Result.success(new StoragePlace(name, totalVolume, orderId));
    }

    public UUID clear() {
        UUID orderId = this.orderId;

        this.orderId = null;
        status = Status.EMPTY;

        return orderId;
    }

    public boolean isOccupied() {
        return status != Status.EMPTY;
    }

    public void storeOrder(Order order) {
        if (status == Status.BUSY) {
            throw new IllegalStateException("Cannot store order while the storage place is busy");
        }

        if (order.getVolume().isGreaterThan(totalVolume)) {
            throw new IllegalStateException("Cannot store order while the storage place is over the total volume");
        }

        orderId = order.getId();
        status = Status.BUSY;
    }
}
