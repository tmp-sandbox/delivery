package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class SpeedTest {


    @Test
    public void shouldCreteSpeed() {
        // Given:
        int value = 1;

        // When:
        Speed speed = Speed.create(value).getValue();

        // Then:
        assertThat(speed.getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    public void shouldGetError_whenValueSpeedInvalid(int value) {
        // Given, When:
        var result = Speed.create(value);

        // Then:
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    public void shouldBeEquals_whenSameValue() {
        // Given:
        int value = 1;

        // When:
        Speed first = Speed.create(value).getValue();
        Speed second = Speed.create(value).getValue();

        // Then:
        assertThat(first).isEqualTo(second);
    }

    @Test
    public void shouldNotBeEquals_whenDifferentValue() {
        // Given:
        int valueFirst = 1;
        int valueSecond = 2;

        // When:
        Speed first = Speed.create(valueFirst).getValue();
        Speed second = Speed.create(valueSecond).getValue();

        // Then:
        assertThat(first).isNotEqualTo(second);
    }

    public static Stream<Arguments> provideDistances() {
        return Stream.of(
                Arguments.of(10, 1, 10.0),
                Arguments.of(10, 2, 5.0),
                Arguments.of(14, 2, 7.0),
                Arguments.of(14, 5, 3.0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDistances")
    public void shouldCalculateCorrectDistance_whenCallTimeFor(
            int distance,
            int speedValue,
            double expected
    ) {
        // Given:
        Speed speed = Speed.create(speedValue).getValue();

        // When:
        double measures = speed.timeForDistance(distance);

        // Then:
        assertThat(measures).isEqualTo(expected);
    }
}