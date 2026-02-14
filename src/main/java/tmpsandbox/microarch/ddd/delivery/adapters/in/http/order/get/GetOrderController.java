package tmpsandbox.microarch.ddd.delivery.adapters.in.http.order.get;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.api.GetOrdersApi;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.model.Order;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.order.get.mapper.GetOrderMapper;
import tmpsandbox.microarch.ddd.delivery.core.application.query.GetAllUnfinishedOrdersQueryHandler;
import tmpsandbox.microarch.ddd.delivery.core.application.query.GetAllUnfinishedOrdersQueryResponse;

import java.util.List;

@RestController
@AllArgsConstructor
public class GetOrderController implements GetOrdersApi {
    private final GetAllUnfinishedOrdersQueryHandler handler;

    private final GetOrderMapper getOrderMapper;

    @Override
    public ResponseEntity<List<Order>> getOrders() {
        List<GetAllUnfinishedOrdersQueryResponse> handle = handler.handle();
        return ResponseEntity.ok(getOrderMapper.mapToResponse(handle));
    }
}
