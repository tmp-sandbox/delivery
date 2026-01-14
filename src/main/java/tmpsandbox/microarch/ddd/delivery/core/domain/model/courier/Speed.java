package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;

import libs.ddd.ValueObject;
import libs.errs.Err;
import libs.errs.Error;
import libs.errs.Result;
import lombok.Getter;

import java.util.List;

@Getter
public final class Speed extends ValueObject<Speed> {
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;

    private final int value;

    private Speed(int value) {
        this.value = value;
    }

    public static Result<Speed, Error> create(int value) {
        var validation = Err.againstOutOfRange(value, MIN_VALUE, MAX_VALUE, "speed");

        if (validation.isFailure()) {
            return Result.failure(validation.getError());
        }

        return Result.success(new Speed(value));
    }

    public double timeForDistance(int distance) {
        return Math.ceil((double) distance / value);
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(value);
    }
}
