package com.sitionix.ntfssox.consumer.mapper;

public interface EventContentMapper<TO, FROM> {

    Class<FROM> supports();

    TO asNotification(FROM event);
}
