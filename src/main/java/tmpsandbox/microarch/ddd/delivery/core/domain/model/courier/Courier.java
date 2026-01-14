package tmpsandbox.microarch.ddd.delivery.core.domain.model.courier;

import libs.ddd.Aggregate;
import libs.errs.Error;
import libs.errs.Except;
import libs.errs.Result;
import libs.errs.UnitResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.kernel.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.kernel.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@Getter
public class Courier extends Aggregate<UUID> {
    private static final Name BAG = Name.create("Сумка").getValue();
    private static final Volume BAG_CAPACITY = Volume.create(10).getValue();

    private final Name name;

    private final Speed speed;

    private Location location;

    private final List<StoragePlace> storagePlaces = new ArrayList<>();

    private Courier(Name name, Speed speed, Location location) {
        super(UUID.randomUUID());
        this.name = name;
        this.speed = speed;
        this.location = location;
        storagePlaces.add(StoragePlace.create(BAG, BAG_CAPACITY).getValue());
    }

    public static Result<Courier, Error> create(Name name, Speed speed, Location location) {
        Except.againstNull(name, "name");
        Except.againstNull(speed, "speed");
        Except.againstNull(location, "location");

        return Result.success(new Courier(name, speed, location));
    }

    public UnitResult<Error> addStoragePlace(String name, int volume) {
        Result<StoragePlace, Error> storagePlaceResult = StoragePlace.create(Name.create(name).getValue(), Volume.create(volume).getValue());

        if (storagePlaceResult.isFailure()) {
            return UnitResult.failure(storagePlaceResult.getError());
        }

        storagePlaces.add(storagePlaceResult.getValue());

        return UnitResult.success();
    }

    // Курьер должен уметь проверять, «может ли он взять заказ?» Может, если в одном из его мест хранения есть место.
    public Result<Boolean, Error> canTakeOrder(Order order) {
        Result<StoragePlace, Error> resultFind = findStoragePlace(order);

        if (resultFind.isFailure()) {
            return Result.failure(resultFind.getError());
        }

        return Result.success(true);
    }

    // Курьер может взять заказ, если предыдущее условие выполняется. В место хранения при этом устанавливается ID заказа.
    public UnitResult<Error> takeOrder(Order order) {
        Result<StoragePlace, Error> resultFind = findStoragePlace(order);

        if (resultFind.isFailure()) {
            return UnitResult.failure(resultFind.getError());
        }

        StoragePlace storagePlace = resultFind.getValue();
        storagePlace.storeOrder(order);

        return UnitResult.success();
    }

    private Result<StoragePlace, Error> findStoragePlace(Order order) {
        return storagePlaces.stream()
                .filter(sp -> !sp.isOccupied() && sp.getTotalVolume().isGreaterOrEqual(order.getVolume()))
                .findFirst()
                .<Result<StoragePlace, Error>>map(Result::success)
                .orElseGet(() -> Result.failure(Error.of("StoragePlace", "not available")));
    }

    // Курьер может завершить заказ. При этом место хранения очищается.
    public UnitResult<Error> completeOrder(Order order) {
        Optional<StoragePlace> result = storagePlaces.stream()
                .filter(sp -> order.getId().equals(sp.getOrderId()))
                .findFirst();

        if (result.isEmpty()) {
            return UnitResult.failure(Error.of("Order", "not found"));
        }

        StoragePlace storagePlace = result.get();
        storagePlace.clear();

        return UnitResult.success();
    }

    // Курьер должен уметь возвращать количество шагов, которое он потенциально затратит на путь до локации заказа. При расчете нужно учесть скорость курьера. К примеру:
    public Result<Double, Error> calculateTimeToLocation(Location location) {
        Result<Integer, Error> distanceResult = location.distanceTo(this.location);

        if (distanceResult.isFailure()) {
            return Result.failure(distanceResult.getError());
        }

        return Result.success(speed.timeForDistance(distanceResult.getValue()));
    }

    // Курьер может переместиться на один шаг в сторону Location заказа. При этом его Location меняется. Возьмите алгоритм из примера
    public UnitResult<Error> move(Location target) {
        if (target == null) {
            return UnitResult.failure(Error.of("target", "value is required"));
        }

        int difX = target.getX() - location.getX();
        int difY = target.getY() - location.getY();

        int cruisingRange = speed.getValue();

        int moveX = Math.max(-cruisingRange, Math.min(difX, cruisingRange));
        cruisingRange -= Math.abs(moveX);

        int moveY = Math.max(-cruisingRange, Math.min(difY, cruisingRange));

        Result<Location, Error> locationCreateResult = Location.create(
                location.getX() + moveX,
                location.getY() + moveY
        );

        if (locationCreateResult.isFailure()) {
            return UnitResult.failure(locationCreateResult.getError());
        }

        this.location = locationCreateResult.getValue();
        return UnitResult.success();
    }
}
