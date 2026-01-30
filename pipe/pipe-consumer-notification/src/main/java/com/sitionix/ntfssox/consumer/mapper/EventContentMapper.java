package com.sitionix.ntfssox.consumer.mapper;

public interface EventContentMapper<T, E> {

    Class<E> supports();

    T asNotification(E event);
}
