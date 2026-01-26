package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import libs.ddd.BaseEntity;
import libs.errs.Error;
import libs.errs.Except;
import libs.errs.Result;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.UUID;

@Entity
@Table(name = "storage_places")
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class StoragePlace extends BaseEntity<UUID> {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    private Name name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "total_volume"))
    private Volume totalVolume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    @Setter
    private Courier courier;

    private UUID orderId;

    @Enumerated(EnumType.STRING)
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
