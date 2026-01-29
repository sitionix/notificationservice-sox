package com.sitionix.ntfssox.consumer.mapper.registry;

import com.sitionix.ntfssox.consumer.mapper.EventContentMapper;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    private final Map<Class<?>, EventContentMapper<?, ?>> mappers;

    public NotificationMapper(final List<EventContentMapper<?, ?>> mappers) {
        this.mappers = mappers.stream()
                .collect(Collectors.toMap(
                        EventContentMapper::supports,
                        Function.identity(),
                        (left, right) -> {
                            throw new IllegalStateException("Duplicate mapper for type: " + left.supports().getName());
                        }
                ));
    }

    public <TO, FROM> TO asNotification(final FROM event) {
        if (event == null) {
            return null;
        }

        final EventContentMapper<TO, FROM> mapper = findMapper(event.getClass());
        if (mapper == null) {
            throw new IllegalArgumentException("No content mapper found for type: " + event.getClass().getName());
        }

        return mapper.asNotification(event);
    }

    @SuppressWarnings("unchecked")
    private <TO, FROM> EventContentMapper<TO, FROM> findMapper(final Class<?> type) {
        final EventContentMapper<TO, FROM> direct =
                (EventContentMapper<TO, FROM>) mappers.get(type);
        if (direct != null) {
            return direct;
        }

        return (EventContentMapper<TO, FROM>) mappers.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(type))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
}
