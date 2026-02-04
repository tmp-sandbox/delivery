package tmpsandbox.microarch.ddd.delivery.core.application.command.order;

import libs.errs.Result;
import libs.errs.UnitResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.ports.OrderRepository;

import java.util.UUID;

/*
Создать заказ
Сервис Delivery создает заказ в результате оформления корзины
Сообщение "Корзина оформлена" будет приходить из Kafka, как только мы реализуем интеграцию с Basket
А пока нам достаточно реализовать Use Case создания заказа
Поля Command:
- OrderID UUID
- street string
- volume int

В следующих уроках мы будем передавать street в сервис Geo и получать Location
Но пока у нас нет этой интеграции — используйте рандомную Location для создания заказа
 */
@Service
@AllArgsConstructor
public class CreateNewOrderCommandHandler {
    private final OrderRepository orderRepository;

    public Result<UUID, Error> handle(CreateNewOrderCommand createNewOrderCommand) {
        var location = Location.create(1, 1).getValue();

        Result<Order, Error> orderResult = Order.create(createNewOrderCommand.orderId(), location, Volume.create(createNewOrderCommand.volume()).getValue());
        if (orderResult.isFailure()) {
            return Result.failure(orderResult.getError());
        }

        orderRepository.save(orderResult.getValue());
        return Result.success(orderResult.getValue().getId());
    }
}
