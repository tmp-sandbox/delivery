package tmpsandbox.microarch.ddd.delivery.core.application.query;

import java.util.UUID;

public record GetAllCouriersQueryResponse(
    UUID id,
    String name,
    String x,
    String y
) {
}
