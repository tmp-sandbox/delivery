package tmpsandbox.microarch.ddd.delivery.core.domain.model.order;

import libs.ddd.BaseEntity;
import libs.errs.Except;
import libs.errs.Result;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.kernel.Volume;

import java.util.UUID;

@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Order extends BaseEntity<UUID> {
    @Getter
    private Volume volume;

    private Order(Volume volume) {
        super(UUID.randomUUID());
        this.volume = volume;
    }

    public static Result<Order, Error> create(Volume volume) {
        Except.againstNull(volume, "volume");
        return Result.success(new Order(volume));
    }
}
