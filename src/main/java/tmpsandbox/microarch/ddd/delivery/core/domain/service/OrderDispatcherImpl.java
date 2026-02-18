package tmpsandbox.microarch.ddd.delivery.core.domain.service;

import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Status;

import java.util.Comparator;
import java.util.List;

@Service
public class OrderDispatcherImpl implements OrderDispatcher {

    /*
        Алгоритм работы
        За 1 раз мы диспетчеризуем только 1 заказ (для упрощения)
        1 курьер может брать только 1 заказ (для упрощения)

        Логика диспетчеризации:
        Считаем время доставки заказа для каждого курьера, учитывая его текущее местоположение
        Побеждает курьер, который потенциально быстрее всего доставит заказ, его и назначаем на заказ

        Курьер может взять заказ, только если у него есть подходящее место хранения (CanTakeOrder() == true).  +

        Если подходящий курьер найден:
        Назначаем курьера на заказ
        Курьер берет заказ (занимает место хранения)
        Возвращаем победителя
        Допущения
        Считаем, что посылка (заказ) появляется у курьера сразу после назначения, ему не надо ехать на склад и потом к клиенту.
        Курьер начинает доставку из его текущего Location и завершает в Location заказа
    * */
    @Override
    public Courier dispatch(Order order, List<Courier> couriers) {
        if (order.getStatus() != Status.CREATED) {
            throw new IllegalArgumentException("Order status is not CREATED");
        }

        if (couriers.isEmpty()) {
            return null;
        }

        List<Courier> couriersCanTakeOrder = couriers.stream()
                .filter(courier -> courier.canTakeOrder(order).getValue())
                .toList();

        return couriersCanTakeOrder.stream()
                .min(Comparator.comparing(courier -> courier.calculateTimeToLocation(order.getLocation()).getValue()))
                .map(courier -> {
                    courier.takeOrder(order);
                    order.assign(courier);
                    return courier;
                })
                .orElse(null);
    }
}
