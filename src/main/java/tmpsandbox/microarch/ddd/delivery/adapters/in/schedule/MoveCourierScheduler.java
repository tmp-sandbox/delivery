package tmpsandbox.microarch.ddd.delivery.adapters.in.schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tmpsandbox.microarch.ddd.delivery.core.application.command.courier.MoveCourierCommandHandler;

@Component
@Slf4j
@AllArgsConstructor
public class MoveCourierScheduler {
    private final MoveCourierCommandHandler handler;

    @Scheduled(fixedDelay = 5000)
    public void move() {
        log.info("Start moving");
        handler.handle();
        log.info("Finish moving");
    }
}
