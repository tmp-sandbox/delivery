package tmpsandbox.microarch.ddd.delivery.adapters.in.http.courier.create.mapper;

import org.mapstruct.Mapper;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.model.CreateCourierResponse;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.model.NewCourier;
import tmpsandbox.microarch.ddd.delivery.core.application.command.courier.CreateNewCourierCommand;

import java.util.UUID;

@Mapper
public interface NewCourierMapper {

    CreateNewCourierCommand mapToCommand(NewCourier newCourier);

    CreateCourierResponse toCreateCourierResponse(UUID courierId);
}
