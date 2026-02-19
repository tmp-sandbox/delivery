package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

import libs.errs.Error;
import libs.errs.UnitResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.ports.CourierRepository;

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

    public UnitResult<Error> handle(CreateNewCourierCommand commandDto) {
        var defaultLocation = Location.create(1, 1).getValue();
        var courierResult = Courier.create(commandDto.name(), commandDto.speed(), defaultLocation);

        if (courierResult.isFailure()) {
            return UnitResult.failure(courierResult.getError());
        }

        courierRepository.save(courierResult.getValue());
        return UnitResult.success();
    }
}
