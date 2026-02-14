package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

import libs.errs.Error;
import libs.errs.UnitResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.port.CourierRepository;

import java.util.Optional;

/*
Добавить место хранения (опционально, если есть время и желание). Мы можем добавлять места хранения к курьеру.
Поля Command:
- CourierID UUID
- Name string
- TotalVolume int
 */
@Service
@AllArgsConstructor
public class CreateNewStoragePlaceCommandHandler {
    private final CourierRepository courierRepositoryImpl;

    public UnitResult<Error> handle(CreateNewStoragePlaceCommand commandDto) {
        Optional<Courier> courierResult = courierRepositoryImpl.findById(commandDto.courierId());
        if (courierResult.isEmpty()) {
            return UnitResult.failure(Error.of("courierById", "Not found courier with id " + commandDto.courierId()));
        }

        Courier courier = courierResult.get();

        var courierWithNewStorage = courier.addStoragePlace(commandDto.name(), commandDto.totalVolume());
        if (courierWithNewStorage.isFailure()) {
            return UnitResult.failure(courierWithNewStorage.getError());
        }

        courierRepositoryImpl.save(courier);
        return UnitResult.success();
    }
}
