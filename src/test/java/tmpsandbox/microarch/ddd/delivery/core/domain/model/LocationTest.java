package tmpsandbox.microarch.ddd.delivery.core.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.kernel.Location;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class LocationTest {
    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "2, 2",
            "3, 3",
            "4, 4",
            "5, 5",
            "6, 6",
            "7, 7",
            "8, 8",
            "9, 9",
            "10, 10",
    })
    void shouldBeCorrect_whenParamsAreCorrectOnCreated(int x, int y) {
        // Given, When:
        var result = Location.create(x, y).getValue();

        // Then:
        assertThat(result.getX()).isEqualTo(x);
        assertThat(result.getY()).isEqualTo(y);
    }

    @Test
    public void shouldBeEquals_whenAllPropertiesIsEqual() {
        // Given:
        Location first = Location.create(1, 1).getValue();
        Location second = Location.create(1, 1).getValue();

        // When:
        boolean result = first.equals(second);

        // Then:
        assertThat(result).isTrue();
    }

    @Test
    public void shouldBeNotEquals_whenOneOfPropertiesIsNotEqual() {
        // Given:
        Location first = Location.create(1, 1).getValue();
        Location second = Location.create(1, 2).getValue();

        // When:
        boolean result = first.equals(second);

        // Then:
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "11, 11",
            "0, 11",
            "1, 11",
            "0, 5"
    })
    void shouldReturnErrorWhenParamsAreNotCorrectOnCreated(int x, int y) {
        // Given, When:
        var result = Location.create(x, y);

        // Then:
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError()).isNotNull();
    }

    @Test
    void shouldReturnError_whenDistanceToIsNull() {
        // Given:
        Location location = Location.create(1, 1).getValue();

        //  When:
        var result = location.distanceTo(null);

        // Then:
        assertThat(result.isFailure()).isTrue();
    }

    public static Stream<Arguments> provideLocations() {
        return Stream.of(
                Arguments.of(Location.create(4, 9).getValue(), Location.create(2, 6).getValue(), 5),
                Arguments.of(Location.create(1, 10).getValue(), Location.create(10, 10).getValue(), 9),
                Arguments.of(Location.create(5, 5).getValue(), Location.create(5, 5).getValue(), 0),
                Arguments.of(Location.create(5, 5).getValue(), Location.create(6, 5).getValue(), 1),
                Arguments.of(Location.create(5, 5).getValue(), Location.create(5, 6).getValue(), 1),
                Arguments.of(Location.create(5, 5).getValue(), Location.create(6, 6).getValue(), 2),
                Arguments.of(Location.create(1, 1).getValue(), Location.create(10, 10).getValue(), 18),
                Arguments.of(Location.create(1, 2).getValue(), Location.create(5, 2).getValue(), 4)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLocations")
    void shouldReturnCorrectDistanceBetweenLocations_whenCallDistanceTo(Location from, Location to, int expectedDistance) {
        // Given, When:
        Integer distance = from.distanceTo(to).getValue();

        // Then:
        assertThat(distance).isEqualTo(expectedDistance);
    }
}