package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import tmpsandbox.microarch.ddd.delivery.core.domain.BaseIT;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CreateNewStoragePlaceCommandHandlerTestIT extends BaseIT {
    private static final String TEST_NAME = "test";
    private static final Integer TEST_SPEED = 5;

    @Autowired
    private CreateNewCourierCommandHandler handler;

    @Test
    public void shouldCreateNewCourier_whenHandle() {
        // Given:
        CreateNewCourierCommand createNewCourierCommand = CreateNewCourierCommand.create(
            TEST_NAME,
            TEST_SPEED
        ).getValue();

        // When:
        var handle = handler.handle(createNewCourierCommand);

        // Then:
        assertThat(handle.isSuccess()).isTrue();
    }
}