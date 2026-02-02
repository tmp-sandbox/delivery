package tmpsandbox.microarch.ddd.delivery.core.application.command.order;

import java.util.UUID;

public record CreateNewOrderCommand(
    UUID orderId,
    String street,
    int volume
) {
}
