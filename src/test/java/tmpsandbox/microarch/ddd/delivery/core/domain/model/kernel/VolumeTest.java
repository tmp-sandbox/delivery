package tmpsandbox.microarch.ddd.delivery.core.domain.model.kernel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class VolumeTest {

    @Test
    public void shouldCreateTotalVolume_whenCorrectValue() {
        // Given:
        int volume = 1;

        // When:
        var totalVolume = Volume.create(volume).getValue();

        // Then:
        assertThat(totalVolume.getValue()).isEqualTo(volume);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2})
    public void shouldReturnError_whenIncorrectValue(int volume) {
        // Given, When:
        var result = Volume.create(volume);

        // Then:
        assertThat(result.isFailure()).isTrue();
    }


    @Test
    public void shouldBeEquals_whenVolumeSame() {
        // Given:
        int volume = 1;
        var first = Volume.create(volume).getValue();
        var second = Volume.create(volume).getValue();

        // When:
        boolean isEqual = first.equals(second);

        // Then:
        assertThat(isEqual).isTrue();
    }

    @Test
    public void shouldNotBeEquals_whenVolumeDifferent() {
        // Given:
        var first = Volume.create(1).getValue();
        var second = Volume.create(2).getValue();

        // When:
        boolean isEqual = first.equals(second);

        // Then:
        assertThat(isEqual).isFalse();
    }

    @Test
    public void shouldCorrectCompare_whenLessThan() {
        // Given:
        var first = Volume.create(2).getValue();

        // When:
        boolean isLessThan = Volume.create(1).getValue().isLessThan(first);

        // Then:
        assertThat(isLessThan).isTrue();
    }

    @Test
    public void shouldCorrectCompare_whenGreaterThan() {
        // Given:
        var first = Volume.create(1).getValue();

        // When:
        boolean isLessThan = Volume.create(2).getValue().isGreaterThan(first);

        // Then:
        assertThat(isLessThan).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldCorrectCompare_whenLessOrEqualThan(int volume) {
        // Given:
        var first = Volume.create(volume).getValue();

        // When:
        boolean isLessThan = Volume.create(1).getValue().isLessOrEqual(first);

        // Then:
        assertThat(isLessThan).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    public void shouldCorrectCompare_whenGreaterOrEqualThan(int volume) {
        // Given:
        var first = Volume.create(volume).getValue();

        // When:
        boolean isLessThan = Volume.create(4).getValue().isGreaterOrEqual(first);

        // Then:
        assertThat(isLessThan).isTrue();
    }
}