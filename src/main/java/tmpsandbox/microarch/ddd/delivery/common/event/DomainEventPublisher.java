package tmpsandbox.microarch.ddd.delivery.common.event;

import libs.ddd.Aggregate;

public interface DomainEventPublisher {
    void publish(Iterable<Aggregate<?>> aggregates);
}