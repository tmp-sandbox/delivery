package tmpsandbox.microarch.ddd.delivery.core.application.command.order;

import libs.errs.Error;
import libs.errs.Result;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Address;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;

import java.util.UUID;

public record CreateNewOrderCommand(
    UUID orderId,
    Address address,
    Volume volume
) {

    public static Result<CreateNewOrderCommand, Error> create(
        UUID orderId,
        String country,
        String city,
        String street,
        String house,
        String apartment,
        int volume
    ) {
        var addressResult = Address.create(country, city, street, house, apartment);

        if (addressResult.isFailure()) {
            return Result.failure(addressResult.getError());
        }

        var volumeResult = Volume.create(volume);

        if (volumeResult.isFailure()) {
            return Result.failure(volumeResult.getError());
        }

        return Result.success(new CreateNewOrderCommand(orderId, addressResult.getValue(), volumeResult.getValue()));
    }
}
