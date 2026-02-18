package tmpsandbox.microarch.ddd.delivery.core.domain.model;

import libs.ddd.ValueObject;
import libs.errs.*;
import libs.errs.Error;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Location extends ValueObject<Location> {
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 10;

    private final int x;
    private final int y;

    public static Result<Location, Error> create(int x, int y) {
        var validation = UnitResult.combine(
                Err.againstOutOfRange(x, MIN_VALUE, MAX_VALUE, "x"),
                Err.againstOutOfRange(y, MIN_VALUE, MAX_VALUE, "y")
        );

        if (validation.isFailure()) return Result.failure(validation.getError());

        return Result.success(new Location(x, y));
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(this.x, this.y);
    }

    public Result<Integer, Error> distanceTo(Location locationTo) {
        if (locationTo == null) {
            return Result.failure(GeneralErrors.valueIsRequired("locationTo"));
        }

        Integer distance = Math.abs(x - locationTo.x) + Math.abs(y - locationTo.y);
        return Result.success(distance);
    }
}
