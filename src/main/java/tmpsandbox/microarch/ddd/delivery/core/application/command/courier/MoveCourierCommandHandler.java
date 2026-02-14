package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.common.event.DomainEventPublisher;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.domain.service.OrderCompletionOnArrivalService;
import tmpsandbox.microarch.ddd.delivery.core.port.CourierRepository;
import tmpsandbox.microarch.ddd.delivery.core.port.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/*
Переместить курьеров
В реальной системе мы бы получали координаты от самих курьеров. В нашей системе будет Job, который срабатывает каждую секунду.
Сам Job мы сделаем в следующих уроках. При срабатывании Job будет вызываться этот Use Case.
Поэтому нам нужно реализовать Use Case, который смещает всех курьеров на 1 шаг в сторону заказа со скоростью их транспорта.
Если курьер доставил заказ (координаты курьера и заказа совпадают), то в рамках этого же Use Case мы завершаем
заказ (переводим в Completed), а курьер освобождает место хранения.

Поля Command:
В данном Command нет полей
 */
@Service
@AllArgsConstructor
@Slf4j
public class MoveCourierCommandHandler {
    private final CourierRepository courierRepositoryImpl;
    private final OrderRepository orderRepositoryImpl;
    private final OrderCompletionOnArrivalService orderCompletionOnArrivalService;
    private final DomainEventPublisher domainEventPublisher;

    public void handle() {
        List<Courier> busyCouriers = courierRepositoryImpl.findBusy();

        if (busyCouriers.isEmpty()) {
            log.info("No busy couriers found");
            return;
        }

        List<UUID> orderIds = busyCouriers.stream()
            .map(Courier::getOrderId)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        Map<UUID, Order> ordersById = orderRepositoryImpl.findAllByIds(orderIds);

        if (ordersById.isEmpty()) {
            log.info("Not found orders");
            return;
        }

        busyCouriers.forEach(courier -> moveAndSave(courier, ordersById.get(courier.getOrderId().get())));
    }

    private void moveAndSave(Courier courier, Order order) {
        orderCompletionOnArrivalService.moveAndCompleteIfArrived(courier, order);

        courierRepositoryImpl.save(courier);
        orderRepositoryImpl.save(order);

        domainEventPublisher.publish(List.of(order));
    }
}
