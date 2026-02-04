package tmpsandbox.microarch.ddd.delivery.core.application.queries;

import java.util.UUID;

public record GetAllCouriersQueryResponse(
    UUID id,
    String name,
    String x,
    String y
) {
}
