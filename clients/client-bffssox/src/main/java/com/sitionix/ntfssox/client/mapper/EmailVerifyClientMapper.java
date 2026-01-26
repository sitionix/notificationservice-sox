package com.sitionix.ntfssox.client.mapper;

import com.app_afesox.bffssox.client.dto.EmailVerificationDTO;
import com.sitionix.ntfssox.domain.config.MapstructModel;
import com.sitionix.ntfssox.domain.model.Notification;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = MapstructModel.COMPONENT_MODEL)
public interface EmailVerifyClientMapper {

    @Mapping(target = "token", expression = "java(extractToken(payload, verifyUrl))")
    @Mapping(target = "siteId", source = "payload.meta.siteId")
    EmailVerificationDTO asEmailVerificationDto(Notification payload, String verifyUrl);

    default String extractToken(final Notification payload, final String verifyUrl) {
        return extractQueryParam(verifyUrl, "token");
    }

    private static String extractQueryParam(final String verifyUrl, final String name) {
        if (verifyUrl == null || verifyUrl.isBlank()) {
            return null;
        }

        String query = null;
        try {
            query = URI.create(verifyUrl).getRawQuery();
        } catch (IllegalArgumentException ignored) {
            query = null;
        }

        if (query == null) {
            final int index = verifyUrl.indexOf('?');
            if (index >= 0 && index + 1 < verifyUrl.length()) {
                query = verifyUrl.substring(index + 1);
            }
        }

        if (query == null || query.isBlank()) {
            return null;
        }

        for (String param : query.split("&")) {
            if (param.isEmpty()) {
                continue;
            }
            final int eqIndex = param.indexOf('=');
            final String rawKey = eqIndex >= 0 ? param.substring(0, eqIndex) : param;
            final String decodedKey = safeDecode(rawKey);
            if (!name.equals(decodedKey)) {
                continue;
            }
            if (eqIndex < 0) {
                return "";
            }
            return safeDecode(param.substring(eqIndex + 1));
        }

        return null;
    }

    private static String safeDecode(final String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ignored) {
            return value;
        }
    }
}
