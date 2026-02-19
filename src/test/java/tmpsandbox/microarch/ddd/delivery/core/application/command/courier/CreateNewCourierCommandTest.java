package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CreateNewCourierCommandTest {
    private static final String TEST_NAME = "test";
    private static final Integer TEST_SPEED = 5;

    public static Stream<String> provideNames() {
        return Stream.of(null, "", "   ");
    }

    @ParameterizedTest
    @MethodSource("provideNames")
    public void shouldReturnError_whenInvalidName(String name) {
        // Given, When:
        var createNewCourierCommandResult = CreateNewCourierCommand.create(
            name,
            TEST_SPEED
        );

        // Then:
        assertThat(createNewCourierCommandResult.isFailure()).isTrue();
    }

    @Test
    public void shouldReturnError_whenInvalidSpeed() {
        // Given, When:
        var createNewCourierCommandResult = CreateNewCourierCommand.create(
            TEST_NAME,
            -1
        );

        // Then:
        assertThat(createNewCourierCommandResult.isFailure()).isTrue();
    }
}