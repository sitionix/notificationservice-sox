package com.sitionix.ntfssox.application.config;

import com.sitionix.ntfssox.domain.model.NotificationHandler;
import com.sitionix.ntfssox.domain.model.NotificationTemplate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationTemplateInjector {

    private final ApplicationContext context;

    @PostConstruct
    public void injectHandlers() {
        final Map<String, NotificationHandler> handlers = this.context.getBeansOfType(NotificationHandler.class);

        Arrays.stream(NotificationTemplate.values())
                .forEach(template -> {
                    final NotificationHandler handler = handlers.get(template.getBindingKey());
                    if (handler == null) {
                        throw new IllegalStateException("No NotificationHandler bean for template: " + template.getBindingKey());
                    }
                    template.setHandler(handler);
                });
    }
}
