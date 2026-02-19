package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tmpsandbox.microarch.ddd.delivery.adapters.out.postgres.CourierJpaRepository;
import tmpsandbox.microarch.ddd.delivery.core.domain.BaseIT;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CreateNewCourierCommandCommandHandlerTestIT extends BaseIT {
    private static final String TEST_NAME = "test";
    private static final Integer TEST_SPEED = 5;

    @Autowired
    private CreateNewCourierCommandHandler handler;

    @Autowired
    private CourierJpaRepository courierJpaRepository;

    @BeforeEach
    public void setUp() {
        courierJpaRepository.deleteAll();
    }

    @Test
    public void shouldCreateNewCourier_whenHandle() {
        // Given:
        var createNewCourierCommand = CreateNewCourierCommand.create(TEST_NAME, TEST_SPEED);

        // When:
        var result = handler.handle(createNewCourierCommand.getValue());

        // Then:
        List<Courier> allCouriers = courierJpaRepository.findAll();

        assertAll(
            () -> assertThat(result.isFailure()).isFalse(),
            () -> assertThat(allCouriers)
                .singleElement()
                .satisfies(courier -> {
                        assertThat(courier.getName().getValue()).isEqualTo(TEST_NAME);
                        assertThat(courier.getSpeed().getValue()).isEqualTo(TEST_SPEED);
                    }
                )
        );
    }
}