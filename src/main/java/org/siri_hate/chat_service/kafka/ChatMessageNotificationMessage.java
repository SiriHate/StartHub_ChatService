package org.siri_hate.chat_service.kafka;

import java.util.List;

public record ChatMessageNotificationMessage(
        Long chatId,
        String senderUsername,
        String content,
        String sendAt,
        List<String> recipients
) {
}
