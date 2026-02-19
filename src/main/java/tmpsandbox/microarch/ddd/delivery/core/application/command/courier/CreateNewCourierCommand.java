package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

import libs.errs.Error;
import libs.errs.Result;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Name;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Speed;

public record CreateNewCourierCommand(
    Name name,
    Speed speed
) {
    public static Result<CreateNewCourierCommand, Error> create(String name, int speed) {
        var nameResult = Name.create(name);
        var speedResult = Speed.create(speed);

        if (nameResult.isFailure()) {
            return Result.failure(nameResult.getError());
        }

        if (speedResult.isFailure()) {
            return Result.failure(speedResult.getError());
        }

        return Result.success(new CreateNewCourierCommand(nameResult.getValue(), speedResult.getValue()));
    }
}
