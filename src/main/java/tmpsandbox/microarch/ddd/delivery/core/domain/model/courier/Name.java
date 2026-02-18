package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;

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
public class Name extends ValueObject<Name> {
    private String value;

    public static Result<Name, Error> create(String value) {
        var validation = Err.againstNullOrEmpty(value, "name");

        if (validation.isFailure()) {
            return Result.failure(validation.getError());
        }

        return Result.success(new Name(value));
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(value);
    }
}
