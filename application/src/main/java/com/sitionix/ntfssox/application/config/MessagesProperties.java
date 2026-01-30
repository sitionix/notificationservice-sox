package com.sitionix.ntfssox.application.config;

import com.sitionix.ntfssox.domain.model.MessageProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "notification")
public class MessagesProperties {

    private Map<String, MessageProperties> messages = new HashMap<>();
}
