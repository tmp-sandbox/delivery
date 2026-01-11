package tmpsandbox.microarch.ddd.delivery.core.domain.model.kernel;

import libs.ddd.ValueObject;
import libs.errs.Err;
import libs.errs.Error;
import libs.errs.Result;
import lombok.Getter;

import java.util.List;

@Getter
public final class Volume extends ValueObject<Volume> {
    private final int value;

    private Volume(int value) {
        this.value = value;
    }

    public static Result<Volume, Error> create(int value) {
        var validation = Err.againstZeroOrNegative(value, "totalValue");

        if (validation.isFailure()) {
            return Result.failure(validation.getError());
        }

        return Result.success(new Volume(value));
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(value);
    }

    public boolean isLessThan(Volume other) {
        return this.compareTo(other) < 0;
    }

    public boolean isGreaterThan(Volume other) {
        return this.compareTo(other) > 0;
    }

    public boolean isLessOrEqual(Volume other) {
        return this.compareTo(other) <= 0;
    }

    public boolean isGreaterOrEqual(Volume other) {
        return this.compareTo(other) >= 0;
    }
}
