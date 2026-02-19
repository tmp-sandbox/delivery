package tmpsandbox.microarch.ddd.delivery.core.application.command.order;

import libs.errs.Err;
import libs.errs.Error;
import libs.errs.Result;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;

import java.util.UUID;

public record CreateNewOrderCommand(
    UUID orderId,
    String street,
    Volume volume
) {

    public static Result<CreateNewOrderCommand, Error> create(UUID orderId, String street, int volume) {
        var errorStreetResult = Err.againstNullOrEmpty(street, "street");

        if (errorStreetResult.isFailure()) {
            return Result.failure(errorStreetResult.getError());
        }

        var volumeErrorResult = Volume.create(volume);

        if (volumeErrorResult.isFailure()) {
            return Result.failure(volumeErrorResult.getError());
        }

        return Result.success(new CreateNewOrderCommand(orderId, street, volumeErrorResult.getValue()));
    }
}
