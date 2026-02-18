package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;

import libs.ddd.BaseEntity;
import libs.errs.Except;
import libs.errs.Result;
import libs.errs.UnitResult;
import libs.errs.Error;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.kernel.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.UUID;


@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class StoragePlace extends BaseEntity<UUID> {
    @Getter
    private Name name;

    @Getter
    private Volume totalVolume;

    private UUID orderId;

    @Getter
    private Status status;

    private StoragePlace(Name name, Volume totalVolume) {
        super(UUID.randomUUID());
        this.name = name;
        this.totalVolume = totalVolume;
        this.status = Status.EMPTY;
    }

    public static Result<StoragePlace, Error> create(Name name, Volume totalVolume) {
        Except.againstNull(name, "name");
        Except.againstNull(totalVolume, "totalVolume");

        return Result.success(new StoragePlace(name, totalVolume));
    }

    public UUID takeOrder() {
        UUID orderId = this.orderId;

        this.orderId = null;
        status = Status.EMPTY;

        return orderId;
    }

    public boolean isOccupied() {
        return status == Status.EMPTY;
    }

    public UnitResult<Error> storeOrder(Order order) {
        if (status == Status.OCCUPIED) {
            return UnitResult.failure(Error.of("status", "Cannot store order while the storage place is occupied"));
        }

        if (order.getVolume().isGreaterThan(totalVolume)) {
            return UnitResult.failure(Error.of("volume", "Cannot store order while the storage place is over the total volume"));
        }

        orderId = order.getId();
        status = Status.OCCUPIED;
        return UnitResult.success();
    }
}
