package tmpsandbox.microarch.ddd.delivery.core.application.command.courier;

import java.util.UUID;

public record CreateNewStoragePlace(
    UUID courierId,
    String name,
    int totalVolume
) {
}
