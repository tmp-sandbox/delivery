package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

import libs.errs.Error;
import libs.errs.Result;
import libs.errs.UnitResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Name;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Speed;
import tmpsandbox.microarch.ddd.delivery.core.ports.CourierRepository;

import java.util.UUID;

/*
Создать курьера (опционально, если есть время и желание). Мы можем добавлять курьеров.
Поля Command:
- Name string
- Speed int
 */
@Service
@AllArgsConstructor
public class CreateNewCourierCommandHandler {
    private CourierRepository courierRepository;

    public Result<UUID, Error> handle(CreateNewCourierCommand commandDto) {
        var defaultLocation = Location.create(1, 1).getValue();

        var nameResult = Name.create(commandDto.name());
        var speedResult = Speed.create(commandDto.speed());

        if (nameResult.isFailure()) {
            return Result.failure(nameResult.getError());
        }

        if (speedResult.isFailure()) {
            return Result.failure(speedResult.getError());
        }

        var courierResult = Courier.create(nameResult.getValue(), speedResult.getValue(), defaultLocation);

        if (courierResult.isFailure()) {
            return Result.failure(courierResult.getError());
        }

        courierRepository.save(courierResult.getValue());
        return Result.success(courierResult.getValue().getId());
    }
}
