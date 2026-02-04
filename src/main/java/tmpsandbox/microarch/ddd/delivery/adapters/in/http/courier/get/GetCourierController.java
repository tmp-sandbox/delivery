package tmpsandbox.microarch.ddd.delivery.adapters.in.http.courier.get;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.api.GetCouriersApi;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.courier.get.mapper.GetCourierMapper;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.model.Courier;
import tmpsandbox.microarch.ddd.delivery.core.application.queries.GetAllCouriersQueryHandler;
import tmpsandbox.microarch.ddd.delivery.core.application.queries.GetAllCouriersQueryResponse;

import java.util.List;

@RestController
@AllArgsConstructor
public class GetCourierController implements GetCouriersApi {
    private final GetAllCouriersQueryHandler handler;

    private final GetCourierMapper getCourierMapper;

    @Override
    public ResponseEntity<List<Courier>> getCouriers() {
        List<GetAllCouriersQueryResponse> handle = handler.handle();
        return ResponseEntity.ok(getCourierMapper.mapToResponse(handle));
    }
}
