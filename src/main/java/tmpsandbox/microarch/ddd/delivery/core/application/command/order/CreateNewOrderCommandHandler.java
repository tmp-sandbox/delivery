package tmpsandbox.microarch.ddd.delivery.core.application.command;

import libs.ddd.CommandHandler;
import org.springframework.stereotype.Service;

@Service
public class CreateNewOrderCommandHandler implements CommandHandler<CreateNewOrder> {

    // Необходимо создать заказ
    @Override
    public void handle(CreateNewOrder createNewOrder) {

    }
}
