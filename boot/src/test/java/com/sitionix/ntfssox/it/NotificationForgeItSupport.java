package com.sitionix.ntfssox.it;

import com.sitionix.forgeit.core.annotation.ForgeFeatures;
import com.sitionix.forgeit.core.api.ForgeIT;
import com.sitionix.forgeit.kafka.api.KafkaSupport;
import com.sitionix.forgeit.wiremock.api.WireMockSupport;

@ForgeFeatures({WireMockSupport.class, KafkaSupport.class})
public interface NotificationForgeItSupport extends ForgeIT {
}
