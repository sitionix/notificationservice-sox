package com.sitionix.ntfssox.domain.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageProperties {

    private List<VerifyChannel> allowedChannels;
    private Map<VerifyChannel, Map<String, Object>> delivery;
}
