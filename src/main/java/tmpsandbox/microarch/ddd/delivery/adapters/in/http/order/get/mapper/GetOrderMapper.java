package tmpsandbox.microarch.ddd.delivery.adapters.in.http.order.get.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tmpsandbox.microarch.ddd.delivery.adapters.in.http.model.Order;
import tmpsandbox.microarch.ddd.delivery.core.application.query.GetAllUnfinishedOrdersQueryResponse;

import java.util.List;

@Mapper
public interface GetOrderMapper {
    List<Order> mapToResponse(List<GetAllUnfinishedOrdersQueryResponse> ordersQueryResponses);

    @Mapping(target = "location.x", source = "x")
    @Mapping(target = "location.y", source = "y")
    Order mapOrder(GetAllUnfinishedOrdersQueryResponse ordersQueryResponse);
}
