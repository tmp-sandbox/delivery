package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

public record CreateNewCourierCommand(
    String name,
    int speed
) {
}
