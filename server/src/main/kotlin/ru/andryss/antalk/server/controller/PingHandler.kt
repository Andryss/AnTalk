package ru.andryss.antalk.server.controller

import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class PingHandler {
    @MessageMapping("/ping")
    @SendTo("/pong")
    fun ping(message: Message<ByteArray>): String {
        return message.payload.decodeToString()
    }
}