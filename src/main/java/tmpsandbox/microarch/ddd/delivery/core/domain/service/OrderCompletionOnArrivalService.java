package tmpsandbox.microarch.ddd.delivery.core.domain.service;

import libs.errs.Error;
import libs.errs.UnitResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;

@Service
@Slf4j
public class OrderCompletionOnArrivalService {

    public void moveAndCompleteIfArrived(Courier courier, Order order) {
        Location location = courier.getLocation();
        UnitResult<Error> move = courier.move(order.getLocation());
        log.info("Move courier: {}, from: {}, to: {}", courier.getId(), location, courier.getLocation());

        if (move.isFailure()) {
            log.error("Cant move courier: {}, to order: {}", courier, order);
            return;
        }

        if (courier.getLocation().equals(order.getLocation())) {
            order.complete();
            courier.completeOrder(order);
        }
    }
}
