package tmpsandbox.microarch.ddd.delivery.core.domain.model.order;

import libs.ddd.Aggregate;
import libs.errs.Except;
import libs.errs.Result;
import libs.errs.UnitResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;

import java.util.UUID;

@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@Getter
public class Order extends Aggregate<UUID> {
    private Location location;

    private Volume volume;

    private Status status;

    private UUID courierId;

    private Order(UUID id, Location location, Volume volume) {
        super(id);
        this.volume = volume;
        this.location = location;
        this.status = Status.CREATED;
    }

    private Order(UUID id, Location location, Volume volume, UUID courierId) {
        super(id);
        this.volume = volume;
        this.location = location;
        this.courierId = courierId;
        this.status = Status.CREATED;
    }

    public static Result<Order, Error> create(UUID id, Location location, Volume volume) {
        Except.againstNull(id, "id");
        Except.againstNull(location, "location");
        Except.againstNull(volume, "volume");

        return Result.success(new Order(id, location, volume));
    }

    public static Result<Order, Error> create(UUID id, Location location, Volume volume, UUID courierId) {
        Except.againstNull(id, "id");
        Except.againstNull(location, "location");
        Except.againstNull(volume, "volume");
        Except.againstNull(courierId, "courierId");

        return Result.success(new Order(id, location, volume, courierId));
    }

    public UnitResult<Error> assign(Courier courier) {
        Except.againstNull(courier, "courier");

        status = Status.ASSIGNED;
        this.courierId = courier.getId();

        return UnitResult.success();
    }

    public UnitResult<Error> complete() {
        if (status != Status.ASSIGNED) {
            throw new IllegalStateException("You can only complete a previously assigned order");
        }

        status = Status.COMPLETED;

        return UnitResult.success();
    }
}
