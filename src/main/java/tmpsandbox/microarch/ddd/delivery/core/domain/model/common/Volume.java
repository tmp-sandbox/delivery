package tmpsandbox.microarch.ddd.delivery.core.domain.model.common;

import jakarta.persistence.Embeddable;
import libs.ddd.ValueObject;
import libs.errs.Err;
import libs.errs.Error;
import libs.errs.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Volume extends ValueObject<Volume> {
    private int value;

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
