package tmpsandbox.microarch.ddd.delivery.core.application.command.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tmpsandbox.microarch.ddd.delivery.adapters.out.postgres.OrderJpaRepository;
import tmpsandbox.microarch.ddd.delivery.core.domain.BaseIT;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CreateNewOrderCommandCommandHandlerTestIT extends BaseIT {
    @Autowired
    private CreateNewOrderCommandHandler handler;

    @Autowired
    private OrderJpaRepository orderRepository;

    @BeforeEach
    public void setup() {
        orderRepository.deleteAll();
    }

    @Test
    void shouldCreateNewOrder_whenCallHandle() {
        // Given:
        var command = CreateNewOrderCommand.create(UUID.randomUUID(), "street", 5).getValue();

        // When:
        var result = handler.handle(command);
        Order createdOrder = orderRepository.findAll().getFirst();

        // Then:
        assertAll(
            () -> assertThat(result.isFailure()).isFalse(),
            () -> assertThat(createdOrder).isNotNull(),
            () -> assertThat(createdOrder.getVolume().getValue()).isEqualTo(5)
        );
    }
}