package tmpsandbox.microarch.ddd.delivery.core.application.queries;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tmpsandbox.microarch.ddd.delivery.adapters.out.postgres.CourierJpaRepository;
import tmpsandbox.microarch.ddd.delivery.core.domain.BaseIT;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Name;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Speed;

import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class GetAllCouriersQueryHandlerTestIT extends BaseIT {
    @Autowired
    private GetAllCouriersQueryHandler handler;
    @Autowired
    private CourierJpaRepository courierJpaRepository;

    @Test
    void shouldReturnAllCouriers() {
        // Given:
        var couriers = IntStream.rangeClosed(1, 4)
            .mapToObj(i -> Courier.create(
                Name.create("Courier").getValue(),
                Speed.create(1).getValue(),
                Location.create(2, 2).getValue()
            ).getValue())
            .toList();

        courierJpaRepository.saveAll(couriers);

        var expected = couriers.stream()
            .map(c -> new GetAllCouriersQueryResponse(
                c.getId(),
                c.getName().getValue(),
                "2",
                "2"
            ))
            .toList();

        // When:
        var actual = handler.handle();

        // Then:
        assertThat(actual)
            .hasSize(4)
            .containsExactlyInAnyOrderElementsOf(expected);
    }
}