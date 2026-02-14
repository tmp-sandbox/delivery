package tmpsandbox.microarch.ddd.delivery.adapters.in.schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tmpsandbox.microarch.ddd.delivery.core.application.command.OrderDispatcherCommandHandler;

@Component
@Slf4j
@AllArgsConstructor
public class OrderDispatcherScheduler {
    private final OrderDispatcherCommandHandler handler;

    @Scheduled(fixedDelayString = "${schedule.order.dispatch-delay}")
    public void move() {
        log.info("Start dispatching");
        handler.handle();
        log.info("Finish dispatching");
    }
}
