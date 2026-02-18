package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;

import libs.ddd.ValueObject;
import libs.errs.Err;
import libs.errs.Error;
import libs.errs.Result;
import lombok.Getter;

import java.util.List;

@Getter
public class Name extends ValueObject<Name> {
    private final String value;

    private Name(String value) {
        this.value = value;
    }

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
