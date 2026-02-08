package com.self.chat_engine.model.enums;

import jakarta.websocket.Decoder;
import org.springframework.web.servlet.tags.form.TextareaTag;

public enum ChannelType {
    TEXT,
    VOICE,
    VIDEO;
}
