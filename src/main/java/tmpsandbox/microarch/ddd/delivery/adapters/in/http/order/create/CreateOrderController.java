package tmpsandbox.microarch.ddd.delivery.adapters.in.http.order.create;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.api.CreateOrderApi;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.model.CreateOrderResponse;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.order.create.mapper.CreateOrderMapper;
import tmpsandbox.microarch.ddd.delivery.core.application.command.order.CreateNewOrderCommand;
import tmpsandbox.microarch.ddd.delivery.core.application.command.order.CreateNewOrderCommandHandler;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class CreateOrderController implements CreateOrderApi {
    private final CreateNewOrderCommandHandler handler;

    private final CreateOrderMapper createOrderMapper;

    @Override
    public ResponseEntity<CreateOrderResponse> createOrder() {
        var resultHandle = handler.handle(new CreateNewOrderCommand(UUID.randomUUID(), "street", 5));
        if (resultHandle.isFailure()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(createOrderMapper.mapToResponse(resultHandle.getValue()));
    }
}
