package tmpsandbox.microarch.ddd.delivery.adapters.in.http.courier.create;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.api.CreateCourierApi;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.courier.create.mapper.NewCourierMapper;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.model.CreateCourierResponse;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.model.NewCourier;
import tmpsandbox.microarch.ddd.delivery.core.application.command.courier.CreateNewCourierCommandHandler;

@RestController
@AllArgsConstructor
public class CreateCourierController implements CreateCourierApi {
    private final CreateNewCourierCommandHandler handler;

    private final NewCourierMapper newCourierMapper;

    @Override
    public ResponseEntity<CreateCourierResponse> createCourier(NewCourier newCourier) {
        var resultCreate = handler.handle(newCourierMapper.mapToCommand(newCourier));

        if (resultCreate.isFailure()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(newCourierMapper.toCreateCourierResponse(resultCreate.getValue()));
    }
}
