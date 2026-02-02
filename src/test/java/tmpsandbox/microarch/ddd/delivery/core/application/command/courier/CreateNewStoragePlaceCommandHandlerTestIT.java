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

    public static Stream<String> provideNames() {
        return Stream.of(null, "", "   ");
    }

    @Test
    public void shouldCreateNewCourier_whenHandle() {
        // Given:
        CreateNewCourierCommand createNewCourierCommand = new CreateNewCourierCommand(
            TEST_NAME,
            TEST_SPEED
        );

        // When:
        var handle = handler.handle(createNewCourierCommand);

        // Then:
        assertThat(handle.isFailure()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("provideNames")
    public void shouldReturnError_whenInvalidName(String name) {
        // Given:
        CreateNewCourierCommand createNewCourierCommand = new CreateNewCourierCommand(
            name,
            TEST_SPEED
        );

        // When:
        var handle = handler.handle(createNewCourierCommand);

        // Then:
        assertThat(handle.isFailure()).isTrue();
    }

    @Test
    public void shouldReturnError_whenInvalidSpeed() {
        // Given:
        CreateNewCourierCommand createNewCourierCommand = new CreateNewCourierCommand(
            TEST_NAME,
            -1
        );

        // When:
        var handle = handler.handle(createNewCourierCommand);

        // Then:
        assertThat(handle.isFailure()).isTrue();
    }
}