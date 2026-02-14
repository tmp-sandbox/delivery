package tmpsandbox.microarch.ddd.delivery.core.application.command;

import jakarta.transaction.Transactional;
import libs.errs.Error;
import libs.errs.UnitResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.domain.service.OrderDispatcher;
import tmpsandbox.microarch.ddd.delivery.core.port.CourierRepository;
import tmpsandbox.microarch.ddd.delivery.core.port.OrderRepository;

import java.util.List;
import java.util.Optional;

/*
Назначить заказ на курьера
Система сама распределяет заказы. Она берет первый неназначенный заказ и
ищет самого подходящего курьера. Алгоритм назначения вы реализовали в 5 модуле.
Этот Use Case также будет запускаться с помощью Job, который
реализуем в следующих уроках. Сейчас нам важно просто его реализовать. +
В этом Use Case мы должны получить свободных курьеров и один любой неназначенный заказ,
затем передать их алгоритму скоринга. Используйте репозиторий.
Заказ и назначенного курьера мы сохраняем в БД одной транзакцией. Используйте Unit Of Work.
Поля Command:
- В данном Command нет полей
 */
@Service
@AllArgsConstructor
@Slf4j
public class OrderDispatcherCommandHandler {
    private final OrderDispatcher orderDispatcher;
    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public UnitResult<Error> handle() {
        List<Courier> freeCouriers = courierRepository.findFree();
        Optional<Order> unassignedOrder = orderRepository.findByStatusCreated();

        if (unassignedOrder.isEmpty()) {
            log.info("Not found free orders");
            return UnitResult.success();
        }

        if (freeCouriers.isEmpty()) {
            return UnitResult.failure(Error.of("orderByStatusCreated", "Cant find a free order for the courier"));
        }

        var dispatchedCourier = orderDispatcher.dispatch(unassignedOrder.get(), freeCouriers);

        if (dispatchedCourier.isEmpty()) {
            log.info("Cannot dispatch courier");
            return UnitResult.success();
        }

        orderRepository.save(unassignedOrder.get());
        courierRepository.save(dispatchedCourier.get());

        return UnitResult.success();
    }
}
