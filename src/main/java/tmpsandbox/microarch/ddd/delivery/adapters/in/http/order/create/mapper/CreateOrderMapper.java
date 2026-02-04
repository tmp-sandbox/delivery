package tmpsandbox.microarch.ddd.delivery.adapters.in.http.order.create.mapper;

import org.mapstruct.Mapper;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.model.CreateOrderResponse;

import java.util.UUID;

@Mapper
public interface CreateOrderMapper {
    CreateOrderResponse mapToResponse(UUID orderId);
}
