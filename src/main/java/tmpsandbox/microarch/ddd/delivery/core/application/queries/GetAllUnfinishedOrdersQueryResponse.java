package tmpsandbox.microarch.ddd.delivery.core.application.queries;

import java.util.UUID;

public record GetAllUnfinishedOrdersQueryResponse(
    UUID id,
    String x,
    String y
) {
}
