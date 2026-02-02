package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

import java.util.UUID;

public record CreateNewStoragePlaceCommand(
    UUID courierId,
    String name,
    int totalVolume
) {
}
