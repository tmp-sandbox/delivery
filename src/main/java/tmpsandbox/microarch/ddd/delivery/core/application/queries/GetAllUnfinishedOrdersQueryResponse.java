package tmpsandbox.microarch.ddd.delivery.core.application.queries;

import java.util.UUID;

public record GetAllUnfinishedOrdersQueryResponse(
    UUID id,
    Integer x,
    Integer y
) {
}
