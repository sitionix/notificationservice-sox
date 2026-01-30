package com.sitionix.ntfssox.it.config;

import com.app_afesox.ntfssox.events.notifications.NotificationEvent;
import com.app_afesox.ntfssox.events.notifications.contents.EmailVerificationContentDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("it")
public class ItJacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer notificationEventContentCustomizer() {
        return builder -> builder.mixIn(NotificationEvent.class, NotificationEventMixin.class);
    }

    private abstract static class NotificationEventMixin {
        @JsonDeserialize(as = EmailVerificationContentDTO.class)
        public abstract Object getContent();
    }
}
