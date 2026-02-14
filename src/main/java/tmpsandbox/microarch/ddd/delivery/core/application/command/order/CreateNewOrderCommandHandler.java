package tmpsandbox.microarch.ddd.delivery.core.application.command.order;

import libs.errs.Error;
import libs.errs.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.common.event.DomainEventPublisher;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Volume;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.order.Order;
import tmpsandbox.microarch.ddd.delivery.core.port.GeoClient;
import tmpsandbox.microarch.ddd.delivery.core.port.OrderRepository;

import java.util.List;
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
@Slf4j
public class CreateNewOrderCommandHandler {
    private final OrderRepository orderRepository;
    private final GeoClient geoClient;
    private final DomainEventPublisher domainEventPublisher;

    public Result<UUID, Error> handle(CreateNewOrderCommand createNewOrderCommand) {
        Result<Location, Error> locationResponse = geoClient.getLocation(createNewOrderCommand.street());

        if (locationResponse.isFailure()) {
            String error = String.format("Failed to get location for street %s", createNewOrderCommand.street());

            log.error(error);
            return Result.failure(Error.of("GeoClient", error));
        }

        var location = Location.create(1, 1).getValue();

        Result<Order, Error> orderResult = Order.create(createNewOrderCommand.orderId(), location, Volume.create(createNewOrderCommand.volume()).getValue());
        if (orderResult.isFailure()) {
            return Result.failure(orderResult.getError());
        }

        orderRepository.save(orderResult.getValue());

        log.info("Created new order: {}", orderResult.getValue().getId());

        domainEventPublisher.publish(List.of(orderResult.getValue()));
        return Result.success(orderResult.getValue().getId());
    }
}
