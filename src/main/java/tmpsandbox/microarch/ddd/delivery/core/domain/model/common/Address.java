package tmpsandbox.microarch.ddd.delivery.core.domain.model.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import libs.ddd.ValueObject;
import libs.errs.Err;
import libs.errs.Error;
import libs.errs.Result;
import libs.errs.UnitResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public  class Address extends ValueObject<Address> {
    private String country;

    private String city;

    private String street;

    private String house;

    private String apartment;

    public static Result<Address, Error> create(String country, String city, String street, String house, String apartment) {
        var validation = UnitResult.combine(
                Err.againstNullOrEmpty(country, "country"),
                Err.againstNullOrEmpty(city, "city"),
                Err.againstNullOrEmpty(street, "street"),
                Err.againstNullOrEmpty(house, "house"),
                Err.againstNullOrEmpty(apartment, "apartment"));
        if (validation.isFailure())
            return Result.failure(validation.getError());

        return Result.success(new Address(country, city, street, house, apartment));
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(this.country, this.city, this.street, this.house, this.apartment);
    }
}