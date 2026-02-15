package tmpsandbox.microarch.ddd.delivery.adapters.out.postgres.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import libs.ddd.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobScheduler {
    private final ApplicationEventPublisher publisher;
    private final OutboxJpaRepository jpa;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 1000)
    public void run() {
        log.info("Start outbox processing");
        var outboxMessages = jpa.findUnprocessedMessages();
        for (var outboxMessage : outboxMessages) {
            try {
                // Динамически находим класс события
                var eventClassName = outboxMessage.getEventType();
                var eventClass = Class.forName(eventClassName);
                var eventObject = objectMapper.readValue(outboxMessage.getPayload(), eventClass);

                // Проверяем, что это DomainEvent
                if (!(eventObject instanceof DomainEvent domainEvent)) {
                    throw new IllegalStateException("Invalid outbox message type: " + eventClass);
                }

                // Публикуем доменное событие
                publisher.publishEvent(domainEvent);

                // Отмечаем как отправленное
                outboxMessage.markAsProcessed();
                jpa.save(outboxMessage);
            } catch (Exception e) {
                log.error("Failed to publish outbox message:{} ", e.getMessage());
            }
        }

        log.info("End outbox processing");
    }
}