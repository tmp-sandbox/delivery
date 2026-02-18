package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NameTest {
    public static Stream<String> provideIncorrectNames() {
        return Stream.of(null, "");
    }

    @Test
    public void shouldCreateName_whenCorrectValue() {
        // Given:
        String value = "рюкзак";

        // When:
        var name = Name.create(value).getValue();

        // Then:
        assertThat(name.getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource("provideIncorrectNames")
    public void shouldReturnError_whenIncorrectValue(String name) {
        // Given, When:
        var result = Name.create(name);

        // Then:
        assertThat(result.isFailure()).isTrue();
    }
}