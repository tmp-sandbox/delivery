package tmpsandbox.microarch.ddd.delivery.core.domain.model.order;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import libs.ddd.Aggregate;
import libs.errs.Error;
import libs.errs.Except;
import libs.errs.Result;
import libs.errs.UnitResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;

import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@Getter
public class Order extends Aggregate<UUID> {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "location"))
    private Location location;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "volume"))
    private Volume volume;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private UUID courierId;

    private Order(UUID id, Location location, Volume volume) {
        super(id);
        this.volume = volume;
        this.location = location;
        this.status = OrderStatus.CREATED;
    }

    private Order(UUID id, Location location, Volume volume, UUID courierId) {
        super(id);
        this.volume = volume;
        this.location = location;
        this.courierId = courierId;
        this.status = OrderStatus.CREATED;
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

        status = OrderStatus.ASSIGNED;
        this.courierId = courier.getId();

        return UnitResult.success();
    }

    public UnitResult<Error> complete() {
        if (status != OrderStatus.ASSIGNED) {
            throw new IllegalStateException("You can only complete a previously assigned order");
        }

        status = OrderStatus.COMPLETED;

        return UnitResult.success();
    }
}
