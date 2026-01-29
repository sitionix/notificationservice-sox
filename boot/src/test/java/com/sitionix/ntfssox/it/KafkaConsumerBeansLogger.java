package com.sitionix.ntfssox.it;

import com.app_afesox.ntfssox.events.notifications.kafka.NotificationsV1ConsumerHandler;
import com.app_afesox.ntfssox.events.notifications.kafka.NotificationsV1ConsumerRunner;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("it")
public class KafkaConsumerBeansLogger implements ApplicationRunner {

    private final ApplicationContext context;

    public KafkaConsumerBeansLogger(final ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(final ApplicationArguments args) {
        final Map<String, NotificationsV1ConsumerHandler> handlers =
                this.context.getBeansOfType(NotificationsV1ConsumerHandler.class);
        log.info("IT DIAG: NotificationsV1ConsumerHandler beans: {}", handlers.keySet());
        handlers.forEach((name, bean) ->
                log.info("IT DIAG: handler bean {} -> {}", name, bean.getClass().getName()));

        final Map<String, NotificationsV1ConsumerRunner> runners =
                this.context.getBeansOfType(NotificationsV1ConsumerRunner.class);
        log.info("IT DIAG: NotificationsV1ConsumerRunner beans: {}", runners.keySet());
        runners.forEach((name, bean) ->
                log.info("IT DIAG: runner bean {} -> {}", name, bean.getClass().getName()));
    }
}
