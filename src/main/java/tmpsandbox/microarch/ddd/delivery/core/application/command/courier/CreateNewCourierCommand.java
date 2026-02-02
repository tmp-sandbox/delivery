package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

public record CreateNewCourier(
    String name,
    int speed
) {
}
