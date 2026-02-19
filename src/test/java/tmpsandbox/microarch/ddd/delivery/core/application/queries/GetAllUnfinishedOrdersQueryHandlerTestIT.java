package tmpsandbox.microarch.ddd.delivery.core.application.queries;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tmpsandbox.microarch.ddd.delivery.adapters.out.postgres.OrderJpaRepository;
import tmpsandbox.microarch.ddd.delivery.core.domain.BaseIT;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class GetAllUnfinishedOrdersQueryHandlerTestIT extends BaseIT {
    private static final Volume BACKPACK_CAPACITY = Volume.create(1).getValue();

    @Autowired
    private GetAllUnfinishedOrdersQueryHandler handler;
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Test
    void shouldGetAllUnfinishedOrders_whenFindAll() {
        // Given:
        List<Order> orders = IntStream.rangeClosed(1, 5)
            .mapToObj(i -> Order.create(
                UUID.randomUUID(),
                Location.create(1, 1).getValue(),
                BACKPACK_CAPACITY
            ).getValue()).toList();

        var expected = orders.stream()
            .map(
                order -> new GetAllUnfinishedOrdersQueryResponse(order.getId(), "1,1")
            ).toList();

        orderJpaRepository.saveAll(orders);

        // When:
        var response = handler.handle();

        // Then:
        assertThat(response)
            .hasSize(5)
            .containsExactlyInAnyOrderElementsOf(expected);
    }
}